package com.taskflow.qa.persistence.repository;

import com.taskflow.qa.domain.StatusReserva;
import com.taskflow.qa.persistence.entity.ReservaEntity;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservaRepository extends JpaRepository<ReservaEntity, Long> {

    List<ReservaEntity> findBySolicitanteIdOrderByInicioDesc(Long solicitanteId);

    List<ReservaEntity> findAllByOrderByInicioDesc();

    @Query("""
            select distinct r from ReservaEntity r join r.recursos rr
            where rr.recurso.id in :recursoIds
              and r.status in (com.taskflow.qa.domain.StatusReserva.PENDENTE,
                               com.taskflow.qa.domain.StatusReserva.AGUARDANDO_APROVACAO,
                               com.taskflow.qa.domain.StatusReserva.APROVADA)
              and r.inicio < :fim and r.fim > :inicio
            """)
    List<ReservaEntity> findAtivasSobrepondo(@Param("recursoIds") List<Long> recursoIds,
                                             @Param("inicio") Instant inicio, @Param("fim") Instant fim);

    @EntityGraph(attributePaths = {"recursos", "recursos.recurso"})
    @Query("""
            select distinct r from ReservaEntity r
            where r.status in :statuses
              and r.inicio < :fim and r.fim > :inicio
            """)
    List<ReservaEntity> findForUtilizationReport(@Param("statuses") List<StatusReserva> statuses,
                                                 @Param("inicio") Instant inicio,
                                                 @Param("fim") Instant fim);
}
