package com.taskflow.qa.application;

import com.taskflow.qa.persistence.entity.UsuarioEntity;
import com.taskflow.qa.persistence.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioAtualService {
    private final UsuarioRepository repository;

    public UsuarioAtualService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public UsuarioEntity obter(Jwt jwt) {
        return repository.findByEmailIgnoreCase(jwt.getSubject())
                .filter(UsuarioEntity::isAtivo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "A conta não está mais ativa."));
    }
}
