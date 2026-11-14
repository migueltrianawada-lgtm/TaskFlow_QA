# Resultado de desempenho — PERF-01

## Conclusão

**Inconclusivo para RNF-03/RNF-05.** Foi executado apenas um smoke de leitura com 10 usuários concorrentes, não o teste requerido de 2.000 usuários nem medição ponta a ponta no navegador. Portanto, não há evidência para aprovar os limites de latência ou taxa de erros estabelecidos para a carga-alvo.

## Evidência local limitada

- Ambiente: aplicação local Java 21/Spring Boot, H2 em memória, perfil `dev`; API escutando em `localhost:18080`.
- Dados: 20 salas sintéticas criadas no banco H2 descartável.
- Operação: GET autenticado e somente leitura a `/api/recursos`.
- Duração/concurrency configuradas: 10 s / 10 workers.
- Resultado: 2.440 respostas HTTP 200, 0 falhas, 239,63 solicitações/s; p50 28,89 ms, p95 107,43 ms, p99 197,66 ms, máximo 438,29 ms.
- Método: `scripts/performance-smoke.py`; usa 10 threads locais e mede latência HTTP da API.

Esses números caracterizam somente esta execução local e esta consulta. Não representam produção, concorrência de gravação, relatórios, navegador, 2.000 sessões ou outras operações. Não devem ser apresentados como validação de RNF-03/RNF-05. Não há artifact externo desta execução.

## Condição do ambiente de desenvolvimento

Na inspeção feita durante a preparação, o host local tinha menos de 1 GiB de memória física livre. Por isso, não executei um ensaio de 1.000 ou 2.000 usuários nesse host compartilhado: além de não representar o ambiente de produção, poderia degradar outros processos. O ensaio de aceitação precisa ocorrer em infraestrutura isolada e dimensionada, conforme `plano-01.md`.

## Pendências para resultado de aceitação

Registrar a data, SHA exato, ambiente, versões/configuração, massa e distribuição de carga, duração, concorrência atingida, p50/p95/p99 por operação e perfil, throughput, erros/timeouts, utilização de recursos, consistência pós-teste e links para os artefatos originais. Repetir o teste no ambiente controlado descrito em `plano-01.md`.
