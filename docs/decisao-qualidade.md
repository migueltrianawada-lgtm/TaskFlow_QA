# Decisão de qualidade — rascunho não assinado

> Grupo composto por uma única integrante, conforme informado pela responsável pelo projeto. A decisão abaixo continua sendo um rascunho até a revisão e confirmação final da integrante.

## Decisão atual

**Bloquear a aprovação plena da release candidate neste momento.** Os [PRs #3](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/pull/3), [#4](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/pull/4) e [#5](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/pull/5) foram integrados. A `main` está no SHA `18c267cdef5e7af318b201a5132e8e22f5b6ef01`, com [CI verde](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43553788543). O ensaio exploratório local de 2.000 usuários não atingiu o p95 de disponibilidade de 2 s e não mediu a jornada renderizada nem a carga completa de operações. SonarCloud não foi configurado/executado por falta de token e configuração do projeto; PostgreSQL/Testcontainers permanece deliberadamente ignorado conforme orientação da responsável. Ainda não há tag.

## Critérios e evidências

| Critério | Evidência disponível | Decisão |
|---|---|---|
| Reprodutibilidade em clone limpo | Baseline auditado `4a92eb9`; alterações integradas na `main` em `18c267c` | A CI da `main` passou; ainda falta clone limpo independente do SHA atual |
| Requisitos funcionais e testes | Código local, RTM e testes locais; BDD: 3/59 executáveis | Parcial; não liberar |
| CI no commit avaliado | [Run #43553788543](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43553788543) verde no SHA `18c267cdef5e7af318b201a5132e8e22f5b6ef01`; artifacts JaCoCo e BDD publicados | PRs #3, #4 e #5 integrados; sem tag. Testcontainers foi ignorado a pedido da responsável porque Docker não está disponível localmente nem no runner |
| Cobertura JaCoCo | [Artifact da CI na main](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43553788543/artifacts/14097351626) | Disponível para o SHA atual da `main`; não equivale a aprovação de cobertura |
| SonarCloud Quality Gate | Workflow sem etapa Sonar; secrets/variables de Actions e variáveis de ambiente Sonar não disponíveis | Não executado; falta configurar projeto e fornecer token autorizado |
| Desempenho | Ensaio exploratório de 2.000 usuários, 350.614 consultas estáveis, 0 falhas, p95 2.688,28 ms | Não atende RNF-03/RNF-05: p95 excede 2 s; API local em H2, sem frontend nem perfil completo. Ver `docs/performance/resultado-01.md` |
| Banco de produção e acesso inicial | Perfis H2/PostgreSQL documentados; Neon sem provisionamento inicial de Administrador | Risco operacional aberto; produção não demonstrada |
| Segurança operacional | JWT e autorização implementados, mas revisão independente, HTTPS do deploy e análise Sonar não comprovados | Parcial; não afirmar prontidão para produção |

## Riscos e responsáveis

| Risco | Impacto | Tratamento/aceite | Responsável |
|---|---|---|---|
| p95 de disponibilidade excede o limite no ensaio local | A meta de latência não foi atingida neste perfil e a evidência não representa produção | Não aceito; investigar e repetir em ambiente isolado, medindo frontend, operações previstas e métricas do ambiente | Miguel Palacios — único integrante |
| Sem procedimento privado para criar primeiro Administrador no Neon | Usuários de produção não conseguem acessar o sistema | Não aceito; definir e testar procedimento seguro antes de produção | Miguel Palacios — único integrante |
| Testcontainers/PostgreSQL deliberadamente ignorado localmente e no CI do PR | Diferenças entre H2 e PostgreSQL podem não ser detectadas | Conforme orientação da responsável, manter ignorado; Docker indisponível localmente e runner sem ambiente Docker válido. Não declarar PostgreSQL validado | Miguel Palacios — único integrante |
| SonarCloud/Quality Gate sem configuração disponível | Não há análise estática/Sonar publicada para o SHA | Não executado; requer projeto e credencial autorizada. A inspeção encontrou zero secrets/variables de Actions e nenhuma configuração Sonar no workflow | Miguel Palacios — único integrante |

## Declaração do grupo

| Integrante (nome completo) | Contribuição confirmada | Declaração |
|---|---|---|
| Miguel Palacios (único integrante) | Requisitos e condução do projeto; revisão e validação da implementação e da documentação com apoio do Copilot | Declaração individual pendente de revisão/aceite final |

## Próxima revisão

Reavaliar a decisão apenas quando a tag candidata apontar para um commit da `main` com CI verde, artifact JaCoCo recuperável, links de evidência válidos, limitações de carga/produção explicitadas e a responsável confirmar a decisão. Nenhum envio ao Hub está registrado por este documento.
