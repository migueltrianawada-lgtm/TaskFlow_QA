package com.taskflow.qa.api.dto;

import com.taskflow.qa.domain.TipoMovimentacao;
import java.time.Instant;

public record MovimentacaoResponse(
        Long id, Long reservaId, Long recursoId, String recurso, String responsavel,
        TipoMovimentacao tipo, int quantidade, Instant registradaEm) {
}
