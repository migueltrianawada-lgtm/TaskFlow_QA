# Índice do pacote de evidências — versão integrada

> A atualização da carga foi integrada à `main`, mas ainda não há tag de release. Os links abaixo identificam o commit e os artifacts efetivamente verificados.

## Identificação do candidato

- Repositório: https://github.com/migueltrianawada-lgtm/TaskFlow_QA
- Pull requests: [#3](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/pull/3) e [#4](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/pull/4), ambos integrados.
- SHA atual da `main` verificado: `2ff104ef8e03a9550508f332a523986a9a600751`.
- CI da `main`: [run #43552739034 — sucesso](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43552739034).
- Artifacts desse CI: [JaCoCo](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43552739034/artifacts/14096622744) e [rastreabilidade BDD](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43552739034/artifacts/14096522932).
- Tag de release: **pendente**. Criar `v1.0.0-rc1` somente após a decisão final de qualidade; se houver correção após usar uma tag, criar uma nova versão.
- Quality Gate SonarCloud do SHA: **pendente de configuração/execução**; não há link verificado.

O `PostgresContainerTest` permanece deliberadamente ignorado, conforme orientação da responsável. Docker está indisponível neste ambiente local e o runner não encontrou ambiente Docker válido. O CI verde da `main` não comprova a validação PostgreSQL/Testcontainers.

## Evidências versionadas

- [Matriz de rastreabilidade](../rtm.md)
- [Plano de desempenho](../performance/plano-01.md)
- [Resultado de desempenho](../performance/resultado-01.md)
- [Registro de defeitos e lacunas](../defeitos/registro-01.md)
- [Relatório de qualidade](../relatorio-qualidade.md)
- [Relatório BDD](../testes-bdd/relatorio-cobertura.md)
- [README e fluxos da API](../../README.md)

## Pendências de entrega

1. SonarCloud/Quality Gate não foi configurado nem executado; não há token, URL ou chave de projeto disponíveis.
2. PostgreSQL/Testcontainers permanece fora da validação por decisão da responsável; o resultado H2 não substitui esse teste.
3. Decidir se os critérios pendentes permitem a entrega e se uma tag de release será criada.
4. O envio ao Hub deve ser realizado por uma integrante do grupo.

Foi executado um ensaio exploratório local de 2.000 usuários autenticados com H2 descartável. A taxa de erros foi 0%, mas o p95 de disponibilidade (2.688,28 ms) excedeu o limite de 2 s; não houve medição de frontend nem validação de produção/PostgreSQL. Consulte `../performance/resultado-01.md`. Nenhum placeholder nesta página constitui evidência de aprovação. Revalidar os links antes do envio.
