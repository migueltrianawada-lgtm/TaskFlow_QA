package com.taskflow.qa.api.dto;

import jakarta.validation.constraints.NotNull;

public record AtualizarStatusUsuarioRequest(@NotNull Boolean ativo) {
}
