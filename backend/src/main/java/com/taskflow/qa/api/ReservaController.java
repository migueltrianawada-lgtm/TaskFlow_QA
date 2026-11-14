package com.taskflow.qa.api;

import com.taskflow.qa.api.dto.CriarReservaRequest;
import com.taskflow.qa.api.dto.DecisaoAprovacaoRequest;
import com.taskflow.qa.api.dto.ReservaResponse;
import com.taskflow.qa.application.ReservaServiceImpl;
import com.taskflow.qa.application.UsuarioAtualService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {
    private final ReservaServiceImpl service;
    private final UsuarioAtualService usuarioAtual;

    public ReservaController(ReservaServiceImpl service, UsuarioAtualService usuarioAtual) {
        this.service = service;
        this.usuarioAtual = usuarioAtual;
    }

    @GetMapping
    public List<ReservaResponse> listar(@AuthenticationPrincipal Jwt jwt) {
        var usuario = usuarioAtual.obter(jwt);
        boolean verTodas = jwt.getClaimAsStringList("authorities").stream()
                .anyMatch(role -> role.equals("ROLE_RESPONSAVEL") || role.equals("ROLE_ADMINISTRADOR"));
        return service.listar(usuario, verTodas);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservaResponse criar(@Valid @RequestBody CriarReservaRequest request,
                                 @AuthenticationPrincipal Jwt jwt) {
        return service.criar(request, usuarioAtual.obter(jwt));
    }

    @PutMapping("/{id}")
    public ReservaResponse atualizar(@PathVariable Long id, @Valid @RequestBody CriarReservaRequest request,
                                     @AuthenticationPrincipal Jwt jwt) {
        return service.atualizar(id, request, usuarioAtual.obter(jwt));
    }

    @PostMapping("/{id}/cancelamento")
    public ReservaResponse cancelar(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return service.cancelar(id, usuarioAtual.obter(jwt));
    }

    @PostMapping("/{id}/aprovacao")
    @PreAuthorize("hasAnyRole('RESPONSAVEL', 'ADMINISTRADOR')")
    public ReservaResponse decidir(@PathVariable Long id, @Valid @RequestBody DecisaoAprovacaoRequest request,
                                   @AuthenticationPrincipal Jwt jwt) {
        return service.aprovar(id, request.aprovada(), request.justificativa(), usuarioAtual.obter(jwt));
    }
}
