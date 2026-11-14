package com.taskflow.qa.api;

import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail tratarValidacao(MethodArgumentNotValidException exception) {
        var erros = exception.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage() == null ? "Valor inválido" : error.getDefaultMessage(),
                        (primeiro, ignorado) -> primeiro));
        ProblemDetail detalhe = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Há campos inválidos.");
        detalhe.setProperty("erros", Map.copyOf(erros));
        return detalhe;
    }

    @ExceptionHandler(ResponseStatusException.class)
    ProblemDetail tratarStatus(ResponseStatusException exception) {
        HttpStatusCode status = exception.getStatusCode();
        String mensagem = exception.getReason() == null ? "A solicitação não pôde ser concluída."
                : exception.getReason();
        return ProblemDetail.forStatusAndDetail(status, mensagem);
    }
}
