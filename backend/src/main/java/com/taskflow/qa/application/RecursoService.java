package com.taskflow.qa.application;

import com.taskflow.qa.api.dto.RecursoResponse;
import com.taskflow.qa.domain.TipoRecurso;
import java.util.List;

public interface RecursoService {

    List<RecursoResponse> consultar(TipoRecurso tipo, String localizacao, String competencia);
}
