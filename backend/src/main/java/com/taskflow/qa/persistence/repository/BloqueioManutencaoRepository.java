package com.taskflow.qa.persistence.repository;

import com.taskflow.qa.persistence.entity.BloqueioManutencaoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BloqueioManutencaoRepository extends JpaRepository<BloqueioManutencaoEntity, Long> {

    List<BloqueioManutencaoEntity> findByRecursoIdOrderByInicio(Long recursoId);
}
