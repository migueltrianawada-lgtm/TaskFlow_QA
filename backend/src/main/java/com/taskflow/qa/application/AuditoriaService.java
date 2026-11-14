package com.taskflow.qa.application;

import com.taskflow.qa.persistence.entity.AuditoriaEntity;
import com.taskflow.qa.persistence.entity.UsuarioEntity;
import com.taskflow.qa.persistence.repository.AuditoriaRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaService {
    private final AuditoriaRepository repository;

    public AuditoriaService(AuditoriaRepository repository) {
        this.repository = repository;
    }

    public void registrar(UsuarioEntity usuario, String acao, String tipo, Long id, String antes, String depois) {
        repository.save(new AuditoriaEntity(usuario, acao, tipo, id, antes, depois));
    }
}
