# ADR-010: PostgreSQL em Testcontainers para o CI

- **Status:** Aceito
- **Data:** 2026-11-09

## Contexto

O pipeline solicitado precisa validar integração com um serviço containerizado. A arquitetura ainda não possui adaptador de persistência de negócio, nem escolheu um banco de produção. Para não tratar disponibilidade de um serviço de CI como prova de persistência funcional, é necessário delimitar a finalidade do container.

## Decisão

Usar PostgreSQL 16 em Testcontainers, somente no escopo de testes de integração e no pipeline de CI. O teste inicial valida que o container inicia e aceita conexão e consulta SQL. Esta escolha **não** seleciona PostgreSQL como banco de produção e não introduz persistência de negócio na aplicação.

## Consequências

- O runner de CI precisa oferecer Docker compatível com Testcontainers; GitHub-hosted Ubuntu atende esse requisito.
- Execução local do teste de container requer Docker; ele pode ser ignorado localmente quando Docker não está disponível, mas não no pipeline.
- O futuro banco de produção permanece decisão aberta e pode exigir um novo ADR.
- A cobertura do container é de infraestrutura/conectividade, não de regras de reservas nem de esquema de negócio.

## Rastreabilidade

- **RFs:** nenhum RF de persistência de negócio é considerado implementado por esta decisão.
- **RNFs:** RNF-01, RNF-19 e RNF-22.
- **Personas:** não se aplica diretamente; é decisão do ambiente de testes.
