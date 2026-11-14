# Checklist de entrega — Aula 14

## Escopo e método

- Repositório público: [migueltrianawada-lgtm/TaskFlow_QA](https://github.com/migueltrianawada-lgtm/TaskFlow_QA).
- Referência auditada inicialmente: `main` no commit `4a92eb9c18f5a8ab1bb086efc17dc7858adafaaa`.
- Clone limpo da referência: concluído; o clone permaneceu sem alterações rastreadas.
- Reprodução no clone: `backend/.mvnw.cmd -B --no-transfer-progress verify` passou; API iniciou e `GET /api/health` retornou `{"status":"UP"}`. `npm ci`, 7 testes frontend, `npm run build` e `npm run dev` passaram.
- Limitações da referência: sem Docker, Testcontainers foi ignorado localmente; o Maven reportou 48 testes ignorados, inclusive cenários BDD não executáveis.
- Estado atual: PRs [#3](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/pull/3), [#4](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/pull/4) e [#5](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/pull/5) integrados. `main` em `18c267cdef5e7af318b201a5132e8e22f5b6ef01`; ainda não há tag.
- CI na `main`: [run #43553788543](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43553788543) passou e publicou artifacts [JaCoCo](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43553788543/artifacts/14097351626) e [BDD](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43553788543/artifacts/14097376625). `PostgresContainerTest` continua ignorado conforme orientação da responsável: não há Docker local nem ambiente Docker válido no runner.

## Inventário atualizado

| Item | Existe? | Atualizado/verificado? | Caminho ou evidência |
|---|---|---|---|
| Repositório público | Sim | Implementação e atualização de carga integradas à `main`; sem tag de release | [GitHub](https://github.com/migueltrianawada-lgtm/TaskFlow_QA) |
| Revisão por pares | Parcial | Não comprovada nas PRs auditadas #1/#2 | PRs integradas; sem registro de review identificado |
| Aplicação executável | Sim | Sim no baseline; funcionalidade local foi validada separadamente | `backend/`, `frontend/`; ver `docs/relatorio-qualidade.md` |
| README para reprodução | Sim | Atualizado localmente; falta validar no clone do commit integrado | `README.md` |
| Documentação da API | Sim | Swagger/OpenAPI e README; revisar no candidato integrado | `/swagger-ui.html`; `README.md` |
| Matriz de rastreabilidade | Sim | Versionada no candidato; estados parciais explícitos | `docs/rtm.md` |
| Diagrama de contexto | Sim | Rascunho local conforme a arquitetura atual | `docs/modelagens-estaticas/diagrama-contexto.md` |
| Diagrama de componentes | Sim | Atualizado para React/Spring Boot/JPA e perfis de banco | `docs/modelagens-estaticas/diagrama-componentes.md` |
| Sequências críticas | Sim | Diagramas existentes; mapeamento e limites de teste indicados | `docs/modelagens-dinamicas/sequencias-requisitos-funcionais.md` |
| Plano/cenários e relatório de testes | Parcial | 3 de 59 cenários BDD executáveis; cobertura BDD não equivale à cobertura de código | `docs/testes-bdd/` |
| Pipeline de CI | Sim | CI público passou no commit atual da `main` | [Run #43553788543](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43553788543) |
| Relatório JaCoCo | Sim | Artifact gerado pelo CI do SHA atual da `main` | [Artifact JaCoCo](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43553788543/artifacts/14097351626) |
| SonarCloud / Quality Gate | Não executado | Workflow não contém etapa Sonar; não há secrets/variables de Actions nem variáveis de ambiente Sonar disponíveis; falta token e configuração do projeto | Sem análise/link confirmado; requer configuração e credencial autorizada |
| Plano e resultado de teste de carga | Sim | Ensaio local exploratório com 2.000 usuários: 0% de erros, p95 2.688,28 ms (acima do limite de 2 s); não aprova RNF-03/RNF-05 | `docs/performance/` |
| Registro de defeitos | Sim | Rascunho inicial com defeitos, lacunas e riscos | `docs/defeitos/registro-01.md` |
| Relatório final de qualidade | Sim | Atualizar neste commit com SHA integrado e CI da `main`; sem tag de release | `docs/relatorio-qualidade.md` |
| Changelog | Sim | Entrada Unreleased, não publicada | `CHANGELOG.md` |
| Índice do pacote de evidências | Sim | Rascunho; links da tag e artifacts pendentes | `docs/evidencias/README.md` |
| Decisão de qualidade | Sim | Rascunho bloqueia liberação; falta assinatura/nome/responsável do grupo | `docs/decisao-qualidade.md` |
| Tag de release candidate | Não | Nenhuma tag desta entrega | Criar somente após CI verde no commit integrado da `main` |
| Declaração nominal do grupo | Sim — grupo de uma integrante, conforme informado pela responsável | Rascunho registrado; falta revisão e confirmação final da declaração | `docs/decisao-qualidade.md` |
| Envio ao Hub | Não verificável | Não realizado por este trabalho | Um integrante deve enviar o link/tag final |

## Bloqueadores, em ordem

1. SonarCloud permanece pendente: obter/configurar projeto e token autorizado, executar a análise no SHA desejado e registrar o Quality Gate. Não há credencial disponível para execução agora.
2. Manter PostgreSQL/Testcontainers ignorado conforme orientação da responsável; Docker local e runner sem ambiente válido. Não alegar validação PostgreSQL.
3. Decidir se o p95 acima da meta e as outras lacunas são aceitos para a entrega; a análise atual recomenda não declarar aprovação plena.
4. A integrante única deve revisar e confirmar a declaração e deliberar sobre os riscos antes de finalizar a decisão.
5. Se aprovada uma release candidate, criar tag somente no SHA aprovado e com CI verde; não mover tags publicadas.
6. O formulário do Hub deve ser enviado por uma integrante do grupo.
