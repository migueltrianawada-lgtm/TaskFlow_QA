package com.taskflow.qa.api;

import com.taskflow.qa.api.dto.LoginRequest;
import com.taskflow.qa.api.dto.TokenResponse;
import com.taskflow.qa.api.dto.UsuarioResponse;
import com.taskflow.qa.persistence.entity.UsuarioEntity;
import com.taskflow.qa.persistence.repository.UsuarioRepository;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final long TOKEN_TTL_SECONDS = 900;

    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final UsuarioRepository usuarioRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder,
                          UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    @Transactional(readOnly = true)
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.senha()));
        } catch (AuthenticationException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Não foi possível autenticar com essas credenciais.");
        }

        UsuarioEntity usuario = usuarioRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas."));
        List<String> authorities = usuario.getPerfis().stream()
                .map(item -> "ROLE_" + item.getPerfil().getNome().name())
                .distinct()
                .toList();
        Instant issuedAt = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("taskflow")
                .issuedAt(issuedAt)
                .expiresAt(issuedAt.plusSeconds(TOKEN_TTL_SECONDS))
                .subject(usuario.getEmail())
                .claim("userId", usuario.getId())
                .claim("name", usuario.getNome())
                .claim("authorities", authorities)
                .build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();
        return new TokenResponse(token, "Bearer", TOKEN_TTL_SECONDS, toResponse(usuario));
    }

    @GetMapping("/me")
    @Transactional(readOnly = true)
    public UsuarioResponse currentUser(@AuthenticationPrincipal Jwt jwt) {
        Long userId = jwt.getClaim("userId");
        UsuarioEntity usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "A sessão não é mais válida."));
        if (!usuario.isAtivo()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "A conta está inativa.");
        }
        return toResponse(usuario);
    }

    private UsuarioResponse toResponse(UsuarioEntity usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(),
                usuario.getPerfis().stream().map(item -> item.getPerfil().getNome()).distinct().toList(),
                usuario.isAtivo());
    }
}
