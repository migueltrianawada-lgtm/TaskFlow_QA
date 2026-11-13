package com.taskflow.qa.persistence;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.taskflow.qa.domain.TipoRecurso;
import com.taskflow.qa.persistence.entity.BloqueioManutencaoEntity;
import com.taskflow.qa.persistence.entity.RecursoEntity;
import com.taskflow.qa.persistence.entity.ReservaEntity;
import com.taskflow.qa.persistence.entity.UsuarioEntity;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class IntervaloEntidadeTest {

    private static final Instant INICIO = Instant.parse("2026-11-13T10:00:00Z");

    @Test
    void rejectsReservationWithInvalidInterval() {
        var usuario = new UsuarioEntity("Ana", "ana@example.com", "bcrypt-hash");

        assertThatThrownBy(() -> new ReservaEntity(usuario, INICIO, INICIO, "Aula"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("fim deve ser posterior");
    }

    @Test
    void rejectsMaintenanceBlockWithInvalidInterval() {
        var usuario = new UsuarioEntity("Admin", "admin@example.com", "bcrypt-hash");
        var recurso = new RecursoEntity("Sala 12", TipoRecurso.SALA, "Bloco A", null, 30, null, false);

        assertThatThrownBy(() -> new BloqueioManutencaoEntity(
                recurso, usuario, INICIO, INICIO, "Manutenção preventiva"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("fim deve ser posterior");
    }
}
