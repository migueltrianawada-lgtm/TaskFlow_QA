package com.taskflow.qa.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class VerificadorDeConflitoTest {

    private final VerificadorDeConflito verificador = new VerificadorDeConflito();

    @Test
    void detectaConflitoComIntervaloExistente() {
        var solicitado = intervalo("10:30:00", "11:30:00");
        var existente = intervalo("10:00:00", "11:00:00");

        assertTrue(verificador.existeConflito(solicitado, List.of(existente)));
    }

    @Test
    void naoDetectaConflitoComIntervaloAdjacente() {
        var solicitado = intervalo("11:00:00", "12:00:00");
        var existente = intervalo("10:00:00", "11:00:00");

        assertFalse(verificador.existeConflito(solicitado, List.of(existente)));
    }

    @Test
    void naoDetectaConflitoQuandoNaoHaReservasExistentes() {
        assertFalse(verificador.existeConflito(intervalo("10:00:00", "11:00:00"), List.of()));
    }

    private IntervaloHorario intervalo(String inicio, String fim) {
        var data = "2026-11-09T";
        return new IntervaloHorario(Instant.parse(data + inicio + "Z"), Instant.parse(data + fim + "Z"));
    }
}
