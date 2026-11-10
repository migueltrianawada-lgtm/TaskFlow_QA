# Architecture Decision Records

ADRs (registros de decisões arquiteturais) documentam as táticas propostas, o contexto que as motiva, a decisão, consequências e requisitos relacionados. Cada registro é uma proposta documental; nenhuma decisão abaixo implica implementação.

| ADR | Tática | Status |
| --- | --- | --- |
| [ADR-001](ADR-001-mvc-e-separacao-de-responsabilidades.md) | MVC, separação de responsabilidades e adaptadores | Proposto |
| [ADR-002](ADR-002-autorizacao-no-servidor.md) | Autorização no servidor por perfil e escopo | Proposto |
| [ADR-003](ADR-003-validacao-e-erros-do-frontend.md) | Validação em camadas e erros seguros no frontend | Proposto |
| [ADR-004](ADR-004-reserva-atomica-e-concorrencia.md) | Operações atômicas e controle de concorrência | Proposto |
| [ADR-005](ADR-005-auditoria-consistente.md) | Auditoria consistente e protegida | Proposto |
| [ADR-006](ADR-006-notificacao-desacoplada.md) | Notificação desacoplada com isolamento de falha | Proposto |
| [ADR-007](ADR-007-consultas-e-relatorios.md) | Consultas filtradas e rastreamento de conflitos | Proposto |
| [ADR-008](ADR-008-portabilidade-e-adaptadores.md) | Configuração por ambiente e contratos agnósticos | Proposto |
| [ADR-009](ADR-009-java-spring-react.md) | Java Spring Boot e React | Aceito |
| [ADR-010](ADR-010-postgresql-testcontainers.md) | PostgreSQL em Testcontainers para CI | Aceito |

## Convenção

Cada ADR usa os campos: status, contexto, decisão, consequências, RFs relacionados e RNFs relacionados. A arquitetura consolidada está em [`../arquitetura.md`](../arquitetura.md); diagramas estáticos e dinâmicos estão nas pastas irmãs `modelagens-estaticas/` e `modelagens-dinamicas/`.
