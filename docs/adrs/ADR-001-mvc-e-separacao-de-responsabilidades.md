# ADR-001: MVC e separação de responsabilidades

- **Status:** Proposto
- **Data:** 2026-11-09

## Contexto

Os fluxos incluem interface frontend, autenticação, reservas, validação de conflitos, cadastros, movimentações, auditoria, relatórios e integração de notificações. Acoplar essas responsabilidades a uma única camada dificultaria testes independentes, evolução e troca de tecnologias.

## Decisão

Organizar a solução logicamente em MVC, com View/frontend, Controllers, casos de uso, Model/domínio, contratos de persistência e adaptadores para integrações. Controllers coordenam a entrada e saída, mas as regras de negócio residem no domínio. O domínio não depende de framework, armazenamento ou provedor externo.

## Consequências

- As regras críticas podem ser testadas sem depender da interface ou de serviços externos.
- Frontend e persistência comunicam-se por contratos definidos.
- Mudanças em uma tecnologia podem exigir adaptadores, sem alterar regras do domínio.
- Há custo de manter contratos e limites entre componentes; os limites devem permanecer proporcionais ao escopo.

## Rastreabilidade

- **RFs:** RF-01 a RF-14.
- **RNFs:** RNF-19, RNF-20, RNF-21 e RNF-22.
- **Personas:** SOL, RES e ADM.
