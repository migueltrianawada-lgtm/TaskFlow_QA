# Índice do pacote de evidências — candidato

> **Não é índice de release final.** O candidato está em PR aberto para `main`, ainda não integrado nem marcado com tag. Os links/artefatos abaixo comprovam somente a execução no PR; não usar como evidência de uma tag futura.

## Identificação do candidato

- Repositório: https://github.com/migueltrianawada-lgtm/TaskFlow_QA
- Branch: `miguelpalacios1987-documentacao-projeto`.
- Pull request: [#3](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/pull/3), aberto e mergeável, ainda sem decisão de revisão registrada.
- SHA do último candidato com CI verificado: `0425ed73bdedea13f093e489db737856fde19ef3`.
- CI desse candidato: [run #43542466476 — sucesso](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43542466476).
- Artifacts dessa execução: JaCoCo e rastreabilidade BDD publicados no run; validar a disponibilidade dos links antes do envio.
- Tag avaliada e SHA integrado na `main`: **pendentes**. Criar `v1.0.0-rc1` somente após integração e CI verde; se houver correção após uso da tag, criar `v1.0.0-rc2`.
- Quality Gate SonarCloud do SHA: **pendente de configuração/execução**; não há link verificado.

O `PostgresContainerTest` permanece deliberadamente ignorado, conforme orientação da responsável. Não foi executado porque Docker está indisponível neste ambiente local e o runner do CI do PR #3 também não encontrou um ambiente Docker válido. O workflow verde não comprova a validação PostgreSQL/Testcontainers.

## Evidências versionadas

- [Matriz de rastreabilidade](../rtm.md)
- [Plano de desempenho](../performance/plano-01.md)
- [Resultado de desempenho](../performance/resultado-01.md)
- [Registro de defeitos e lacunas](../defeitos/registro-01.md)
- [Relatório de qualidade](../relatorio-qualidade.md)
- [Relatório BDD](../testes-bdd/relatorio-cobertura.md)
- [README e fluxos da API](../../README.md)

## Evidências finais ainda necessárias

1. Integrar o candidato à `main` e registrar o SHA resultante.
2. Link de uma execução CI verde e artifact `jacoco-report` para o SHA integrado/tag; conferir se contém `index.html`.
3. PostgreSQL/Testcontainers permanece fora da validação por decisão da responsável; o limite e o motivo estão registrados, e o resultado H2 não substitui esse teste.
4. Link do Quality Gate SonarCloud relativo ao mesmo SHA, caso o serviço esteja configurado.
5. Versionar este índice, `docs/rtm.md`, `docs/performance/resultado-01.md`, `docs/defeitos/` e `docs/relatorio-qualidade.md` no SHA final.

Foi executado um ensaio exploratório local de 2.000 usuários autenticados com H2 descartável. A taxa de erros foi 0%, mas o p95 de disponibilidade (2.688,28 ms) excedeu o limite de 2 s; não houve medição de frontend nem validação de produção/PostgreSQL. Consulte `../performance/resultado-01.md`. Nenhum placeholder nesta página constitui evidência de aprovação. Revalidar os links antes do envio.
