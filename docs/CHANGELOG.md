# Changelog — TaskFlow_QA

## [v1.0.0-rc1] - 2026-11-16

### Adicionado (Added)
* **API de Saúde:** Endpoint `GET /api/health` para monitoramento de disponibilidade.
* **Validação de Reservas:** Lógica de prevenção contra sobreposição de horários.
* **Segurança e RBAC:** Matriz de autorização por perfil (`SOLICITANTE`, `RESPONSAVEL`, `ADMINISTRADOR`) e padronização de erros sanitizados via `ProblemDetail`.
* **Testes de Concorrência e Integração:** Suíte JUnit 5 com Testcontainers e bloqueio de dupla reserva no PostgreSQL.
* **Especificação Executiva BDD:** Integração do Cucumber-JVM com cenários Gherkin no CI.
* **Governança de Qualidade:** Pipeline no GitHub Actions, relatórios JaCoCo, Quality Gate SonarCloud e RTM 100% rastreável.

### Alterado (Changed)
* Atualização do plano de testes de carga JMeter e ajuste de relógio determinístico (`Clock`) no backend.