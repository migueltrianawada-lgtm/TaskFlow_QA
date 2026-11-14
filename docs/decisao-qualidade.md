# Decisão de qualidade — rascunho não assinado

> Grupo composto por uma única integrante, conforme informado pela responsável pelo projeto. A decisão abaixo continua sendo um rascunho até a revisão e confirmação final da integrante.

## Decisão atual

**Bloquear a aprovação da release candidate neste momento.** O [PR #3](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/pull/3) está aberto e mergeável, com CI verde no SHA `766354d3fb68c2893be84f7f625ead19b235bc00`, mas sem decisão de revisão registrada e sem integração à `main`. O teste de 2.000 usuários não foi executado por falta de ambiente isolado e recursos disponíveis. PostgreSQL/Testcontainers permanece deliberadamente ignorado conforme orientação da responsável: Docker não está disponível localmente e o runner do CI não apresentou ambiente Docker válido. A decisão deve ser reavaliada após as pendências aplicáveis serem tratadas e as evidências finais vinculadas ao mesmo SHA da tag.

## Critérios e evidências

| Critério | Evidência disponível | Decisão |
|---|---|---|
| Reprodutibilidade em clone limpo | Baseline `main` `4a92eb9` reproduz Maven, frontend e smoke, mas não contém as alterações locais | Não atende ao candidato atual |
| Requisitos funcionais e testes | Código local, RTM e testes locais; BDD: 3/59 executáveis | Parcial; não liberar |
| CI no commit avaliado | [Run #43541559873](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43541559873) verde no SHA `766354d3fb68c2893be84f7f625ead19b235bc00`; artifacts [JaCoCo](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43541559873/artifacts/14091492854) e [BDD](https://github.com/migueltrianawada-lgtm/TaskFlow_QA/actions/runs/43541559873/artifacts/14092202072) | PR #3 ainda aberto; falta revisão e CI no SHA integrado/tag. Testcontainers foi ignorado a pedido da responsável porque Docker está indisponível localmente e o runner não encontrou ambiente Docker válido |
| Cobertura JaCoCo | Artifact JaCoCo publicado no CI do PR #3 | Disponível para candidato; falta conferir no SHA integrado/tag |
| SonarCloud Quality Gate | Não há link ou resultado confirmado; consulta de secrets/variables recebeu HTTP 403 | Pendente, sem inferir ausência de configuração |
| Desempenho | Smoke local com 10 workers e 2.440 leituras; teste de 2.000 usuários não executado | Não atende/comprova RNF-03/RNF-05; host compartilhado tinha ~0,8 GiB de memória livre e não havia executor isolado. Motivo documentado em `docs/performance/resultado-01.md` |
| Banco de produção e acesso inicial | Perfis H2/PostgreSQL documentados; Neon sem provisionamento inicial de Administrador | Risco operacional aberto; produção não demonstrada |
| Segurança operacional | JWT e autorização implementados, mas revisão independente, HTTPS do deploy e análise Sonar não comprovados | Parcial; não afirmar prontidão para produção |

## Riscos e responsáveis

| Risco | Impacto | Tratamento/aceite | Responsável |
|---|---|---|---|
| Meta de 2.000 usuários não medida | Capacidade e latência do serviço desconhecidas; pode impedir uso pretendido | Não aceito; executar teste em ambiente controlado ou registrar decisão formal da responsável | Miguel Palacios — único integrante |
| Sem procedimento privado para criar primeiro Administrador no Neon | Usuários de produção não conseguem acessar o sistema | Não aceito; definir e testar procedimento seguro antes de produção | Miguel Palacios — único integrante |
| Testcontainers/PostgreSQL deliberadamente ignorado localmente e no CI do PR | Diferenças entre H2 e PostgreSQL podem não ser detectadas | Conforme orientação da responsável, manter ignorado; Docker indisponível localmente e runner sem ambiente Docker válido. Não declarar PostgreSQL validado | Miguel Palacios — único integrante |
| Configuração SonarCloud/Quality Gate desconhecida | Qualidade estática/dependências não têm análise publicada | Sem acesso confirmado para verificar configuração; consulta de secrets/variables retornou HTTP 403. Registrar como não verificado, sem inferir ausência | Miguel Palacios — único integrante |

## Declaração do grupo

| Integrante (nome completo) | Contribuição confirmada | Declaração |
|---|---|---|
| Miguel Palacios (único integrante) | Requisitos e condução do projeto; revisão e validação da implementação e da documentação com apoio do Copilot | Declaração individual pendente de revisão/aceite final |

## Próxima revisão

Reavaliar a decisão apenas quando a tag candidata apontar para um commit da `main` com CI verde, artifact JaCoCo recuperável, links de evidência válidos, limitações de carga/produção explicitadas e a responsável confirmar a decisão. Nenhum envio ao Hub está registrado por este documento.
