# language: pt

@qualidade
Funcionalidade: Verificar atributos não funcionais do TaskFlow
  Para confirmar qualidade, segurança e desempenho
  Como equipe responsável pela validação
  Quero verificar os critérios não funcionais em condições controladas

  @RNF-01 @RF-01 @RF-14
  Cenário: Demonstrar cobertura de aceitação para fluxos funcionais
    Dado que cada requisito funcional RF-01 a RF-14 possui cenários de aceitação rastreáveis
    Quando a suíte de aceitação é executada contra os fluxos implementados
    Então cada cenário apresenta resultado esperado
    E nenhum fluxo informa sucesso sem estado consistente

  @RNF-02 @RF-01 @RF-13
  Cenário: Refletir estado de sucesso ou rejeição no frontend
    Dado que o servidor responde com sucesso, rejeição de regra ou falha recuperável
    Quando o frontend processa a resposta
    Então apresenta o mesmo resultado funcional sem criar sucesso falso

  @RNF-03 @RF-03 @RF-04 @RF-09 @RF-12 @carga-controlada
  Cenário: Sustentar carga de dez mil usuários simultâneos autenticados
    Dado que o ambiente controlado possui dados e perfil de carga documentados
    Quando dez mil usuários simultâneos executam operações representativas pelo frontend
    Então pelo menos 95 por cento das consultas de disponibilidade respondem em até 2 segundos
    E pelo menos 95 por cento das demais operações interativas respondem em até 3 segundos
    E a taxa de erros técnicos e timeouts é inferior a 1 por cento
    E o relatório registra p50, p95, p99, duração, distribuição de carga e erros

  @RNF-04 @RF-04 @RF-05 @RF-06 @concorrencia
  Cenário: Garantir no máximo uma alocação incompatível sob concorrência
    Dado que várias solicitações disputam o mesmo recurso e intervalo simultaneamente
    Quando o sistema processa as operações concorrentes
    Então no máximo uma alocação incompatível é confirmada
    E as demais são rejeitadas sem alocação parcial

  @RNF-05 @RF-03 @RF-12 @carga-controlada
  Cenário: Apresentar relatórios dentro da meta de desempenho
    Dado que dez mil usuários simultâneos executam o perfil de carga definido
    Quando Responsável e Administrador solicitam relatórios com filtros
    Então pelo menos 95 por cento dos relatórios são apresentados em até 5 segundos
    E cada resultado respeita os filtros informados

  @RNF-06 @RF-13 @compatibilidade
  Esquema do Cenário: Preservar fluxos nos navegadores suportados
    Dado que o frontend é aberto na versão estável vigente de <navegador>
    Quando o usuário executa os fluxos principais
    Então nenhuma diferença impede o uso funcional
    E a versão do navegador fica registrada na validação

    Exemplos:
      | navegador |
      | Chrome    |
      | Firefox   |
      | Edge      |
      | Safari    |

  @RNF-07 @RF-04 @RF-07 @RF-11
  Cenário: Preservar estado da reserva quando notificação fica indisponível
    Dado que uma reserva válida foi persistida
    E o serviço externo de notificação está indisponível
    Quando o adaptador tenta notificar
    Então a reserva permanece no estado persistido
    E a falha de entrega é registrada
    E o frontend não apresenta a reserva como falha de agendamento

  @RNF-08 @RF-13 @responsividade
  Esquema do Cenário: Manter interface utilizável nos viewports definidos
    Dado que a página é exibida em viewport de <largura> pixels
    Quando o usuário percorre o fluxo correspondente ao perfil
    Então não há sobreposição de conteúdo ou ações
    E não há rolagem horizontal da página

    Exemplos:
      | largura |
      | 360     |
      | 768     |
      | 1280    |

  @RNF-09 @RF-13
  Cenário: Exibir mensagem compreensível para falha recuperável
    Dado que ocorre uma rejeição de validação ou falha recuperável
    Quando o frontend exibe a resposta
    Então explica o problema e indica uma ação possível
    E oculta detalhes técnicos internos

  @RNF-10 @RF-04 @RF-07 @RF-08 @RF-13
  Cenário: Comunicar estados e ações também por texto
    Dado que uma tela apresenta status ou ação de reserva
    Quando o usuário identifica seu significado
    Então há rótulo textual compreensível além de cor ou ícone

  @RNF-11 @RF-04 @RF-05 @RF-06 @RF-08 @RF-09
  Cenário: Reverter integralmente operação após falha controlada
    Dado que uma operação crítica falha durante uma etapa de persistência
    Quando o sistema encerra a tentativa
    Então não há registros ou alocações parciais
    E a disponibilidade final permanece consistente

  @RNF-12 @RF-04 @RF-10 @RF-11
  Cenário: Registrar falha de notificação sem desfazer negócio
    Dado que uma operação válida foi confirmada
    Quando o adaptador de notificação falha
    Então a operação de negócio permanece confirmada
    E a falha da notificação pode ser consultada

  @RNF-13 @RF-04 @RF-07 @RF-08 @RF-09 @RF-10
  Cenário: Associar auditoria a cada mudança relevante
    Dado que um usuário autorizado conclui uma mudança relevante
    Quando a operação é persistida
    Então o evento de auditoria contém usuário, ação, entidade e data e hora
    E um usuário sem permissão não consegue alterar o histórico

  @RNF-14 @RF-01 @seguranca
  Cenário: Não persistir senha em texto simples
    Dado que uma conta é criada ou tem sua credencial atualizada
    Quando os dados de autenticação são persistidos
    Então somente material de verificação protegido é armazenado
    E a senha original não pode ser recuperada do armazenamento

  @RNF-15 @RF-01 @RF-12 @seguranca
  Cenário: Verificar autorização no servidor em operação protegida
    Dado que um usuário possui identidade e perfil conhecidos
    Quando executa uma operação permitida ou proibida ao seu perfil
    Então o servidor autoriza ou nega conforme perfil e escopo
    E uma operação negada não altera dados

  @RNF-16 @RF-02 @RF-12 @seguranca
  Cenário: Rejeitar entrada inválida e manter consultas parametrizadas
    Dado que a solicitação contém entrada fora do contrato
    Quando o servidor valida e consulta a persistência
    Então rejeita os dados inválidos
    E a entrada não altera o significado da consulta ou comando persistente

  @RNF-17 @RF-01 @RF-14 @seguranca
  Cenário: Proteger comunicação e credenciais de sessão em produção
    Dado que o sistema está configurado no ambiente de produção controlado
    Quando o frontend comunica dados de sessão com o servidor
    Então os dados trafegam por canal protegido
    E os atributos de segurança da sessão correspondem à configuração adotada

  @RNF-18 @RF-01 @RF-14 @seguranca
  Cenário: Não expor informação sensível em respostas
    Dado que ocorre erro ou rejeição em operação protegida
    Quando o frontend recebe a resposta do servidor
    Então a resposta não contém credenciais, caminhos internos ou detalhes de implementação
    E somente apresenta dados pessoais necessários ao fluxo autorizado

  @RNF-19 @RF-01 @RF-14 @manutenibilidade
  Cenário: Preservar separação entre frontend, controller, domínio e persistência
    Dado que uma alteração é solicitada em uma camada
    Quando a revisão arquitetural e os testes de camada são executados
    Então responsabilidades permanecem separadas
    E não há dependência não relacionada nas demais camadas

  @RNF-20 @RF-01 @RF-04 @RF-10 @testes
  Esquema do Cenário: Verificar regras críticas com testes automatizados
    Dado que a regra de <regra> tem casos permitidos, rejeitados e concorrentes aplicáveis
    Quando a suíte automatizada correspondente é executada
    Então os resultados cobrem cada caso definido

    Exemplos:
      | regra                          |
      | conflito de horário            |
      | autorização por perfil         |
      | bloqueio de manutenção         |
      | disponibilidade de materiais  |

  @RNF-21 @RF-14 @documentacao
  Cenário: Manter documentação dos contratos públicos atualizada
    Dado que um endpoint ou fluxo público foi alterado
    Quando a revisão da documentação é concluída
    Então finalidade, entrada, resposta, erros e autorização estão descritos
    E a documentação corresponde à versão vigente do contrato

  @RNF-22 @RF-01 @RF-14 @portabilidade
  Esquema do Cenário: Executar aplicação sem alterar código entre ambientes
    Dado que a aplicação está instalada no ambiente <ambiente>
    Quando a configuração própria do ambiente é carregada
    Então a aplicação inicia sem alteração do código-fonte
    E a suíte de fumaça dos fluxos principais pode ser executada

    Exemplos:
      | ambiente             |
      | desenvolvimento      |
      | teste controlado     |
      | produção              |
