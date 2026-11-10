# ADR-002: Autorização no servidor por perfil e escopo

- **Status:** Proposto
- **Data:** 2026-11-09

## Contexto

O sistema possui os perfis Solicitante, Responsável e Administrador. A interface pode ocultar ações, mas não é uma fronteira confiável de segurança. Além do perfil, operações sobre reservas precisam respeitar o escopo do solicitante e o estado do recurso.

## Decisão

Validar no servidor a identidade, o perfil e o escopo em cada operação protegida. A interface pode adaptar navegação e ações visíveis ao perfil para usabilidade, mas essa adaptação nunca substitui autorização no servidor. Acesso negado não deve alterar dados nem revelar informação protegida.

## Consequências

- A autorização mantém-se efetiva mesmo se chamadas forem feitas fora do frontend.
- Cada operação precisa declarar a permissão necessária e, quando aplicável, verificar propriedade ou escopo da entidade.
- Testes devem cobrir acesso autorizado e negado para cada perfil e operação.
- Mensagens de erro precisam evitar exposição de dados e detalhes de implementação.

## Rastreabilidade

- **RFs:** RF-01 a RF-12.
- **RNFs:** RNF-14, RNF-15 e RNF-18.
- **Personas:** SOL, RES e ADM.
