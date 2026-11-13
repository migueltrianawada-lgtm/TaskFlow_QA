package com.taskflow.qa.api;

import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
