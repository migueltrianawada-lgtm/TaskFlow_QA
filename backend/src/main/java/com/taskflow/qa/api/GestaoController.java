package com.taskflow.qa.api;

import com.taskflow.qa.api.dto.AuditoriaResponse;
import com.taskflow.qa.api.dto.AtualizarStatusUsuarioRequest;
import com.taskflow.qa.api.dto.BloqueioRequest;
import com.taskflow.qa.api.dto.CriarUsuarioRequest;
import com.taskflow.qa.api.dto.MovimentacaoResponse;
import com.taskflow.qa.api.dto.MovimentacaoRequest;
import com.taskflow.qa.api.dto.RelatorioResponse;
import com.taskflow.qa.api.dto.UsuarioResponse;
import com.taskflow.qa.application.AuditoriaService;
import com.taskflow.qa.application.NotificacaoService;
import com.taskflow.qa.application.UsuarioAtualService;
import com.taskflow.qa.domain.PerfilAcesso;
import com.taskflow.qa.domain.TipoMovimentacao;
import com.taskflow.qa.persistence.entity.AprovacaoEntity;
import com.taskflow.qa.persistence.entity.AuditoriaEntity;
import com.taskflow.qa.persistence.entity.BloqueioManutencaoEntity;
import com.taskflow.qa.persistence.entity.MovimentacaoMaterialEntity;
import com.taskflow.qa.persistence.entity.PerfilEntity;
import com.taskflow.qa.persistence.entity.RecursoEntity;
import com.taskflow.qa.persistence.entity.ReservaEntity;
import com.taskflow.qa.persistence.entity.UsuarioEntity;
import com.taskflow.qa.persistence.repository.AuditoriaRepository;
import com.taskflow.qa.persistence.repository.BloqueioManutencaoRepository;
import com.taskflow.qa.persistence.repository.EventoConflitoRepository;
import com.taskflow.qa.persistence.repository.MovimentacaoMaterialRepository;
import com.taskflow.qa.persistence.repository.NotificacaoRepository;
import com.taskflow.qa.persistence.repository.PerfilRepository;
import com.taskflow.qa.persistence.repository.RecursoRepository;
import com.taskflow.qa.persistence.repository.ReservaRepository;
import com.taskflow.qa.persistence.repository.UsuarioRepository;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class GestaoController {
    private final UsuarioRepository usuarios;
    private final PerfilRepository perfis;
    private final RecursoRepository recursos;
    private final ReservaRepository reservas;
    private final BloqueioManutencaoRepository bloqueios;
    private final MovimentacaoMaterialRepository movimentacoes;
    private final AuditoriaRepository auditorias;
    private final EventoConflitoRepository conflitos;
    private final NotificacaoRepository notificacoes;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioAtualService usuarioAtual;
    private final AuditoriaService auditoria;

    public GestaoController(UsuarioRepository usuarios, PerfilRepository perfis, RecursoRepository recursos,
                            ReservaRepository reservas, BloqueioManutencaoRepository bloqueios,
                            MovimentacaoMaterialRepository movimentacoes, AuditoriaRepository auditorias,
                            EventoConflitoRepository conflitos, NotificacaoRepository notificacoes,
                            PasswordEncoder passwordEncoder, UsuarioAtualService usuarioAtual,
                            AuditoriaService auditoria) {
        this.usuarios = usuarios;
        this.perfis = perfis;
        this.recursos = recursos;
        this.reservas = reservas;
        this.bloqueios = bloqueios;
        this.movimentacoes = movimentacoes;
        this.auditorias = auditorias;
        this.conflitos = conflitos;
        this.notificacoes = notificacoes;
        this.passwordEncoder = passwordEncoder;
        this.usuarioAtual = usuarioAtual;
        this.auditoria = auditoria;
    }

    @GetMapping("/usuarios")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Transactional(readOnly = true)
    public List<UsuarioResponse> usuarios() {
        return usuarios.findAllByOrderByNomeAsc().stream().map(this::response).toList();
    }

    @PostMapping("/usuarios")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Transactional
    public UsuarioResponse criarUsuario(@Valid @RequestBody CriarUsuarioRequest request,
                                         @AuthenticationPrincipal Jwt jwt) {
        if (usuarios.findByEmailIgnoreCase(request.email()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma conta com esse e-mail.");
        }
        UsuarioEntity usuario = new UsuarioEntity(request.nome(), request.email(),
                passwordEncoder.encode(request.senha()));
        PerfilEntity perfil = perfis.findByNome(request.perfil())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Perfil padrão não foi inicializado."));
        usuario.adicionarPerfil(perfil);
        usuarios.save(usuario);
        auditoria.registrar(usuarioAtual.obter(jwt), "CRIAR", "USUARIO", usuario.getId(), null,
                usuario.getEmail() + "|" + request.perfil());
        return response(usuario);
    }

    @PatchMapping("/usuarios/{id}/status")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Transactional
    public UsuarioResponse atualizarStatusUsuario(@PathVariable Long id,
            @Valid @RequestBody AtualizarStatusUsuarioRequest request,
            @AuthenticationPrincipal Jwt jwt) {
        UsuarioEntity alvo = usuarios.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
        UsuarioEntity ator = usuarioAtual.obter(jwt);
        if (alvo.getId().equals(ator.getId()) && !request.ativo()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é permitido desativar a própria conta.");
        }
        boolean anterior = alvo.isAtivo();
        alvo.setAtivo(request.ativo());
        auditoria.registrar(ator, request.ativo() ? "ATIVAR" : "DESATIVAR", "USUARIO", id,
                Boolean.toString(anterior), Boolean.toString(request.ativo()));
        return response(alvo);
    }

    @GetMapping("/manutencoes")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'RESPONSAVEL')")
    @Transactional(readOnly = true)
    public List<Map<String, Object>> manutencoes(@RequestParam(required = false) Long recursoId) {
        List<BloqueioManutencaoEntity> lista = recursoId == null
                ? bloqueios.findAll() : bloqueios.findByRecursoIdOrderByInicio(recursoId);
        return lista.stream().map(b -> Map.<String, Object>of(
                "id", b.getId(), "recursoId", b.getRecurso().getId(), "recurso", b.getRecurso().getNome(),
                "inicio", b.getInicio(), "fim", b.getFim(), "justificativa", b.getJustificativa())).toList();
    }

    @PostMapping("/manutencoes")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Transactional
    public Map<String, Object> criarManutencao(@Valid @RequestBody BloqueioRequest request,
                                               @AuthenticationPrincipal Jwt jwt) {
        validarPeriodo(request.inicio(), request.fim());
        RecursoEntity recurso = recursos.bloquearAtivosPorIds(List.of(request.recursoId())).stream().findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recurso não encontrado."));
        List<ReservaEntity> conflicts = reservas.findAtivasSobrepondo(
                List.of(recurso.getId()), request.inicio(), request.fim());
        if (!conflicts.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Não é possível bloquear um recurso que já possui reservas no período.");
        }
        UsuarioEntity ator = usuarioAtual.obter(jwt);
        BloqueioManutencaoEntity bloqueio = bloqueios.save(new BloqueioManutencaoEntity(
                recurso, ator, request.inicio(), request.fim(), request.justificativa()));
        auditoria.registrar(ator, "CRIAR_BLOQUEIO", "MANUTENCAO", bloqueio.getId(), null,
                recurso.getId() + "|" + request.inicio() + "|" + request.fim());
        return Map.of("id", bloqueio.getId(), "recurso", recurso.getNome(),
                "inicio", bloqueio.getInicio(), "fim", bloqueio.getFim());
    }

    @PostMapping("/reservas/{reservaId}/movimentacoes")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('RESPONSAVEL', 'ADMINISTRADOR')")
    @Transactional
    public Map<String, Object> movimentar(@PathVariable Long reservaId,
                                          @Valid @RequestBody MovimentacaoRequest request,
                                          @AuthenticationPrincipal Jwt jwt) {
        ReservaEntity reserva = reservas.findById(reservaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva não encontrada."));
        RecursoEntity recurso = recursos.bloquearAtivosPorIds(List.of(request.recursoId())).stream().findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recurso não encontrado."));
        if (reserva.getStatus() != com.taskflow.qa.domain.StatusReserva.APROVADA) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Só é possível movimentar materiais de uma reserva aprovada.");
        }
        int reservado = reserva.getRecursos().stream()
                .filter(rr -> rr.getRecurso().getId().equals(recurso.getId()))
                .mapToInt(com.taskflow.qa.persistence.entity.ReservaRecursoEntity::getQuantidade)
                .findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "O material não pertence a esta reserva."));
        if (recurso.getTipo() != com.taskflow.qa.domain.TipoRecurso.MATERIAL
                && recurso.getTipo() != com.taskflow.qa.domain.TipoRecurso.EQUIPAMENTO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Retirada e devolução só se aplicam a materiais e equipamentos.");
        }
        List<MovimentacaoMaterialEntity> anteriores =
                movimentacoes.findByReservaIdAndRecursoId(reservaId, recurso.getId());
        int retirado = anteriores.stream().filter(m -> m.getTipo() == TipoMovimentacao.RETIRADA)
                .mapToInt(MovimentacaoMaterialEntity::getQuantidade).sum();
        int devolvido = anteriores.stream().filter(m -> m.getTipo() == TipoMovimentacao.DEVOLUCAO)
                .mapToInt(MovimentacaoMaterialEntity::getQuantidade).sum();
        int quantidade = request.quantidade();
        if (request.tipo() == TipoMovimentacao.RETIRADA && quantidade + retirado > reservado) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A quantidade retirada excederia o total reservado.");
        }
        if (request.tipo() == TipoMovimentacao.DEVOLUCAO && quantidade + devolvido > retirado) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A devolução não pode exceder a quantidade retirada.");
        }
        UsuarioEntity ator = usuarioAtual.obter(jwt);
        MovimentacaoMaterialEntity mov = movimentacoes.save(new MovimentacaoMaterialEntity(
                reserva, recurso, ator, request.tipo(), quantidade));
        auditoria.registrar(ator, request.tipo().name(), "MOVIMENTACAO", mov.getId(), null,
                reservaId + "|" + recurso.getId() + "|" + quantidade);
        return Map.of("id", mov.getId(), "reservaId", reservaId, "recursoId", recurso.getId(),
                "tipo", mov.getTipo(), "quantidade", mov.getQuantidade(), "registradaEm", mov.getRegistradaEm());
    }

    @GetMapping("/reservas/{reservaId}/movimentacoes")
    @Transactional(readOnly = true)
    public List<MovimentacaoResponse> movimentacoes(@PathVariable Long reservaId,
                                                     @AuthenticationPrincipal Jwt jwt) {
        ReservaEntity reserva = reservas.findById(reservaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva não encontrada."));
        UsuarioEntity ator = usuarioAtual.obter(jwt);
        boolean privilegiado = ator.getPerfis().stream().anyMatch(p ->
                p.getPerfil().getNome() == PerfilAcesso.RESPONSAVEL
                        || p.getPerfil().getNome() == PerfilAcesso.ADMINISTRADOR);
        if (!privilegiado && !reserva.getSolicitante().getId().equals(ator.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Você não pode consultar as movimentações desta reserva.");
        }
        return movimentacoes.findByReservaIdOrderByRegistradaEm(reservaId).stream()
                .map(m -> new MovimentacaoResponse(m.getId(), reservaId, m.getRecurso().getId(),
                        m.getRecurso().getNome(), m.getResponsavel().getNome(),
                        m.getTipo(), m.getQuantidade(), m.getRegistradaEm()))
                .toList();
    }

    @GetMapping("/auditorias")
    @PreAuthorize("hasAnyRole('RESPONSAVEL', 'ADMINISTRADOR')")
    @Transactional(readOnly = true)
    public List<AuditoriaResponse> auditorias() {
        return auditorias.findTop100ByOrderByOcorridaEmDesc().stream().map(this::auditoriaResponse).toList();
    }

    @GetMapping("/notificacoes")
    @Transactional(readOnly = true)
    public List<Map<String, Object>> notificacoes(@AuthenticationPrincipal Jwt jwt) {
        UsuarioEntity usuario = usuarioAtual.obter(jwt);
        return notificacoes.findByDestinatarioIdOrderByCriadaEmDesc(usuario.getId()).stream()
                .<Map<String, Object>>map(n -> Map.of("id", n.getId(), "mensagem", n.getMensagem(),
                        "status", n.getStatus(), "criadaEm", n.getCriadaEm()))
                .toList();
    }

    @GetMapping("/relatorios/utilizacao")
    @PreAuthorize("hasAnyRole('RESPONSAVEL', 'ADMINISTRADOR')")
    @Transactional(readOnly = true)
    public RelatorioResponse relatorio(@RequestParam Instant inicio, @RequestParam Instant fim) {
        validarPeriodo(inicio, fim);
        List<ReservaEntity> noPeriodo = reservas.findForUtilizationReport(
                List.of(com.taskflow.qa.domain.StatusReserva.APROVADA,
                        com.taskflow.qa.domain.StatusReserva.AGUARDANDO_APROVACAO), inicio, fim);
        Map<Long, Long> segundosPorRecurso = new HashMap<>();
        Map<Long, Long> reservasPorRecurso = new HashMap<>();
        Map<Long, String> nomesPorRecurso = new HashMap<>();
        for (ReservaEntity reserva : noPeriodo) {
            long segundos = Duration.between(
                    reserva.getInicio().isBefore(inicio) ? inicio : reserva.getInicio(),
                    reserva.getFim().isAfter(fim) ? fim : reserva.getFim()).toSeconds();
            reserva.getRecursos().forEach(item -> {
                Long recursoId = item.getRecurso().getId();
                nomesPorRecurso.put(recursoId, item.getRecurso().getNome());
                segundosPorRecurso.merge(recursoId, segundos, Long::sum);
                reservasPorRecurso.merge(recursoId, 1L, Long::sum);
            });
        }
        List<RelatorioResponse.UtilizacaoRecurso> porRecurso = new ArrayList<>();
        segundosPorRecurso.forEach((recursoId, segundos) -> porRecurso.add(
                new RelatorioResponse.UtilizacaoRecurso(recursoId, nomesPorRecurso.get(recursoId),
                        BigDecimal.valueOf(segundos).divide(BigDecimal.valueOf(3600), 2, RoundingMode.HALF_UP),
                        reservasPorRecurso.get(recursoId))));
        porRecurso.sort(Comparator.comparing(RelatorioResponse.UtilizacaoRecurso::nome));
        long segundosTotais = porRecurso.stream()
                .map(RelatorioResponse.UtilizacaoRecurso::horas)
                .map(h -> h.multiply(BigDecimal.valueOf(3600)))
                .mapToLong(BigDecimal::longValue).sum();
        return new RelatorioResponse(noPeriodo.size(),
                BigDecimal.valueOf(segundosTotais).divide(BigDecimal.valueOf(3600), 2, RoundingMode.HALF_UP),
                porRecurso.size(), conflitos.count(), porRecurso);
    }

    private UsuarioResponse response(UsuarioEntity usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(),
                usuario.getPerfis().stream().map(item -> item.getPerfil().getNome()).distinct().toList(),
                usuario.isAtivo());
    }

    private AuditoriaResponse auditoriaResponse(AuditoriaEntity item) {
        return new AuditoriaResponse(item.getId(), item.getUsuario().getNome(), item.getAcao(),
                item.getTipoEntidade(), item.getEntidadeId(), item.getDadosAnteriores(),
                item.getDadosNovos(), item.getOcorridaEm());
    }

    private void validarPeriodo(Instant inicio, Instant fim) {
        if (inicio == null || fim == null || !inicio.isBefore(fim)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O fim do período deve ser posterior ao início.");
        }
    }
}
