# Resultado de desempenho — PERF-01

## Conclusão

**Inconclusivo para RNF-03/RNF-05.** Foi executado apenas um smoke de leitura com 10 usuários concorrentes, não o teste requerido de 2.000 usuários nem medição ponta a ponta no navegador. Portanto, não há evidência para aprovar os limites de latência ou taxa de erros estabelecidos para a carga-alvo.

## Por que o ensaio de 2.000 não foi executado

Na inspeção atual, o computador compartilhado tinha aproximadamente **0,8 GiB de memória física livre**. Não há infraestrutura isolada de teste de carga provisionada, configuração de aplicação/banco comparável à implantação, nem massa sintética e monitoramento preparados para o perfil de 15 minutos definido em `plano-01.md`. Gerar essa carga neste host poderia esgotar recursos e afetar outros processos; executar contra Neon/produção sem ambiente e autorização de teste também seria inadequado. Por isso, o teste não foi iniciado e a meta permanece **não medida**, não aprovada.

## Evidência local limitada

- Ambiente: aplicação local Java 21/Spring Boot, H2 em memória, perfil `dev`; API escutando em `localhost:18080`.
- Dados: 20 salas sintéticas criadas no banco H2 descartável.
- Operação: GET autenticado e somente leitura a `/api/recursos`.
- Duração/concurrency configuradas: 10 s / 10 workers.
- Resultado: 2.440 respostas HTTP 200, 0 falhas, 239,63 solicitações/s; p50 28,89 ms, p95 107,43 ms, p99 197,66 ms, máximo 438,29 ms.
- Método: `scripts/performance-smoke.py`; usa 10 threads locais e mede latência HTTP da API.

Esses números caracterizam somente esta execução local e esta consulta. Não representam produção, concorrência de gravação, relatórios, navegador, 2.000 sessões ou outras operações. Não devem ser apresentados como validação de RNF-03/RNF-05. Não há artifact externo desta execução.

## Condição do ambiente de desenvolvimento

Na inspeção atual, o host local tinha aproximadamente 0,8 GiB de memória física livre. Por isso, não executei um ensaio de 1.000 ou 2.000 usuários nesse host compartilhado: além de não representar o ambiente de produção, poderia degradar outros processos. O ensaio de aceitação precisa ocorrer em infraestrutura isolada e dimensionada, conforme `plano-01.md`.

## Pendências para resultado de aceitação

Provisionar e autorizar um executor isolado com recursos suficientes, banco e aplicação representativos, massa sintética e monitoramento. Em seguida, registrar data, SHA exato, ambiente, versões/configuração, distribuição da carga, duração, concorrência atingida, p50/p95/p99 por operação e perfil, throughput, erros/timeouts, utilização de recursos, consistência pós-teste e links dos artefatos originais. Sem esse ambiente, RNF-03/RNF-05 continuam sem comprovação.
