# Índice do pacote de evidências — versão integrada

> A atualização da carga foi integrada à `main`, mas ainda não há tag de release. Os links abaixo identificam o commit e os artifacts efetivamente verificados.

## Identificação do candidato

- Repositório: https://github.com/migueltrianawada-lgtm/TaskFlow_QA
- Pull requests: [#3](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/pull/3), [#4](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/pull/4) e [#5](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/pull/5), todos integrados.
- SHA atual da `main` verificado: `18c267cdef5e7af318b201a5132e8e22f5b6ef01`.
- CI da `main`: [run #43553788543 — sucesso](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43553788543).
- Artifacts desse CI: [JaCoCo](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43553788543/artifacts/14097351626) e [rastreabilidade BDD](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43553788543/artifacts/14097376625).
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
- [Inventário de testes executados e pendentes](inventario-testes.md)
- [README e fluxos da API](../../README.md)

## Pendências de entrega

1. SonarCloud/Quality Gate não foi configurado nem executado; não há token, URL ou chave de projeto disponíveis.
2. PostgreSQL/Testcontainers permanece fora da validação por decisão da responsável; o resultado H2 não substitui esse teste.
3. Decidir se os critérios pendentes permitem a entrega e se uma tag de release será criada.
4. O envio ao Hub deve ser realizado por uma integrante do grupo.

Foi executado um ensaio exploratório local de 2.000 usuários autenticados com H2 descartável. A taxa de erros foi 0%, mas o p95 de disponibilidade (2.688,28 ms) excedeu o limite de 2 s; não houve medição de frontend nem validação de produção/PostgreSQL. Consulte `../performance/resultado-01.md`. Nenhum placeholder nesta página constitui evidência de aprovação. Revalidar os links antes do envio.
