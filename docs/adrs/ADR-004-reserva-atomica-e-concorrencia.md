# ADR-004: Reserva atômica e controle de concorrência

- **Status:** Proposto
- **Data:** 2026-11-09

## Contexto

Uma leitura de disponibilidade seguida de gravação sem coordenação permite que duas solicitações simultâneas reservem o mesmo horário. O mesmo risco aplica-se à agenda de professores e ao saldo de materiais. Falhas durante a gravação não podem deixar recursos parcialmente alocados.

## Decisão

Executar validação de disponibilidade e gravação da reserva, recursos associados e estado necessário em uma operação atômica. A persistência deve coordenar solicitações concorrentes de modo que apenas alocações compatíveis sejam confirmadas. A estratégia física de isolamento, bloqueio ou restrição fica para escolha tecnológica posterior; ela deve preservar a garantia, não apenas uma consulta prévia.

Em caso de conflito, rejeitar a solicitação sem alocação parcial e devolver um estado de conflito ao frontend. Alteração e cancelamento devem seguir o mesmo princípio transacional.

## Consequências

- A camada de persistência é parte da garantia de não haver dupla reserva.
- Solicitações concorrentes podem aguardar ou receber conflito, conforme o mecanismo escolhido, mas não podem ambas confirmar uma alocação incompatível.
- Testes de concorrência e falha precisam verificar tanto a resposta como o estado persistido final.
- O desempenho em 10.000 usuários simultâneos deve ser medido; a estratégia concreta não pode ser presumida antes dessa medição.

## Rastreabilidade

- **RFs:** RF-04, RF-05, RF-06, RF-08 e RF-09.
- **RNFs:** RNF-03, RNF-04, RNF-11 e RNF-20.
- **Personas:** SOL, RES e ADM.
