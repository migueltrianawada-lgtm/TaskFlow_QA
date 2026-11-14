# Decisão de qualidade — rascunho não assinado

> Grupo composto por uma única integrante, conforme informado pela responsável pelo projeto. A decisão abaixo continua sendo um rascunho até a revisão e confirmação final da integrante.

## Decisão atual

**Bloquear a entrega da release candidate neste momento.** O estado local contém implementação e documentação em preparação, mas não há commit integrado/tag com CI verde que comprove esse conteúdo. A decisão deve ser reavaliada após todas as evidências serem vinculadas ao mesmo SHA da tag.

## Critérios e evidências

| Critério | Evidência disponível | Decisão |
|---|---|---|
| Reprodutibilidade em clone limpo | Baseline `main` `4a92eb9` reproduz Maven, frontend e smoke, mas não contém as alterações locais | Não atende ao candidato atual |
| Requisitos funcionais e testes | Código local, RTM e testes locais; BDD: 3/59 executáveis | Parcial; não liberar |
| CI no commit avaliado | Run #43523400331 verde apenas no baseline, SHA `4a92eb9` | Pendente para candidato/tag |
| Cobertura JaCoCo | Plugin adicionado e artifact configurado no workflow; nenhum CI do candidato publicou artifact | Pendente |
| SonarCloud Quality Gate | Não há link ou resultado confirmado; consulta de secrets/variables recebeu HTTP 403 | Pendente, sem inferir ausência de configuração |
| Desempenho | Smoke local com 10 workers e 2.440 leituras; teste de 2.000 usuários não executado | Não atende/comprova RNF-03/RNF-05; smoke limitado descrito em `docs/performance/resultado-01.md` |
| Banco de produção e acesso inicial | Perfis H2/PostgreSQL documentados; Neon sem provisionamento inicial de Administrador | Risco operacional aberto; produção não demonstrada |
| Segurança operacional | JWT e autorização implementados, mas revisão independente, HTTPS do deploy e análise Sonar não comprovados | Parcial; não afirmar prontidão para produção |

## Riscos e responsáveis

| Risco | Impacto | Tratamento/aceite | Responsável |
|---|---|---|---|
| Meta de 2.000 usuários não medida | Capacidade e latência do serviço desconhecidas; pode impedir uso pretendido | Não aceito; executar teste em ambiente controlado ou registrar decisão formal da responsável | Miguel Palacios — único integrante |
| Sem procedimento privado para criar primeiro Administrador no Neon | Usuários de produção não conseguem acessar o sistema | Não aceito; definir e testar procedimento seguro antes de produção | Miguel Palacios — único integrante |
| Testcontainers/PostgreSQL não executado localmente | Diferenças entre H2 e PostgreSQL podem não ser detectadas | Pendente; executar no CI/Docker ou ambiente equivalente | Miguel Palacios — único integrante |
| Configuração SonarCloud/Quality Gate desconhecida | Qualidade estática/dependências não têm análise publicada | Confirmar acesso/configuração e registrar resultado | Miguel Palacios — único integrante |

## Declaração do grupo

| Integrante (nome completo) | Contribuição confirmada | Declaração |
|---|---|---|
| Miguel Palacios (único integrante) | Requisitos e condução do projeto; revisão e validação da implementação e da documentação com apoio do Copilot | Declaração individual pendente de revisão/aceite final |

## Próxima revisão

Reavaliar a decisão apenas quando a tag candidata apontar para um commit da `main` com CI verde, artifact JaCoCo recuperável, links de evidência válidos, limitações de carga/produção explicitadas e a responsável confirmar a decisão. Nenhum envio ao Hub está registrado por este documento.
