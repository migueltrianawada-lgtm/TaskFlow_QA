package com.taskflow.qa.application;

import com.taskflow.qa.api.dto.CriarReservaRequest;
import com.taskflow.qa.api.dto.ReservaResponse;
import com.taskflow.qa.persistence.entity.UsuarioEntity;
import java.util.List;

public interface ReservaService {

    ReservaResponse criar(CriarReservaRequest request, UsuarioEntity solicitante);

    ReservaResponse atualizar(Long reservaId, CriarReservaRequest request, UsuarioEntity ator);

    ReservaResponse cancelar(Long reservaId, UsuarioEntity ator);

    List<ReservaResponse> listar(UsuarioEntity usuario, boolean verTodas);
}
