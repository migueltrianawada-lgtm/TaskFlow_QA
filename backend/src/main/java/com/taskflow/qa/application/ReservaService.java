package com.taskflow.qa.application;

import com.taskflow.qa.api.dto.CriarReservaRequest;
import com.taskflow.qa.api.dto.ReservaResponse;

public interface ReservaService {

    ReservaResponse criar(CriarReservaRequest request, Long solicitanteId);

    ReservaResponse cancelar(Long reservaId, Long solicitanteId);
}
