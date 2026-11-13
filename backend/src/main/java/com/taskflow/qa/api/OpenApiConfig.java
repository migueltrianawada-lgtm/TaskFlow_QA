package com.taskflow.qa.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "TaskFlow API",
        version = "v1",
        description = "API para organização de salas, professores, materiais e reservas."))
public class OpenApiConfig {
}
