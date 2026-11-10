package com.taskflow.qa.domain;

import java.time.Instant;
import java.util.Objects;

public record IntervaloHorario(Instant inicio, Instant fim) {

    public IntervaloHorario {
        Objects.requireNonNull(inicio, "inicio é obrigatório");
        Objects.requireNonNull(fim, "fim é obrigatório");
        if (!inicio.isBefore(fim)) {
            throw new IllegalArgumentException("fim deve ser posterior ao início");
        }
    }

    public boolean sobrepoe(IntervaloHorario outro) {
        Objects.requireNonNull(outro, "outro intervalo é obrigatório");
        return inicio.isBefore(outro.fim()) && outro.inicio().isBefore(fim);
    }
}
