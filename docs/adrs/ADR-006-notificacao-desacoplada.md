# ADR-006: Notificação desacoplada com isolamento de falha

- **Status:** Proposto
- **Data:** 2026-11-09

## Contexto

O sistema deve suportar notificação simulada ou integração com API externa. A falha ou indisponibilidade do serviço de notificação não deve transformar uma reserva válida em falha nem deixar incerto o estado que o frontend apresenta.

## Decisão

Invocar notificações por um contrato independente da regra de reserva, com adaptadores para simulador ou integração externa. Persistir primeiro o estado válido do fluxo de negócio; tratar resultado de notificação separadamente e registrar sucesso ou falha. A interface apresenta o estado da reserva, não confunde estado de entrega da notificação com estado da reserva.

## Consequências

- É possível trocar o mecanismo de notificação sem alterar as regras centrais.
- A notificação pode não ser entregue imediatamente, e falhas precisam ser observáveis no registro correspondente.
- O fluxo precisa evitar duplicar ou perder estados de reserva ao lidar com falhas externas.
- O mecanismo de repetição e a política de entrega ficam para decisão posterior, sem serem pressupostos aqui.

## Rastreabilidade

- **RFs:** RF-04, RF-07 e RF-11.
- **RNFs:** RNF-07 e RNF-12.
- **Personas:** SOL, RES e ADM.
