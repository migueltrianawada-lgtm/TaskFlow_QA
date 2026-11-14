package com.taskflow.qa.persistence.repository;

import com.taskflow.qa.persistence.entity.BloqueioManutencaoEntity;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BloqueioManutencaoRepository extends JpaRepository<BloqueioManutencaoEntity, Long> {

    List<BloqueioManutencaoEntity> findByRecursoIdOrderByInicio(Long recursoId);

    @Query("select b from BloqueioManutencaoEntity b where b.recurso.id in :ids "
            + "and b.inicio < :fim and b.fim > :inicio")
    List<BloqueioManutencaoEntity> findSobrepondo(@Param("ids") List<Long> ids,
                                                  @Param("inicio") Instant inicio,
                                                  @Param("fim") Instant fim);
}
