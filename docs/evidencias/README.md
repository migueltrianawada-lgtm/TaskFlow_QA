# Índice do pacote de evidências — rascunho

> **Não é índice de release final.** As alterações ainda estão em branch local, sem commit integrado, tag ou execução de CI correspondente. Todos os links/artefatos de aceitação devem ser substituídos ou confirmados para o SHA exato da tag avaliada. Não usar a execução de baseline abaixo como evidência da tag.

## Identificação pendente

- Repositório: https://github.com/migueltrianawada-lgtm/TaskFlow_QA
- Tag avaliada: **pendente** (`v1.0.0-rc1` somente após integração e CI verde; se houver correção após uso da tag, criar `v1.0.0-rc2`).
- SHA da tag: **pendente**.
- Execução de CI do SHA da tag: **pendente**.
- Artifact JaCoCo da execução: **pendente**.
- Quality Gate SonarCloud do SHA: **pendente de configuração/execução**; não há link verificado.

## Evidências versionadas

- [Matriz de rastreabilidade](../rtm.md)
- [Plano de desempenho](../performance/plano-01.md)
- [Resultado de desempenho](../performance/resultado-01.md)
- [Registro de defeitos e lacunas](../defeitos/registro-01.md)
- [Relatório de qualidade](../relatorio-qualidade.md)
- [Relatório BDD](../testes-bdd/relatorio-cobertura.md)
- [README e fluxos da API](../../README.md)

## Artefatos a anexar após a integração

1. Link de uma execução CI verde cujo SHA seja exatamente o SHA da tag.
2. Artifact `jacoco-report` obtido nessa mesma execução; conferir se contém `index.html`.
3. Link do Quality Gate SonarCloud relativo ao mesmo SHA, caso o serviço esteja configurado.
4. Este índice, `docs/rtm.md`, `docs/performance/resultado-01.md`, `docs/defeitos/` e `docs/relatorio-qualidade.md` versionados no mesmo SHA.

Nenhum placeholder nesta página constitui evidência de aprovação. Revalidar os links antes do envio.
