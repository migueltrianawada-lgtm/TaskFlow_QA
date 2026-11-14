package com.taskflow.qa.persistence.repository;

import com.taskflow.qa.persistence.entity.NotificacaoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacaoRepository extends JpaRepository<NotificacaoEntity, Long> {

    List<NotificacaoEntity> findByReservaIdOrderByCriadaEmDesc(Long reservaId);

    List<NotificacaoEntity> findByDestinatarioIdOrderByCriadaEmDesc(Long destinatarioId);
}
