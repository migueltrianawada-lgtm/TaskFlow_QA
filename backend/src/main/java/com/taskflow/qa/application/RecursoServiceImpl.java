package com.taskflow.qa.application;

import com.taskflow.qa.api.dto.CriarRecursoRequest;
import com.taskflow.qa.api.dto.RecursoResponse;
import com.taskflow.qa.domain.TipoRecurso;
import com.taskflow.qa.persistence.entity.RecursoEntity;
import com.taskflow.qa.persistence.entity.UsuarioEntity;
import com.taskflow.qa.persistence.repository.BloqueioManutencaoRepository;
import com.taskflow.qa.persistence.repository.RecursoRepository;
import com.taskflow.qa.persistence.repository.ReservaRepository;
import com.taskflow.qa.persistence.repository.UsuarioRepository;
import java.time.Instant;
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
public class RecursoServiceImpl implements RecursoService {
    private final RecursoRepository recursos;
    private final UsuarioRepository usuarios;
    private final ReservaRepository reservas;
    private final BloqueioManutencaoRepository bloqueios;
    private final AuditoriaService auditoria;

    public RecursoServiceImpl(RecursoRepository recursos, UsuarioRepository usuarios,
                              ReservaRepository reservas, BloqueioManutencaoRepository bloqueios,
                              AuditoriaService auditoria) {
        this.recursos = recursos;
        this.usuarios = usuarios;
        this.reservas = reservas;
        this.bloqueios = bloqueios;
        this.auditoria = auditoria;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecursoResponse> consultar(TipoRecurso tipo, String localizacao, String competencia) {
        return consultar(tipo, localizacao, competencia, null, null, null);
    }

    @Transactional(readOnly = true)
    public List<RecursoResponse> consultar(TipoRecurso tipo, String localizacao, String competencia,
                                           Integer capacidadeMinima, Instant inicio, Instant fim) {
        if ((inicio == null) != (fim == null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Informe início e fim para filtrar disponibilidade.");
        }
        List<RecursoEntity> candidatos = recursos.findByAtivoTrueOrderByNomeAsc().stream()
                .filter(r -> tipo == null || r.getTipo() == tipo)
                .filter(r -> localizacao == null || contem(r.getLocalizacao(), localizacao))
                .filter(r -> competencia == null || contem(r.getCompetencia(), competencia))
                .filter(r -> capacidadeMinima == null || (r.getCapacidade() != null
                        && r.getCapacidade() >= capacidadeMinima))
                .toList();
        if (inicio == null) return candidatos.stream().map(this::response).toList();
        if (!inicio.isBefore(fim)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O fim deve ser posterior ao início.");
        }
        List<Long> ids = candidatos.stream().map(RecursoEntity::getId).toList();
        if (ids.isEmpty()) return List.of();
        Set<Long> emManutencao = new HashSet<>(bloqueios.findSobrepondo(ids, inicio, fim).stream()
                .map(b -> b.getRecurso().getId()).toList());
        Map<Long, Integer> quantidadesReservadas = new HashMap<>();
        reservas.findAtivasSobrepondo(ids, inicio, fim).stream()
                .flatMap(r -> r.getRecursos().stream())
                .forEach(rr -> quantidadesReservadas.merge(rr.getRecurso().getId(),
                        rr.getRecurso().getTipo() == TipoRecurso.MATERIAL
                                || rr.getRecurso().getTipo() == TipoRecurso.EQUIPAMENTO
                                ? rr.getQuantidade() : 1, Integer::sum));
        return candidatos.stream()
                .filter(r -> !emManutencao.contains(r.getId()))
                .filter(r -> r.getTipo() == TipoRecurso.MATERIAL || r.getTipo() == TipoRecurso.EQUIPAMENTO
                        ? r.getQuantidadeTotal() - quantidadesReservadas.getOrDefault(r.getId(), 0) > 0
                        : !quantidadesReservadas.containsKey(r.getId()))
                .map(this::response)
                .toList();
    }

    @Transactional
    public RecursoResponse criar(CriarRecursoRequest request, UsuarioEntity autor) {
        UsuarioEntity docente = validarDocente(request, null);
        validarTipo(request, docente);
        RecursoEntity recurso = new RecursoEntity(request.nome(), request.tipo(), request.localizacao(),
                request.competencia(), request.capacidade(), request.quantidadeTotal(), request.restrito());
        recurso.setDocente(docente);
        recursos.save(recurso);
        auditoria.registrar(autor, "CRIAR", "RECURSO", recurso.getId(), null, recurso.getNome());
        return response(recurso);
    }

    @Transactional
    public RecursoResponse atualizar(Long id, CriarRecursoRequest request, UsuarioEntity autor) {
        RecursoEntity recurso = recursos.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recurso não encontrado."));
        UsuarioEntity docente = validarDocente(request, recurso);
        validarTipo(request, docente);
        String anterior = recurso.getNome() + "|" + recurso.getTipo();
        recurso.atualizar(request.nome(), request.tipo(), request.localizacao(), request.competencia(),
                request.capacidade(), request.quantidadeTotal(), request.restrito(), docente);
        auditoria.registrar(autor, "ATUALIZAR", "RECURSO", id, anterior,
                recurso.getNome() + "|" + recurso.getTipo());
        return response(recurso);
    }

    @Transactional
    public void desativar(Long id, UsuarioEntity autor) {
        RecursoEntity recurso = recursos.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recurso não encontrado."));
        recurso.setAtivo(false);
        auditoria.registrar(autor, "DESATIVAR", "RECURSO", id, recurso.getNome(), null);
    }

    private UsuarioEntity validarDocente(CriarRecursoRequest request, RecursoEntity existente) {
        if (request.tipo() == TipoRecurso.PROFESSOR) {
            Long docenteId = request.docenteUsuarioId();
            if (docenteId == null && existente != null && existente.getDocente() != null) {
                docenteId = existente.getDocente().getId();
            }
            if (docenteId == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Selecione o usuário vinculado ao recurso do tipo professor.");
            }
            return usuarios.findById(docenteId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Usuário docente não encontrado."));
        }
        if (request.docenteUsuarioId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O vínculo com docente só é permitido para recursos do tipo professor.");
        }
        return null;
    }

    private void validarTipo(CriarRecursoRequest request, UsuarioEntity docente) {
        boolean valido = switch (request.tipo()) {
            case SALA -> request.capacidade() != null && request.quantidadeTotal() == null && docente == null;
            case PROFESSOR -> request.competencia() != null && !request.competencia().isBlank()
                    && request.capacidade() == null && request.quantidadeTotal() == null && docente != null;
            case MATERIAL, EQUIPAMENTO -> request.quantidadeTotal() != null
                    && request.capacidade() == null && docente == null;
        };
        if (!valido) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Informe os atributos compatíveis com o tipo de recurso.");
        }
    }

    private boolean contem(String value, String query) {
        return value != null && value.toLowerCase(java.util.Locale.ROOT)
                .contains(query.toLowerCase(java.util.Locale.ROOT));
    }

    public RecursoResponse response(RecursoEntity recurso) {
        return new RecursoResponse(recurso.getId(), recurso.getNome(), recurso.getTipo(),
                recurso.getLocalizacao(), recurso.getCompetencia(), recurso.getCapacidade(),
                recurso.getQuantidadeTotal(), recurso.isRestrito(), recurso.isAtivo(), null);
    }
}
