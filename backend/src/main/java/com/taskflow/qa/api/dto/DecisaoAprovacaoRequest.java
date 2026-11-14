package com.taskflow.qa.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DecisaoAprovacaoRequest(@NotNull Boolean aprovada, @Size(max = 1000) String justificativa) {
}
