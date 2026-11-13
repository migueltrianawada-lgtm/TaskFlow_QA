package com.taskflow.qa.api.dto;

import com.taskflow.qa.domain.StatusReserva;
import java.time.Instant;
import java.util.List;

public record ReservaResponse(
        Long id,
        Long solicitanteId,
        Instant inicio,
        Instant fim,
        StatusReserva status,
        List<Long> recursoIds) {
}
