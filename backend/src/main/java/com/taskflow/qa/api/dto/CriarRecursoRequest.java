package com.taskflow.qa.api.dto;

import com.taskflow.qa.domain.TipoRecurso;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CriarRecursoRequest(
        @NotBlank @Size(max = 160) String nome,
        @NotNull TipoRecurso tipo,
        @Size(max = 160) String localizacao,
        @Size(max = 240) String competencia,
        @Positive Integer capacidade,
        @PositiveOrZero Integer quantidadeTotal,
        boolean restrito,
        Long docenteUsuarioId) {
}
