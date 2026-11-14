# Decisão de qualidade — rascunho não assinado

> Grupo composto por uma única integrante, conforme informado pela responsável pelo projeto. A decisão abaixo continua sendo um rascunho até a revisão e confirmação final da integrante.

## Decisão atual

**Bloquear a entrega da release candidate neste momento.** O candidato foi commitado e enviado à branch de trabalho, e o CI dessa branch terminou verde; ainda não há integração à `main`, tag, validação PostgreSQL/Testcontainers ou teste de carga de 2.000 usuários. A decisão deve ser reavaliada após as evidências finais serem vinculadas ao mesmo SHA da tag.

## Critérios e evidências

| Critério | Evidência disponível | Decisão |
|---|---|---|
| Reprodutibilidade em clone limpo | Baseline `main` `4a92eb9` reproduz Maven, frontend e smoke, mas não contém as alterações locais | Não atende ao candidato atual |
| Requisitos funcionais e testes | Código local, RTM e testes locais; BDD: 3/59 executáveis | Parcial; não liberar |
| CI no commit avaliado | [Run #43541031194](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43541031194) verde na branch candidata, SHA `1a623c785daaabbdb1f5ba29b0da7d50785f410f`; artifacts [JaCoCo](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43541031194/artifacts/14092062221) e [BDD](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43541031194/artifacts/14091987167) | Candidato validado em CI; falta executar no SHA integrado/tag. Testcontainers foi ignorado por ausência de Docker válido no runner |
| Cobertura JaCoCo | Artifact JaCoCo publicado no CI da branch candidata | Disponível para candidato; falta conferir no SHA integrado/tag |
| SonarCloud Quality Gate | Não há link ou resultado confirmado; consulta de secrets/variables recebeu HTTP 403 | Pendente, sem inferir ausência de configuração |
| Desempenho | Smoke local com 10 workers e 2.440 leituras; teste de 2.000 usuários não executado | Não atende/comprova RNF-03/RNF-05; smoke limitado descrito em `docs/performance/resultado-01.md` |
| Banco de produção e acesso inicial | Perfis H2/PostgreSQL documentados; Neon sem provisionamento inicial de Administrador | Risco operacional aberto; produção não demonstrada |
| Segurança operacional | JWT e autorização implementados, mas revisão independente, HTTPS do deploy e análise Sonar não comprovados | Parcial; não afirmar prontidão para produção |

## Riscos e responsáveis

| Risco | Impacto | Tratamento/aceite | Responsável |
|---|---|---|---|
| Meta de 2.000 usuários não medida | Capacidade e latência do serviço desconhecidas; pode impedir uso pretendido | Não aceito; executar teste em ambiente controlado ou registrar decisão formal da responsável | Miguel Palacios — único integrante |
| Sem procedimento privado para criar primeiro Administrador no Neon | Usuários de produção não conseguem acessar o sistema | Não aceito; definir e testar procedimento seguro antes de produção | Miguel Palacios — único integrante |
| Testcontainers/PostgreSQL ignorado localmente e no CI da branch | Diferenças entre H2 e PostgreSQL podem não ser detectadas | Pendente; disponibilizar Docker compatível no runner e confirmar teste executado | Miguel Palacios — único integrante |
| Configuração SonarCloud/Quality Gate desconhecida | Qualidade estática/dependências não têm análise publicada | Confirmar acesso/configuração e registrar resultado | Miguel Palacios — único integrante |

## Declaração do grupo

| Integrante (nome completo) | Contribuição confirmada | Declaração |
|---|---|---|
| Miguel Palacios (único integrante) | Requisitos e condução do projeto; revisão e validação da implementação e da documentação com apoio do Copilot | Declaração individual pendente de revisão/aceite final |

## Próxima revisão

Reavaliar a decisão apenas quando a tag candidata apontar para um commit da `main` com CI verde, artifact JaCoCo recuperável, links de evidência válidos, limitações de carga/produção explicitadas e a responsável confirmar a decisão. Nenhum envio ao Hub está registrado por este documento.
