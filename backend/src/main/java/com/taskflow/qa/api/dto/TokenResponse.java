package com.taskflow.qa.api.dto;

public record TokenResponse(String accessToken, String tokenType, long expiresIn, UsuarioResponse usuario) {
}
