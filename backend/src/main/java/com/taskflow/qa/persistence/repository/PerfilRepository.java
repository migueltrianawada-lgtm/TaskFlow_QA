package com.taskflow.qa.persistence.repository;

import com.taskflow.qa.persistence.entity.PerfilEntity;
import com.taskflow.qa.domain.PerfilAcesso;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerfilRepository extends JpaRepository<PerfilEntity, Long> {

    Optional<PerfilEntity> findByNome(PerfilAcesso nome);
}
