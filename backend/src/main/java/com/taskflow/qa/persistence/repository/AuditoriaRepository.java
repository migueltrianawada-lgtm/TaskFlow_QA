package com.taskflow.qa.persistence.repository;

import com.taskflow.qa.persistence.entity.AuditoriaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditoriaRepository extends JpaRepository<AuditoriaEntity, Long> {

    List<AuditoriaEntity> findByTipoEntidadeAndEntidadeIdOrderByOcorridaEmDesc(
            String tipoEntidade, Long entidadeId);
}
