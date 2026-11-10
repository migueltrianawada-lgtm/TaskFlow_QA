# language: pt

@aceitacao
Funcionalidade: Interface e documentação dos fluxos públicos
  Para usar o sistema em diferentes dispositivos e integrar seus fluxos documentados
  Como usuário ou consumidor de um fluxo público
  Quero uma interface responsiva e contratos atualizados

  @RF-13 @RNF-02 @RNF-06 @RNF-08
  Esquema do Cenário: Renderizar fluxo principal em viewport responsivo
    Dado que o usuário abre o frontend em um viewport de <largura> pixels
    Quando acessa um fluxo principal permitido ao seu perfil
    Então conteúdo e ações permanecem visíveis e utilizáveis
    E a página não apresenta rolagem horizontal

    Exemplos:
      | largura |
      | 360     |
      | 768     |
      | 1280    |

  @RF-13 @RNF-06
  Esquema do Cenário: Executar fluxo principal em navegador suportado
    Dado que o usuário acessa o frontend no navegador <navegador>
    Quando executa um fluxo principal autorizado
    Então o fluxo pode ser concluído sem perda de funcionalidade

    Exemplos:
      | navegador |
      | Chrome    |
      | Firefox   |
      | Edge      |
      | Safari    |

  @RF-13 @RNF-02 @RNF-09 @RNF-18
  Cenário: Apresentar erro recuperável com orientação segura
    Dado que uma operação falha por validação ou regra de negócio
    Quando o frontend recebe o resultado do servidor
    Então informa o que ocorreu e qual ação o usuário pode tomar
    E não apresenta stack trace, caminhos internos ou dados sensíveis

  @RF-13 @RNF-10
  Cenário: Identificar estado de reserva sem depender apenas de cor ou ícone
    Dado que o usuário consulta uma reserva pendente, aprovada, recusada ou cancelada
    Quando a interface apresenta o estado
    Então o estado é identificado por texto compreensível
    E as ações disponíveis são identificáveis por texto

  @RF-13 @RNF-02 @RNF-11
  Cenário: Não indicar sucesso quando a persistência falha
    Dado que o frontend envia uma operação
    E a persistência rejeita ou não confirma a operação
    Quando o frontend recebe a resposta
    Então não apresenta a operação como concluída
    E apresenta o estado real e uma orientação segura

  @RF-14 @RNF-19 @RNF-21 @RNF-22
  Cenário: Manter contrato público documentado junto ao fluxo
    Dado que um fluxo público ou endpoint foi criado ou alterado
    Quando a documentação correspondente é revisada
    Então finalidade, entrada, resposta, erros e autorização estão descritos
    E a documentação corresponde ao contrato vigente
