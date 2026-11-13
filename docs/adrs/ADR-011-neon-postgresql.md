# ADR-011: PostgreSQL gerenciado no Neon

- **Status:** Aceito
- **Data:** 2026-11-13

## Contexto

O projeto precisa de um banco hospedado acessível para desenvolvimento e demonstração sem administrar um servidor próprio. O domínio e os requisitos incluem integridade relacional e transações para reservas; PostgreSQL já é usado na validação de integração com Testcontainers.

## Decisão

Usar PostgreSQL gerenciado no Neon para persistência hospedada. A aplicação conecta por JDBC usando variáveis externas (`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` e `SPRING_DATASOURCE_PASSWORD`), TLS obrigatório e o perfil Spring `prod`. Migrações Flyway versionam o schema; Hibernate apenas o valida nesse perfil. H2 em memória permanece disponível no perfil local `dev`.

O plano gratuito é destinado a desenvolvimento e demonstração. Ele pode suspender projetos inativos e não é evidência de que o sistema cumpre a meta de 10.000 usuários simultâneos.

## Consequências

- Credenciais não são armazenadas no repositório. O arquivo local `backend/.env` é ignorado pelo Git; ambientes hospedados devem usar seu gerenciador de segredos.
- A URL de conexão deve vir do painel Neon e usar TLS (`sslmode=require`); quando aplicável, usar o endpoint com pooler para aplicações.
- O pipeline valida as migrações em PostgreSQL 16 via Testcontainers.
- Backups, limites do plano, disponibilidade e dimensionamento devem ser reavaliados antes de uma implantação de produção ou teste de carga.
- A escolha do provedor não implementa os casos de uso nem as regras de concorrência de reserva.

## Rastreabilidade

- **RFs:** RF-02, RF-04 a RF-10 e RF-12.
- **RNFs:** RNF-03, RNF-04, RNF-11, RNF-13, RNF-19 e RNF-22.
- **Personas:** Solicitante, Responsável e Administrador.
