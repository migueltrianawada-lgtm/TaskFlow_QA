package com.taskflow.qa.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class IntervaloHorarioTest {

    @Test
    void consideraSobreposicaoQuandoOsIntervalosCompartilhamTempo() {
        var primeiro = new IntervaloHorario(
                Instant.parse("2026-11-09T10:00:00Z"),
                Instant.parse("2026-11-09T11:00:00Z"));
        var segundo = new IntervaloHorario(
                Instant.parse("2026-11-09T10:30:00Z"),
                Instant.parse("2026-11-09T11:30:00Z"));

        assertTrue(primeiro.sobrepoe(segundo));
    }

    @Test
    void consideraIntervalosAdjacentesSemSobreposicao() {
        var primeiro = new IntervaloHorario(
                Instant.parse("2026-11-09T10:00:00Z"),
                Instant.parse("2026-11-09T11:00:00Z"));
        var seguinte = new IntervaloHorario(
                Instant.parse("2026-11-09T11:00:00Z"),
                Instant.parse("2026-11-09T12:00:00Z"));

        assertFalse(primeiro.sobrepoe(seguinte));
    }

    @Test
    void rejeitaIntervaloSemDuracaoPositiva() {
        var instante = Instant.parse("2026-11-09T10:00:00Z");

        assertThrows(IllegalArgumentException.class,
                () -> new IntervaloHorario(instante, instante));
    }
}
