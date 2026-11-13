package com.taskflow.qa.persistence.repository;

import com.taskflow.qa.domain.TipoRecurso;
import com.taskflow.qa.persistence.entity.RecursoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecursoRepository extends JpaRepository<RecursoEntity, Long> {

    List<RecursoEntity> findByTipoAndAtivoTrue(TipoRecurso tipo);

    List<RecursoEntity> findByLocalizacaoContainingIgnoreCaseAndAtivoTrue(String localizacao);

    List<RecursoEntity> findByCompetenciaContainingIgnoreCaseAndAtivoTrue(String competencia);
}
