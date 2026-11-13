package com.taskflow.qa.persistence.repository;

import com.taskflow.qa.persistence.entity.ReservaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<ReservaEntity, Long> {

    List<ReservaEntity> findBySolicitanteIdOrderByInicioDesc(Long solicitanteId);
}
