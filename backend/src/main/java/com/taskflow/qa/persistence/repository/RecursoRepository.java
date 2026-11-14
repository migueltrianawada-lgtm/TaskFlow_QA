package com.taskflow.qa.persistence.repository;

import com.taskflow.qa.domain.TipoRecurso;
import com.taskflow.qa.persistence.entity.RecursoEntity;
import java.util.List;
import java.util.Collection;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecursoRepository extends JpaRepository<RecursoEntity, Long> {

    List<RecursoEntity> findByTipoAndAtivoTrue(TipoRecurso tipo);

    List<RecursoEntity> findByLocalizacaoContainingIgnoreCaseAndAtivoTrue(String localizacao);

    List<RecursoEntity> findByCompetenciaContainingIgnoreCaseAndAtivoTrue(String competencia);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from RecursoEntity r where r.id in :ids and r.ativo = true order by r.id")
    List<RecursoEntity> bloquearAtivosPorIds(@Param("ids") Collection<Long> ids);

    Optional<RecursoEntity> findByIdAndAtivoTrue(Long id);

    List<RecursoEntity> findByAtivoTrueOrderByNomeAsc();
}
