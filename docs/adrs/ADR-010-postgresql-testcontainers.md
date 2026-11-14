# ADR-010: PostgreSQL em Testcontainers para o CI

- **Status:** Aceito
- **Data:** 2026-11-09

## Contexto

O pipeline precisa validar integração com um banco containerizado. A execução local do teste pode não ter Docker disponível.

## Decisão

Usar PostgreSQL 16 em Testcontainers para testar o schema e as migrações do banco nos testes de integração e no pipeline de CI. A decisão de usar PostgreSQL hospedado em produção/desenvolvimento compartilhado é registrada separadamente no [ADR-011](ADR-011-neon-postgresql.md).

## Consequências

- O runner de CI precisa oferecer Docker compatível com Testcontainers; GitHub-hosted Ubuntu atende esse requisito.
- Execução do teste de container requer Docker. O workflow executa o teste, mas a execução do CI candidato #43541031194 o ignorou ao não encontrar um ambiente Docker válido; a decisão ainda não está validada no pipeline.
- O teste inicia o perfil `prod` após aplicar a migração, validando o schema também contra as entidades JPA; não cobre regras de reservas nem persistência dos casos de uso.

## Rastreabilidade

- **RFs:** nenhum RF de persistência de negócio é considerado implementado por esta decisão.
- **RNFs:** RNF-01, RNF-19 e RNF-22.
- **Personas:** Solicitante, Responsável e Administrador.
