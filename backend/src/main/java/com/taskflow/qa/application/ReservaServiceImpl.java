package com.taskflow.qa.application;

import com.taskflow.qa.api.dto.CriarReservaRequest;
import com.taskflow.qa.api.dto.RecursoResponse;
import com.taskflow.qa.api.dto.ReservaResponse;
import com.taskflow.qa.domain.StatusReserva;
import com.taskflow.qa.domain.TipoRecurso;
import com.taskflow.qa.persistence.entity.RecursoEntity;
import com.taskflow.qa.persistence.entity.ReservaEntity;
import com.taskflow.qa.persistence.entity.ReservaRecursoEntity;
import com.taskflow.qa.persistence.entity.UsuarioEntity;
import com.taskflow.qa.persistence.entity.AprovacaoEntity;
import com.taskflow.qa.domain.StatusAprovacao;
import com.taskflow.qa.persistence.repository.AprovacaoRepository;
import com.taskflow.qa.persistence.repository.BloqueioManutencaoRepository;
import com.taskflow.qa.persistence.repository.RecursoRepository;
import com.taskflow.qa.persistence.repository.ReservaRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReservaServiceImpl implements ReservaService {
    private final ReservaRepository reservas;
    private final RecursoRepository recursos;
    private final BloqueioManutencaoRepository bloqueios;
    private final AuditoriaService auditoria;
    private final NotificacaoService notificacoes;
    private final RegistroConflitoService conflitos;
    private final AprovacaoRepository aprovacoes;

    public ReservaServiceImpl(ReservaRepository reservas, RecursoRepository recursos,
                              BloqueioManutencaoRepository bloqueios, AuditoriaService auditoria,
                              NotificacaoService notificacoes, RegistroConflitoService conflitos,
                              AprovacaoRepository aprovacoes) {
        this.reservas = reservas;
        this.recursos = recursos;
        this.bloqueios = bloqueios;
        this.auditoria = auditoria;
        this.notificacoes = notificacoes;
        this.conflitos = conflitos;
        this.aprovacoes = aprovacoes;
    }

    @Transactional
    public ReservaResponse criar(CriarReservaRequest request, UsuarioEntity solicitante) {
        validarIntervalo(request.inicio(), request.fim());
        List<RecursoEntity> escolhidos = bloquearRecursos(request.recursoIds());
        validarDisponibilidade(escolhidos, request, null, solicitante.getId());
        ReservaEntity reserva = new ReservaEntity(solicitante, request.inicio(), request.fim(), request.finalidade());
        Map<Long, Integer> quantidades = quantidades(request);
        escolhidos.forEach(recurso -> reserva.adicionarRecurso(recurso, quantidades.getOrDefault(recurso.getId(), 1)));
        reserva.setStatus(escolhidos.stream().anyMatch(RecursoEntity::isRestrito)
                ? StatusReserva.AGUARDANDO_APROVACAO : StatusReserva.APROVADA);
        reservas.save(reserva);
        auditoria.registrar(solicitante, "CRIAR", "RESERVA", reserva.getId(), null, resumo(reserva));
        notificacoes.enviar(reserva, solicitante, "Reserva criada: " + reserva.getStatus());
        return response(reserva);
    }

    @Transactional
    public ReservaResponse atualizar(Long reservaId, CriarReservaRequest request, UsuarioEntity ator) {
        ReservaEntity reserva = obterReserva(reservaId);
        verificarPropriedade(reserva, ator);
        if (reserva.getStatus() == StatusReserva.CANCELADA || reserva.getStatus() == StatusReserva.RECUSADA) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esta reserva não pode mais ser alterada.");
        }
        validarIntervalo(request.inicio(), request.fim());
        List<Long> ids = new ArrayList<>(request.recursoIds());
        reserva.getRecursos().forEach(rr -> ids.add(rr.getRecurso().getId()));
        List<RecursoEntity> bloqueados = bloquearRecursos(ids.stream().distinct().toList());
        Map<Long, RecursoEntity> porId = new HashMap<>();
        bloqueados.forEach(r -> porId.put(r.getId(), r));
        List<RecursoEntity> novos = request.recursoIds().stream().map(porId::get).toList();
        if (novos.contains(null)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recurso não encontrado.");
        validarDisponibilidade(novos, request, reservaId, ator.getId());
        String anterior = resumo(reserva);
        reserva.atualizar(request.inicio(), request.fim(), request.finalidade(), novos, quantidades(request));
        reserva.setStatus(novos.stream().anyMatch(RecursoEntity::isRestrito)
                ? StatusReserva.AGUARDANDO_APROVACAO : StatusReserva.APROVADA);
        auditoria.registrar(ator, "ATUALIZAR", "RESERVA", reservaId, anterior, resumo(reserva));
        notificacoes.enviar(reserva, reserva.getSolicitante(), "Reserva atualizada: " + reserva.getStatus());
        return response(reserva);
    }

    @Transactional
    public ReservaResponse cancelar(Long reservaId, UsuarioEntity ator) {
        ReservaEntity reserva = obterReserva(reservaId);
        verificarPropriedade(reserva, ator);
        if (reserva.getStatus() == StatusReserva.CANCELADA) return response(reserva);
        String anterior = resumo(reserva);
        reserva.setStatus(StatusReserva.CANCELADA);
        auditoria.registrar(ator, "CANCELAR", "RESERVA", reservaId, anterior, resumo(reserva));
        notificacoes.enviar(reserva, reserva.getSolicitante(), "Reserva cancelada.");
        return response(reserva);
    }

    @Transactional(readOnly = true)
    public List<ReservaResponse> listar(UsuarioEntity usuario, boolean verTodas) {
        List<ReservaEntity> lista = verTodas
                ? reservas.findAllByOrderByInicioDesc()
                : reservas.findBySolicitanteIdOrderByInicioDesc(usuario.getId());
        return lista.stream().map(this::response).toList();
    }

    @Transactional
    public ReservaResponse aprovar(Long id, boolean aprovada, String justificativa, UsuarioEntity responsavel) {
        ReservaEntity reserva = obterReserva(id);
        if (reserva.getStatus() != StatusReserva.AGUARDANDO_APROVACAO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A reserva não está aguardando aprovação.");
        }
        reserva.setStatus(aprovada ? StatusReserva.APROVADA : StatusReserva.RECUSADA);
        aprovacoes.save(new AprovacaoEntity(reserva, responsavel,
                aprovada ? StatusAprovacao.APROVADA : StatusAprovacao.RECUSADA, justificativa));
        auditoria.registrar(responsavel, aprovada ? "APROVAR" : "RECUSAR", "RESERVA", id,
                StatusReserva.AGUARDANDO_APROVACAO.name(), reserva.getStatus().name());
        notificacoes.enviar(reserva, reserva.getSolicitante(),
                "Reserva " + (aprovada ? "aprovada." : "recusada."));
        return response(reserva);
    }

    private List<RecursoEntity> bloquearRecursos(List<Long> ids) {
        if (ids == null || ids.isEmpty() || new HashSet<>(ids).size() != ids.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Informe recursos sem duplicidade.");
        }
        List<Long> ordenados = ids.stream().sorted().toList();
        List<RecursoEntity> encontrados = recursos.bloquearAtivosPorIds(ordenados);
        if (encontrados.size() != ordenados.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Um ou mais recursos não foram encontrados ou estão inativos.");
        }
        return encontrados;
    }

    private void validarDisponibilidade(List<RecursoEntity> alvos, CriarReservaRequest request,
                                        Long reservaIgnorada, Long usuarioId) {
        List<Long> ids = alvos.stream().map(RecursoEntity::getId).toList();
        if (!bloqueios.findSobrepondo(ids, request.inicio(), request.fim()).isEmpty()) {
            registrarConflito(alvos.get(0), usuarioId, request, "Recurso bloqueado para manutenção.");
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Um dos recursos está em manutenção no período solicitado.");
        }
        Map<Long, Integer> pedidos = quantidades(request);
        List<ReservaEntity> existentes = reservas.findAtivasSobrepondo(ids, request.inicio(), request.fim());
        for (RecursoEntity recurso : alvos) {
            int emUso = existentes.stream().filter(r -> !r.getId().equals(reservaIgnorada))
                    .flatMap(r -> r.getRecursos().stream())
                    .filter(rr -> rr.getRecurso().getId().equals(recurso.getId()))
                    .mapToInt(rr -> recurso.getTipo() == TipoRecurso.MATERIAL
                            || recurso.getTipo() == TipoRecurso.EQUIPAMENTO ? rr.getQuantidade() : 1)
                    .sum();
            int solicitada = pedidos.getOrDefault(recurso.getId(), 1);
            boolean conflito = (recurso.getTipo() == TipoRecurso.MATERIAL
                    || recurso.getTipo() == TipoRecurso.EQUIPAMENTO)
                    ? solicitada + emUso > recurso.getQuantidadeTotal() : emUso > 0;
            if (conflito) {
                registrarConflito(recurso, usuarioId, request, "Sobreposição de reserva.");
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "O recurso \"" + recurso.getNome() + "\" já está ocupado nesse horário.");
            }
        }
    }

    private void registrarConflito(RecursoEntity recurso, Long usuarioId,
                                   CriarReservaRequest request, String motivo) {
        conflitos.registrar(recurso.getId(), usuarioId, request.inicio(), request.fim(), motivo);
    }

    private Map<Long, Integer> quantidades(CriarReservaRequest request) {
        Map<Long, Integer> quantidades = request.quantidades() == null ? Map.of() : request.quantidades();
        if (!request.recursoIds().containsAll(quantidades.keySet())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A quantidade informada deve corresponder a um recurso selecionado.");
        }
        return quantidades;
    }

    private ReservaEntity obterReserva(Long id) {
        return reservas.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva não encontrada."));
    }

    private void verificarPropriedade(ReservaEntity reserva, UsuarioEntity ator) {
        boolean privilegiado = ator.getPerfis().stream().anyMatch(p ->
                p.getPerfil().getNome().name().equals("ADMINISTRADOR")
                        || p.getPerfil().getNome().name().equals("RESPONSAVEL"));
        if (!privilegiado && !reserva.getSolicitante().getId().equals(ator.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Você só pode alterar suas próprias reservas.");
        }
    }

    private void validarIntervalo(Instant inicio, Instant fim) {
        if (inicio == null || fim == null || !inicio.isBefore(fim) || !inicio.isAfter(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Informe um período futuro com horário final posterior ao inicial.");
        }
    }

    private String resumo(ReservaEntity reserva) {
        return reserva.getInicio() + "|" + reserva.getFim() + "|" + reserva.getStatus();
    }

    public ReservaResponse response(ReservaEntity reserva) {
        return new ReservaResponse(reserva.getId(), reserva.getSolicitante().getId(),
                reserva.getSolicitante().getNome(), reserva.getInicio(), reserva.getFim(),
                reserva.getFinalidade(), reserva.getStatus(), reserva.getRecursos().stream()
                .map(rr -> new RecursoResponse(rr.getRecurso().getId(), rr.getRecurso().getNome(),
                        rr.getRecurso().getTipo(), rr.getRecurso().getLocalizacao(),
                        rr.getRecurso().getCompetencia(), rr.getRecurso().getCapacidade(),
                        rr.getRecurso().getQuantidadeTotal(), rr.getRecurso().isRestrito(),
                        rr.getRecurso().isAtivo(), rr.getQuantidade()))
                .toList());
    }
}
