package com.taskflow.qa.application;

import com.taskflow.qa.persistence.entity.EventoConflitoEntity;
import com.taskflow.qa.persistence.repository.EventoConflitoRepository;
import com.taskflow.qa.persistence.repository.RecursoRepository;
import com.taskflow.qa.persistence.repository.UsuarioRepository;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistroConflitoService {
    private final EventoConflitoRepository eventos;
    private final RecursoRepository recursos;
    private final UsuarioRepository usuarios;

    public RegistroConflitoService(EventoConflitoRepository eventos, RecursoRepository recursos,
                                   UsuarioRepository usuarios) {
        this.eventos = eventos;
        this.recursos = recursos;
        this.usuarios = usuarios;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrar(Long recursoId, Long usuarioId, Instant inicio, Instant fim, String motivo) {
        eventos.save(new EventoConflitoEntity(recursos.findById(recursoId).orElseThrow(),
                usuarios.findById(usuarioId).orElseThrow(), inicio, fim, motivo));
    }
}
