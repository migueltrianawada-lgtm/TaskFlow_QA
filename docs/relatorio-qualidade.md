# Relatório de qualidade — candidato local

## Identificação e decisão

- Repositório: `migueltrianawada-lgtm/TaskFlow_QA`.
- Commit público usado como baseline da auditoria limpa: `4a92eb9c18f5a8ab1bb086efc17dc7858adafaaa`.
- Candidato enviado na branch `miguelpalacios1987-documentacao-projeto`: `1a623c785daaabbdb1f5ba29b0da7d50785f410f`.
- Estado deste relatório: o CI do candidato está verde, mas a branch ainda não foi integrada; este não é um relatório do commit de uma tag.
- Conclusão: **bloquear a entrega como versão aprovada** até que o código local seja integrado, os itens de evidência pendentes sejam decididos e a validação seja refeita no SHA candidato.

## Execuções registradas

| Execução | Escopo | Resultado | Limites |
|---|---|---|---|
| Clone limpo da `main` em `4a92eb9` | `backend/.mvnw.cmd -B --no-transfer-progress verify`; inicialização da API e health check; `npm ci`, testes, build e servidor Vite | Maven verify, health check, 7 testes frontend, build e servidor passaram | Testcontainers ignorado por Docker ausente; 48 testes Maven ignorados; o README/API nesse commit não refletia a implementação funcional local |
| Branch local, antes do JaCoCo | `backend/.mvnw.cmd verify`, `npm test` e `npm run build` | Maven verify passou: 70 testes contabilizados, 0 falhas/erros, 48 ignorados; frontend passou com 4 testes e build | Worktree não commitado; teste PostgreSQL/Testcontainers não executado |
| Branch local, após incluir JaCoCo | Verificação Maven/geração de relatório, frontend | Maven terminou com exit code 0; frontend: 4 testes passaram e build Vite concluiu | Cucumber reportou 91 cenários, 88 ignorados; o aggregate Maven reportou 70 testes, 0 falhas/erros, 48 ignorados. Testcontainers foi ignorado por Docker ausente |
| Branch local, após otimizar relatório | `mvnw verify` após filtro de período no repositório, agregação em passagem única e índice V2 | Maven verify passou: 71 testes contabilizados, 0 falhas/erros, 48 ignorados; JaCoCo gerou relatório | Cucumber reportou 91 cenários, 88 ignorados; PostgreSQL/Testcontainers foi ignorado por Docker ausente; ainda não executado em CI |
| CI do candidato enviado | [Run #43541031194](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43541031194), SHA `1a623c785daaabbdb1f5ba29b0da7d50785f410f` | Workflow verde; backend, frontend, build e auditoria de dependências passaram; artifacts [JaCoCo](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43541031194/artifacts/14092062221) e [BDD](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43541031194/artifacts/14091987167) publicados | 71 testes agregados, 0 falhas/erros, 48 ignorados; Cucumber: 91 cenários, 88 ignorados. `PostgresContainerTest` foi ignorado porque o runner não encontrou Docker; CI não valida PostgreSQL. Branch ainda não integrada e não há tag |
| Smoke de desempenho local | 10 workers autenticados; GET `/api/recursos`; 20 salas sintéticas no H2; 10 s | 2.440 respostas HTTP 200, 0 falhas; p50 28,89 ms, p95 107,43 ms, p99 197,66 ms, max. 438,29 ms | Apenas smoke de API local; não valida a meta de 2.000 usuários, operações de escrita nem tempo de renderização |
| CI público baseline | [Run #43523400331](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43523400331) no SHA `4a92eb9c18f5a8ab1bb086efc17dc7858adafaaa` | Verde para aquele SHA | Não valida alterações locais nem é CI da tag candidata |

## Cobertura e qualidade de testes

O relatório BDD rastreia 14/14 RFs e 22/22 RNFs em tags/cenários, mas somente **3 de 59** cenários estão marcados executáveis. A execução Cucumber expandiu exemplos em 91 cenários e reportou 88 ignorados. Nenhuma dessas contagens equivale à cobertura de código. O relatório JaCoCo da verificação local mais recente mediu **54,64% de cobertura de instruções** (2.457 cobertas de 4.497 instruções); o artifact correspondente foi publicado no CI da branch candidata. Não há meta de cobertura configurada; esse número não constitui aprovação.

O teste concorrente integrado em H2 confirma que no máximo uma das duas solicitações simultâneas para o mesmo recurso é persistida. O CI tentou executar PostgreSQL/Testcontainers, mas `PostgresContainerTest` foi ignorado por falta de ambiente Docker válido; portanto a migração e consulta ainda precisam de validação PostgreSQL.

O relatório de utilização foi otimizado para filtrar reservas sobrepostas no banco, carregar recursos associados na consulta e agregar em uma passagem; foi criado um índice V2 para status/início/fim e teste de integração com período e totais conhecidos. O teste direcionado e a suíte Maven final passaram em H2. A migração e consulta ainda precisam de validação PostgreSQL no CI.

## Segurança, desempenho e ambiente

- Login JWT e controle por perfil são implementados; há testes de autenticação inválida, token obrigatório e autorização. Falta cobertura completa por endpoint/perfil e revisão de segurança independente.
- SonarCloud/Quality Gate não foi comprovado. A consulta de secrets/variables foi negada com HTTP 403; o estado de credenciais não é conhecido.
- A carga de 2.000 usuários não foi executada. Consulte `performance/plano-01.md` e `performance/resultado-01.md`.
- Neon não provisiona usuário inicial por decisão de escopo; o processo privado de provisionamento continua necessário para disponibilizar produção.
- O grupo foi informado como composto por uma única integrante; o documento de decisão registra o nome e a contribuição declarada, sujeitos à confirmação final da própria integrante.

## Rastreamento de defeitos e riscos

Ver `defeitos/registro-01.md`. A evidência precisa ser refeita no commit integrado; nenhum risco está formalmente aceito ou atribuído neste rascunho.
