package com.taskflow.qa.api;

import com.taskflow.qa.api.dto.CriarRecursoRequest;
import com.taskflow.qa.api.dto.RecursoResponse;
import com.taskflow.qa.application.RecursoServiceImpl;
import com.taskflow.qa.application.UsuarioAtualService;
import com.taskflow.qa.domain.TipoRecurso;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recursos")
public class RecursoController {
    private final RecursoServiceImpl service;
    private final UsuarioAtualService usuarioAtual;

    public RecursoController(RecursoServiceImpl service, UsuarioAtualService usuarioAtual) {
        this.service = service;
        this.usuarioAtual = usuarioAtual;
    }

    @GetMapping
    public List<RecursoResponse> consultar(
            @RequestParam(required = false) TipoRecurso tipo,
            @RequestParam(required = false) String localizacao,
            @RequestParam(required = false) String competencia,
            @RequestParam(required = false) Integer capacidadeMinima,
            @RequestParam(required = false) Instant inicio,
            @RequestParam(required = false) Instant fim) {
        return service.consultar(tipo, localizacao, competencia, capacidadeMinima, inicio, fim);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public RecursoResponse criar(@Valid @RequestBody CriarRecursoRequest request,
                                 @AuthenticationPrincipal Jwt jwt) {
        return service.criar(request, usuarioAtual.obter(jwt));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public RecursoResponse atualizar(@PathVariable Long id, @Valid @RequestBody CriarRecursoRequest request,
                                     @AuthenticationPrincipal Jwt jwt) {
        return service.atualizar(id, request, usuarioAtual.obter(jwt));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void desativar(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        service.desativar(id, usuarioAtual.obter(jwt));
    }
}
