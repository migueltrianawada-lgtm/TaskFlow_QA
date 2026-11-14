# Matriz de rastreabilidade

## Escopo e interpretação

Esta matriz relaciona os requisitos funcionais com código, testes e evidências da versão integrada à `main` no SHA `18c267cdef5e7af318b201a5132e8e22f5b6ef01`. “Implementado” não significa que o requisito tenha cobertura automatizada suficiente ou esteja comprovado em produção. A cobertura BDD é rastreabilidade documental, não cobertura de código.

| Requisito | Implementação principal | Verificação disponível | Estado da evidência |
|---|---|---|---|
| RF-01 — autenticação e autorização por perfil | `backend/src/main/java/com/taskflow/qa/api/AuthController.java`; `backend/src/main/java/com/taskflow/qa/security/` | `HealthControllerTest`: login válido/inválido, Bearer obrigatório e controle de perfil | Parcial; cenários automatizados selecionados passam, não há matriz completa de todos os endpoints e perfis |
| RF-02 — cadastro e consulta de recursos | `api/RecursoController.java`; `application/RecursoServiceImpl.java`; `api/GestaoController.java` | Testes de autorização e API; cenários BDD em `docs/testes-bdd/` | Parcial; fluxos administrativos não têm cobertura de aceitação completa |
| RF-03 — pesquisa por atributos e disponibilidade | `api/RecursoController.java`; `application/RecursoServiceImpl.java` | Especificação BDD e consulta autenticada | Parcial; filtros/disponibilidade não foram validados integralmente por teste de aceitação |
| RF-04 — criar, alterar e cancelar reservas | `api/ReservaController.java`; `application/ReservaServiceImpl.java` | `HealthControllerTest`: criação e rejeição de sobreposição; teste de concorrência | Parcial; aprovação, atualização, cancelamento e persistência PostgreSQL precisam de evidência dedicada |
| RF-05 — sobreposição de recursos e docente | `application/ReservaServiceImpl.java`; `persistence/repository/ReservaRepository.java` | Testes de sobreposição de reserva | Parcial; conflitos de agenda docente e todos os tipos de recurso não estão cobertos de ponta a ponta |
| RF-06 — impedir dupla reserva simultânea | `application/ReservaServiceImpl.java`; bloqueio pessimista em `RecursoRepository.java` | `simultaneousRequestsForOneResourceConfirmAtMostOneReservation` | Verificado em teste integrado H2; PostgreSQL/Testcontainers segue ignorado por decisão do grupo: Docker indisponível localmente e runner do CI sem ambiente Docker válido |
| RF-07 — aprovação de recurso restrito | `api/ReservaController.java`; `application/ReservaServiceImpl.java` | Cenários BDD especificados | Parcial; falta teste de aceitação de aprovação/recusa e autorização por perfil |
| RF-08 — bloqueio de manutenção | `api/GestaoController.java`; `persistence/repository/BloqueioManutencaoRepository.java`; `application/ReservaServiceImpl.java` | Cenários BDD especificados | Parcial; falta validação integrada de bloqueio e conflito |
| RF-09 — retirada e devolução de materiais | `api/GestaoController.java`; entidade e repositório de movimentações | Cenários BDD especificados | Parcial; falta teste de ponta a ponta dos limites de retirada/devolução |
| RF-10 — histórico auditável | `application/AuditoriaService.java`; `api/GestaoController.java` | Auditoria é exercitada junto a operações, mas não há teste dedicado de completude/imutabilidade | Parcial |
| RF-11 — notificações | `application/NotificacaoService.java`; `api/GestaoController.java` | Implementação simulada persistida; cenários BDD especificados | Parcial; integração externa não está configurada nem é requisito da versão demonstrativa |
| RF-12 — relatório de utilização/carga/conflitos | `api/GestaoController.java`, endpoint `/api/relatorios/utilizacao`; consulta filtrada, associação carregada e agregação em passagem única | `utilizationReportFiltersAndAggregatesReservationsInRequestedPeriod` | Parcial; cálculo básico verificado em H2; falta validação PostgreSQL e carga |
| RF-13 — responsividade e mensagens compreensíveis | `frontend/src/App.jsx`; `backend/src/main/java/com/taskflow/qa/api/ApiExceptionHandler.java` | Testes automatizados do frontend e build | Parcial; não há evidência de viewport/navegadores nem validação manual documentada |
| RF-14 — documentação da API/fluxos | `backend/src/main/java/com/taskflow/qa/api/OpenApiConfig.java`; `README.md`; Swagger | OpenAPI/Swagger e contratos descritos no README | Parcial; revisar documentação contra o commit integrado |

## Requisitos não funcionais

| Requisito | Evidência disponível | Lacuna/limite |
|---|---|---|
| RNF-01/02 — adequação funcional e mensagens | Testes backend/frontend; RTM acima | Não há aceitação completa dos RFs nem validação de cada falha de integração |
| RNF-03/05 — desempenho sob carga | Ensaio exploratório local de 2.000 usuários: 350.614 consultas estáveis, 0 falhas, p95 2.688,28 ms; `docs/performance/resultado-01.md` | Não aprovado: p95 excede 2 s. Medição somente HTTP de disponibilidade em H2; sem jornada frontend, operações de escrita/relatório ou métricas de CPU/memória/banco. Não representa produção nem valida PostgreSQL |
| RNF-04/11 — concorrência e confiabilidade | Teste concorrente H2; transações no serviço | Teste PostgreSQL/Testcontainers deliberadamente ignorado conforme decisão do grupo; falta também teste controlado de falha |
| RNF-06/08/10 — compatibilidade e responsividade | Build e testes automatizados frontend | Browsers e viewports definidos ainda não foram testados |
| RNF-07/12 — comportamento de notificações | Adaptador simulado e persistência local | Indisponibilidade/integração externa não tem teste dedicado |
| RNF-09/13/18 — usabilidade, auditoria e exposição | Mensagens de erro e gravação de auditoria implementadas | Falta revisão sistemática por perfil e evidência de acessibilidade |
| RNF-14/15/16/17 — segurança | Hash de senha, JWT, autorização por endpoint e validação | Falta revisão independente, verificação de HTTPS em deploy e análise SonarCloud |
| RNF-19/21/22 — manutenção, contratos e portabilidade | Organização de pacotes, Swagger, perfis H2/PostgreSQL e README | Falta execução em ambiente PostgreSQL real e validação de contrato/navegadores |

## Fontes

- Requisitos: `requisitos-funcionais.md` e `requisitos-nao-funcionais.md`.
- Testes BDD: `testes-bdd/relatorio-cobertura.md` e `testes-bdd/*.feature`.
- Testes de API: `../backend/src/test/java/com/taskflow/qa/api/HealthControllerTest.java`.
- Resultado de qualidade: `relatorio-qualidade.md`.
