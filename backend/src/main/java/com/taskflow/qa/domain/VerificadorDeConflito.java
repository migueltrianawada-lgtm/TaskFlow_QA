package com.taskflow.qa.domain;

import java.util.List;
import java.util.Objects;

public class VerificadorDeConflito {

    public boolean existeConflito(IntervaloHorario solicitado, List<IntervaloHorario> existentes) {
        Objects.requireNonNull(solicitado, "intervalo solicitado é obrigatório");
        Objects.requireNonNull(existentes, "intervalos existentes são obrigatórios");
        return existentes.stream().anyMatch(solicitado::sobrepoe);
    }
}
