# Checklist de entrega — Aula 14

## Escopo e método

- Repositório público: [migueltrianawada-lgtm/TaskFlow_QA](https://github.com/migueltrianawada-lgtm/TaskFlow_QA).
- Referência auditada: `main` no commit `4a92eb9c18f5a8ab1bb086efc17dc7858adafaaa`.
- Clone limpo da referência: concluído; o clone permaneceu sem alterações rastreadas.
- Reprodução no clone: `backend/.mvnw.cmd -B --no-transfer-progress verify` passou; API iniciou e `GET /api/health` retornou `{"status":"UP"}`. `npm ci`, 7 testes frontend, `npm run build` e `npm run dev` passaram.
- Limitações da referência: sem Docker, Testcontainers foi ignorado localmente; o Maven reportou 48 testes ignorados, inclusive cenários BDD não executáveis.
- Estado do candidato: commit `1a623c785daaabbdb1f5ba29b0da7d50785f410f` enviado à branch `miguelpalacios1987-documentacao-projeto`; ainda sem PR, integração à `main` ou tag.
- CI da branch: [run #43541031194](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43541031194) passou e publicou artifacts JaCoCo e BDD. `PostgresContainerTest` foi ignorado porque o runner não encontrou Docker válido.

## Inventário atualizado

| Item | Existe? | Atualizado/verificado? | Caminho ou evidência |
|---|---|---|---|
| Repositório público | Sim | Parcial — candidato local ainda não integrado | [GitHub](https://github.com/migueltrianawada-lgtm/TaskFlow_QA) |
| Revisão por pares | Parcial | Não comprovada nas PRs auditadas #1/#2 | PRs integradas; sem registro de review identificado |
| Aplicação executável | Sim | Sim no baseline; funcionalidade local foi validada separadamente | `backend/`, `frontend/`; ver `docs/relatorio-qualidade.md` |
| README para reprodução | Sim | Atualizado localmente; falta validar no clone do commit integrado | `README.md` |
| Documentação da API | Sim | Swagger/OpenAPI e README; revisar no candidato integrado | `/swagger-ui.html`; `README.md` |
| Matriz de rastreabilidade | Sim | Rascunho local; estados parciais explícitos | `docs/rtm.md` |
| Diagrama de contexto | Sim | Rascunho local conforme a arquitetura atual | `docs/modelagens-estaticas/diagrama-contexto.md` |
| Diagrama de componentes | Sim | Atualizado para React/Spring Boot/JPA e perfis de banco | `docs/modelagens-estaticas/diagrama-componentes.md` |
| Sequências críticas | Sim | Diagramas existentes; mapeamento e limites de teste indicados | `docs/modelagens-dinamicas/sequencias-requisitos-funcionais.md` |
| Plano/cenários e relatório de testes | Parcial | 3 de 59 cenários BDD executáveis; cobertura BDD não equivale à cobertura de código | `docs/testes-bdd/` |
| Pipeline de CI | Sim | CI público da branch candidata verde; não integrado à `main` | [Run #43541031194](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43541031194) |
| Relatório JaCoCo | Gerado localmente e publicado no CI candidato | Artifact da branch disponível; artifact do SHA integrado/tag ainda pendente | [Artifact JaCoCo](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43541031194/artifacts/14092062221) |
| SonarCloud / Quality Gate | Não comprovado | Estado da configuração não conhecido; consulta de secrets/variables retornou HTTP 403 | Sem análise/link confirmado; conferir sem presumir ausência |
| Plano e resultado de teste de carga | Sim | Plano atualizado; meta revisada de 2.000 usuários não executada | `docs/performance/` |
| Registro de defeitos | Sim | Rascunho inicial com defeitos, lacunas e riscos | `docs/defeitos/registro-01.md` |
| Relatório final de qualidade | Sim | Rascunho local; precisa refletir SHA integrado e artefatos finais | `docs/relatorio-qualidade.md` |
| Changelog | Sim | Entrada Unreleased, não publicada | `CHANGELOG.md` |
| Índice do pacote de evidências | Sim | Rascunho; links da tag e artifacts pendentes | `docs/evidencias/README.md` |
| Decisão de qualidade | Sim | Rascunho bloqueia liberação; falta assinatura/nome/responsável do grupo | `docs/decisao-qualidade.md` |
| Tag de release candidate | Não | Nenhuma tag desta entrega | Criar somente após CI verde no commit integrado da `main` |
| Declaração nominal do grupo | Sim — grupo de uma integrante, conforme informado pela responsável | Rascunho registrado; falta revisão e confirmação final da declaração | `docs/decisao-qualidade.md` |
| Envio ao Hub | Não verificável | Não realizado por este trabalho | Um integrante deve enviar o link/tag final |

## Bloqueadores, em ordem

1. Revisar o candidato enviado e integrá-lo à `main` por PR; o baseline público ainda não representa o candidato.
2. Executar clone limpo e `verify` no commit integrado; confirmar README, testes e relatório JaCoCo.
3. Completar ou justificar formalmente os cenários BDD ainda ignorados e executar PostgreSQL/Testcontainers em runner com Docker válido; o teste foi ignorado no CI da branch.
4. Confirmar se SonarCloud está configurado, executar a análise no commit candidato e registrar o Quality Gate; a permissão atual não permitiu inspecionar secrets/variables.
5. Executar o teste de carga controlado de 2.000 usuários previsto no RNF-03/RNF-05, ou registrar explicitamente o bloqueio e não alegar atendimento.
6. A integrante única revisar e confirmar a declaração e deliberar sobre riscos antes de finalizar a decisão.
7. Atualizar o índice com links válidos e artifacts da mesma execução CI/SHA da tag.
8. Somente com os itens aplicáveis resolvidos e CI verde na `main`, criar/enviar tag anotada `v1.0.0-rc1`. Nunca mover uma tag usada; se mudar o commit após a tag, criar `v1.0.0-rc2`.
9. Após validação final, preparar o texto de entrega; o formulário do Hub deve ser enviado por um integrante.
