package com.taskflow.qa.api.dto;

import com.taskflow.qa.domain.TipoRecurso;

public record RecursoResponse(
        Long id,
        String nome,
        TipoRecurso tipo,
        String localizacao,
        String competencia,
        Integer capacidade,
        Integer quantidadeTotal,
        boolean restrito,
        boolean ativo) {
}
