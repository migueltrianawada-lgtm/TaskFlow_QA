# Índice do pacote de evidências — candidato

> **Não é índice de release final.** O candidato está em PR aberto para `main`, ainda não integrado nem marcado com tag. Os links/artefatos abaixo comprovam somente a execução no PR; não usar como evidência de uma tag futura.

## Identificação do candidato

- Repositório: https://github.com/migueltrianawada-lgtm/TaskFlow_QA
- Branch: `miguelpalacios1987-documentacao-projeto`.
- Pull request: [#3](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/pull/3), aberto e mergeável, ainda sem decisão de revisão registrada.
- SHA do candidato no PR: `766354d3fb68c2893be84f7f625ead19b235bc00`.
- CI do PR: [run #43541559873 — sucesso](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43541559873).
- Artifacts dessa execução: [JaCoCo](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43541559873/artifacts/14091492854) e [rastreabilidade BDD](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43541559873/artifacts/14092202072).
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

O teste de 2.000 usuários também continua pendente: não foi iniciado porque o host compartilhado tinha cerca de 0,8 GiB de memória livre e não existe executor isolado de carga provisionado. Nenhum placeholder nesta página constitui evidência de aprovação. Revalidar os links antes do envio.
