package com.taskflow.qa.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.taskflow.qa.domain.TipoRecurso;
import com.taskflow.qa.persistence.entity.PerfilEntity;
import com.taskflow.qa.persistence.entity.RecursoEntity;
import com.taskflow.qa.persistence.entity.UsuarioEntity;
import com.taskflow.qa.persistence.repository.PerfilRepository;
import com.taskflow.qa.persistence.repository.RecursoRepository;
import com.taskflow.qa.persistence.repository.UsuarioRepository;
import com.taskflow.qa.domain.PerfilAcesso;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class RecursoRepositoryTest {

    @Autowired
    private RecursoRepository recursoRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void persistsAndQueriesResourcesByType() {
        recursoRepository.save(new RecursoEntity(
                "Sala 12", TipoRecurso.SALA, "Bloco A", null, 30, null, false));

        assertThat(recursoRepository.findByTipoAndAtivoTrue(TipoRecurso.SALA))
                .extracting(RecursoEntity::getNome)
                .containsExactly("Sala 12");
    }

    @Test
    void persistsUserProfilesThroughTheDocumentedJoinEntity() {
        var perfil = perfilRepository.save(new PerfilEntity(PerfilAcesso.ADMINISTRADOR));
        var usuario = new UsuarioEntity("Ana", "ANA@example.com", "bcrypt-hash");
        usuario.adicionarPerfil(perfil);

        var salvo = usuarioRepository.saveAndFlush(usuario);

        assertThat(salvo.getEmail()).isEqualTo("ana@example.com");
        assertThat(salvo.getPerfis()).hasSize(1);
        assertThat(salvo.getPerfis().getFirst().getPerfil().getNome())
                .isEqualTo(PerfilAcesso.ADMINISTRADOR);
    }

    @Test
    void rejectsResourceAttributesThatDoNotMatchTheirType() {
        var salaComEstoque = new RecursoEntity(
                "Sala 14", TipoRecurso.SALA, "Bloco A", null, 30, 2, false);

        assertThatThrownBy(() -> recursoRepository.saveAndFlush(salaComEstoque))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
