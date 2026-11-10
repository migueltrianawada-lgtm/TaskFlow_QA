package com.taskflow.qa.bdd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.taskflow.qa.domain.IntervaloHorario;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;

public class IntervaloHorarioSteps {

    private List<IntervaloHorario> existentes;
    private IntervaloHorario solicitado;
    private Boolean existeConflito;
    private IllegalArgumentException erro;

    @Dado("que existe uma reserva no intervalo de {string} a {string}")
    public void queExisteUmaReservaNoIntervalo(String inicio, String fim) {
        existentes = List.of(intervalo(inicio, fim));
    }

    @Quando("o usuario solicita o intervalo de {string} a {string}")
    public void oUsuarioSolicitaOIntervalo(String inicio, String fim) {
        try {
            solicitado = intervalo(inicio, fim);
            existeConflito = solicitado.sobrepoe(existentes.getFirst());
        } catch (IllegalArgumentException exception) {
            erro = exception;
        }
    }

    @Entao("o sistema identifica sobreposicao")
    public void oSistemaIdentificaSobreposicao() {
        assertEquals(Boolean.TRUE, existeConflito);
    }

    @Entao("o sistema nao identifica sobreposicao")
    public void oSistemaNaoIdentificaSobreposicao() {
        assertEquals(Boolean.FALSE, existeConflito);
    }

    @Entao("o sistema rejeita o intervalo como invalido")
    public void oSistemaRejeitaOIntervaloComoInvalido() {
        assertEquals(IllegalArgumentException.class, erro.getClass());
    }

    private IntervaloHorario intervalo(String inicio, String fim) {
        var data = "2026-11-09";
        return new IntervaloHorario(
                LocalTime.parse(inicio).atDate(java.time.LocalDate.parse(data)).toInstant(ZoneOffset.UTC),
                LocalTime.parse(fim).atDate(java.time.LocalDate.parse(data)).toInstant(ZoneOffset.UTC));
    }
}
