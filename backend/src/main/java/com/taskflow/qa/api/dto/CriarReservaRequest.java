package com.taskflow.qa.api.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.Instant;
import java.util.List;

public record CriarReservaRequest(
        @NotNull Instant inicio,
        @NotNull Instant fim,
        @NotEmpty List<@NotNull @Positive Long> recursoIds) {
}
