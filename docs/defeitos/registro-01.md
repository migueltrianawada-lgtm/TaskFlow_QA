# Registro inicial de defeitos e lacunas de evidência

Este registro separa defeitos funcionais de pendências de entrega. Os itens referem-se ao candidato enviado à branch `miguelpalacios1987-documentacao-projeto` e à auditoria do `main`; reavaliar no commit integrado final.

| ID | Tipo / prioridade | Descrição e impacto | Situação / evidência | Responsável |
|---|---|---|---|---|
| D-01 | Defeito de entrega / Alta | O `main` auditado em `4a92eb9` não continha os fluxos funcionais e README atualizados desta branch; a versão pública não corresponde ao candidato. | Candidato commitado/enviado (`1a623c7`), CI da branch verde; requer revisão, integração por PR e CI na `main`. | Pendente de atribuição pelo grupo |
| E-01 | Evidência / Alta | Apenas 3 de 59 cenários BDD estão marcados executáveis; várias funcionalidades não têm teste de aceitação demonstrativo. | `docs/testes-bdd/relatorio-cobertura.md`; suíte local reportou 48 testes ignorados no total, incluindo os cenários BDD não executáveis. | Pendente de atribuição pelo grupo |
| E-02 | Evidência / Alta | Teste PostgreSQL/Testcontainers não foi executado, pois localmente e no CI da branch não havia ambiente Docker válido. | `mvnw verify` e [run #43541031194](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43541031194) passaram com `PostgresContainerTest` ignorado. | Pendente de atribuição pelo grupo |
| E-03 | Evidência / Alta | Meta revisada de 2.000 usuários e limites RNF-03/RNF-05 não foram medidos. | `docs/performance/resultado-01.md`; sem relatório de carga. | Miguel Palacios — único integrante |
| E-04 | Evidência / Média | SonarCloud/Quality Gate não tem configuração ou link confirmado; a consulta de secrets/variables falhou com HTTP 403, portanto a existência de credenciais não é conhecida. | Requer confirmar configuração e executar análise no commit candidato; não declarar serviço indisponível sem verificação autorizada. | Pendente de atribuição pelo grupo |
| E-05 | Evidência / Média | Revisão por pares não foi comprovada nas PRs auditadas. | PRs #1/#2 foram integradas; a auditoria não encontrou registro de revisão submetida. | Pendente de atribuição pelo grupo |
| R-01 | Risco operacional / Alta | Neon não cria Administrador automaticamente; sem procedimento privado de provisionamento, usuários não conseguem iniciar sessão em produção. | Decisão de escopo documentada no README; ambiente de produção não validado. Não é uma falha de login do perfil local. | Pendente de decisão e responsável do grupo |

Nenhum defeito foi reclassificado como corrigido na `main`. A atualização do status depende de revisão e execução no commit integrado.
