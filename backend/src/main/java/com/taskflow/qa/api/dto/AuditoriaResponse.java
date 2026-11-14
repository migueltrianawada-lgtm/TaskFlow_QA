package com.taskflow.qa.api.dto;

import java.time.Instant;

public record AuditoriaResponse(
        Long id, String usuario, String acao, String tipoEntidade, Long entidadeId,
        String dadosAnteriores, String dadosNovos, Instant ocorridaEm) {
}
