# ADR-008: Portabilidade e contratos agnósticos

- **Status:** Proposto
- **Data:** 2026-11-09

## Contexto

O projeto mantém em aberto o banco de dados, o provedor de notificação e o ambiente de hospedagem. A arquitetura deve manter essas decisões abertas sem tornar o comportamento do sistema dependente do ambiente. A seleção de Java Spring Boot e React está registrada no ADR-009.

## Decisão

Definir contratos lógicos para persistência, notificação e operações públicas. Usar adaptadores para conectar esses contratos a tecnologias escolhidas posteriormente. Configurações que variam entre desenvolvimento, teste controlado e produção devem ficar fora das regras de negócio e do código específico do ambiente. A stack selecionada não fixa fornecedor de banco, hospedagem ou notificação.

## Consequências

- A documentação permanece independente de fabricante e implantação.
- Cada implementação futura deverá demonstrar que seu adaptador mantém semântica, segurança e garantias transacionais esperadas.
- Contratos públicos e de integração precisam ser versionados e documentados.
- A portabilidade não garante compatibilidade automática; a suíte de validação deve ser executada em cada ambiente e navegador suportado.

## Rastreabilidade

- **RFs:** RF-11 e RF-14.
- **RNFs:** RNF-06, RNF-07, RNF-21 e RNF-22.
- **Personas:** SOL, RES e ADM; consumidores dos fluxos públicos.
