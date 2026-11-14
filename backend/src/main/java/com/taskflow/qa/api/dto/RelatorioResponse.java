package com.taskflow.qa.api.dto;

import java.math.BigDecimal;
import java.util.List;

public record RelatorioResponse(
        long reservas,
        BigDecimal horasAlocadas,
        long recursosUtilizados,
        long conflitosEvitados,
        List<UtilizacaoRecurso> utilizacaoPorRecurso) {
    public record UtilizacaoRecurso(Long recursoId, String nome, BigDecimal horas, long reservas) {
    }
}
