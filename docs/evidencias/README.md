# Índice do pacote de evidências — candidato

> **Não é índice de release final.** O candidato está commitado e enviado à branch de trabalho, mas ainda não foi integrado à `main` nem marcado com tag. Os links/artefatos abaixo comprovam somente a execução nessa branch; não usar como evidência de uma tag futura.

## Identificação do candidato

- Repositório: https://github.com/migueltrianawada-lgtm/TaskFlow_QA
- Branch: `miguelpalacios1987-documentacao-projeto`.
- SHA do candidato enviado: `1a623c785daaabbdb1f5ba29b0da7d50785f410f`.
- CI dessa branch: [run #43541031194 — sucesso](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43541031194).
- Artifacts dessa execução: [JaCoCo](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43541031194/artifacts/14092062221) e [rastreabilidade BDD](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43541031194/artifacts/14091987167).
- Tag avaliada e SHA integrado na `main`: **pendentes**. Criar `v1.0.0-rc1` somente após integração e CI verde; se houver correção após uso da tag, criar `v1.0.0-rc2`.
- Quality Gate SonarCloud do SHA: **pendente de configuração/execução**; não há link verificado.

O CI da branch terminou verde, mas o log registra que `PostgresContainerTest` foi ignorado porque o runner não encontrou um ambiente Docker válido. O sucesso do workflow não comprova a validação PostgreSQL/Testcontainers.

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
3. Executar PostgreSQL/Testcontainers em runner com Docker disponível e registrar o resultado.
4. Link do Quality Gate SonarCloud relativo ao mesmo SHA, caso o serviço esteja configurado.
5. Versionar este índice, `docs/rtm.md`, `docs/performance/resultado-01.md`, `docs/defeitos/` e `docs/relatorio-qualidade.md` no SHA final.

Nenhum placeholder nesta página constitui evidência de aprovação. Revalidar os links antes do envio.
