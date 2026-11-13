package com.taskflow.qa.persistence.repository;

import com.taskflow.qa.persistence.entity.EventoConflitoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoConflitoRepository extends JpaRepository<EventoConflitoEntity, Long> {

    List<EventoConflitoEntity> findByRecursoIdOrderByRegistradoEmDesc(Long recursoId);
}
