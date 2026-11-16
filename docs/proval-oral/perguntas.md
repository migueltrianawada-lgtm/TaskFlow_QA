# Banco de Perguntas para Arguição Oral (Aula 14) — TaskFlow_QA

## Bloco A: Arquitetura, ATAM e Decisões Técnicas
1. **[ATAM & Requisitos]** Como os cenários de qualidade da Utility Tree orientaram as decisões arquiteturais e as táticas de Len Bass adotadas no projeto?
2. **[Modelo C4]** Explique a diferença de responsabilidade entre os níveis de Contêiner e Componente na arquitetura do TaskFlow_QA.
3. **[Batch vs. Online]** Por que o processamento batch exige mecanismos de checkpoint e idempotência para não impactar o fluxo online?
4. **[ADRs]** Qual foi a principal decisão arquitetural registrada em ADR e qual o trade-off aceito pela equipe?

## Bloco B: Testes, Cobertura e Dublês
5. **[Estratégia de Mocks]** Qual a diferença prática entre usar `Mockito`, `WireMock` e `Testcontainers` no projeto?
6. **[Concorrência]** Como a suíte de testes garante que duas requisições no mesmo milissegundo não gerem reserva dupla?
7. **[Relógio Determinístico]** Por que é crítico injetar um `Clock` determinístico nos testes de expiração de token e reservas?
8. **[JaCoCo vs. SonarCloud]** Ter 80% de cobertura no JaCoCo garante ausência de vulnerabilidades de segurança? Justifique.
9. **[Testes de Mutação]** O que a suíte de mutação manual provou sobre a qualidade das asserções dos nossos testes?

## Bloco C: BDD, TDD e Segurança
10. **[Ciclo TDD]** Como a disciplina dos commits `test(red)`, `feat(green)` e `refactor` foi mantida durante o desenvolvimento?
11. **[Cucumber / Gherkin]** Como os cenários de `.feature` se transformam em especificações executáveis com MockMvc?
12. **[Matriz de Autorização]** Como o sistema garante que um perfil `SOLICITANTE` não execute operações reservadas ao `ADMINISTRADOR`?
13. **[ProblemDetail]** Por que sanitizar as respostas de erro 401/403 é vital para evitar vazamento de informações (Information Disclosure)?

## Bloco D: Desempenho e Decisão de Release
14. **[Métricas de Carga]** Por que um teste do JMeter com 0% de erro e p95 de 3,2s não pode ser declarado como meta cumprida?
15. **[Release Candidate]** Quais critérios justificam a decisão de liberar o sistema em `v1.0.0-rc1` mesmo com riscos residuais aceitos?