package com.taskflow.qa.api.dto;

import com.taskflow.qa.domain.PerfilAcesso;
import java.util.List;

public record UsuarioResponse(Long id, String nome, String email, List<PerfilAcesso> perfis, boolean ativo) {
}
