package com.taskflow.qa.application;

import com.taskflow.qa.persistence.entity.NotificacaoEntity;
import com.taskflow.qa.persistence.entity.ReservaEntity;
import com.taskflow.qa.persistence.entity.UsuarioEntity;
import com.taskflow.qa.persistence.repository.NotificacaoRepository;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoService {
    private final NotificacaoRepository repository;

    public NotificacaoService(NotificacaoRepository repository) {
        this.repository = repository;
    }

    public void enviar(ReservaEntity reserva, UsuarioEntity destinatario, String mensagem) {
        NotificacaoEntity notificacao = new NotificacaoEntity(reserva, destinatario, "SIMULADO", mensagem);
        notificacao.registrarEnvio(Instant.now());
        repository.save(notificacao);
    }
}
