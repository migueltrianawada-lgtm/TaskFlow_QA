# ADR-007: Consultas filtradas e rastreamento de conflitos

- **Status:** Proposto
- **Data:** 2026-11-09

## Contexto

Pesquisa de disponibilidade e relatórios precisam responder em uso interativo e no cenário de teste com 10.000 usuários simultâneos. O relatório de conflitos evitados requer evidência das tentativas rejeitadas, não apenas reservas confirmadas.

## Decisão

Aplicar filtros no acesso aos dados, consultando somente os recursos e intervalos pertinentes à pesquisa ou relatório. Calcular disponibilidade combinando os recursos elegíveis com reservas ativas e bloqueios aplicáveis. Registrar tentativas rejeitadas por conflito como eventos auditáveis mínimos para sustentar o indicador de conflitos evitados. Não adotar cache ou tecnologia de análise específica nesta etapa.

## Consequências

- Consultas e relatórios mantêm relação explícita entre filtros, dados consultados e resultado.
- O registro de conflito precisa evitar dados pessoais desnecessários e possuir política de acesso.
- Métricas do teste de carga devem incluir latências p50/p95/p99 e taxa de erro.
- Otimizações físicas adicionais só devem ser escolhidas com base em medição e sem comprometer consistência.

## Rastreabilidade

- **RFs:** RF-03, RF-05 e RF-12.
- **RNFs:** RNF-03 e RNF-05.
- **Personas:** SOL, RES e ADM; relatórios para RES e ADM.
