package com.taskflow.qa.api.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public record CriarReservaRequest(
        @NotNull Instant inicio,
        @NotNull Instant fim,
        @NotBlank @Size(max = 500) String finalidade,
        @NotEmpty List<@NotNull @Positive Long> recursoIds,
        Map<@Positive Long, @Positive Integer> quantidades) {
}
