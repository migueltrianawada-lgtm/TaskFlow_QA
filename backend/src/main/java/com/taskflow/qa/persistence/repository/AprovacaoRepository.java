package com.taskflow.qa.persistence.repository;

import com.taskflow.qa.persistence.entity.AprovacaoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AprovacaoRepository extends JpaRepository<AprovacaoEntity, Long> {

    List<AprovacaoEntity> findByResponsavelIdOrderByDecididaEmDesc(Long responsavelId);
}
