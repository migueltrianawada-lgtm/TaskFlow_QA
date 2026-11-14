package com.taskflow.qa.persistence.repository;

import com.taskflow.qa.persistence.entity.MovimentacaoMaterialEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentacaoMaterialRepository extends JpaRepository<MovimentacaoMaterialEntity, Long> {

    List<MovimentacaoMaterialEntity> findByReservaIdOrderByRegistradaEm(Long reservaId);

    List<MovimentacaoMaterialEntity> findByReservaIdAndRecursoId(Long reservaId, Long recursoId);
}
