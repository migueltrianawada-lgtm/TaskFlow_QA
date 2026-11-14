package com.taskflow.qa.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.Instant;

public record BloqueioRequest(
        @NotNull @Positive Long recursoId,
        @NotNull Instant inicio,
        @NotNull Instant fim,
        @NotBlank @Size(max = 500) String justificativa) {
}
