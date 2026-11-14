package com.taskflow.qa.api.dto;

import com.taskflow.qa.domain.TipoMovimentacao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MovimentacaoRequest(
        @NotNull @Positive Long recursoId,
        @NotNull TipoMovimentacao tipo,
        @Positive int quantidade) {
}
