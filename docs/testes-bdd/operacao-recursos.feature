# language: pt

@aceitacao
Funcionalidade: Operação, rastreabilidade, notificação e relatórios
  Para acompanhar o uso e o ciclo de vida dos recursos
  Como Responsável ou Administrador
  Quero registrar movimentações, consultar histórico e obter relatórios confiáveis

  Contexto:
    Dado que o usuário está autenticado

  @RF-09 @RNF-11 @RNF-13 @RNF-15 @RNF-20
  Esquema do Cenário: Registrar retirada ou devolução de material
    Dado que há uma reserva aprovada com <quantidade_disponivel> unidades disponíveis
    Quando o Responsável registra a <movimentacao> de <quantidade> unidades
    Então o sistema <resultado>
    E a movimentação válida é associada à reserva, ao material, ao responsável e à data e hora

    Exemplos:
      | quantidade_disponivel | movimentacao | quantidade | resultado                                      |
      | 5                     | retirada     | 2          | registra a retirada sem exceder o saldo        |
      | 3                     | devolução    | 2          | registra a devolução e atualiza o saldo        |
      | 1                     | retirada     | 2          | rejeita a retirada sem alterar o saldo         |

  @RF-09 @RNF-15 @RNF-16
  Cenário: Impedir movimentação por usuário sem permissão
    Dado que o usuário possui o perfil Solicitante
    Quando tenta registrar retirada ou devolução
    Então o servidor nega a operação
    E nenhuma movimentação é persistida

  @RF-10 @RNF-13 @RNF-15 @RNF-18
  Cenário: Consultar histórico auditável de uma reserva
    Dado que uma reserva recebeu alterações e decisões registradas
    Quando um usuário autorizado consulta seu histórico
    Então cada evento apresenta autor, ação, entidade afetada e data e hora
    E os registros são apresentados sem permitir alteração pelo usuário

  @RF-10 @RNF-15
  Cenário: Negar acesso não autorizado ao histórico
    Dado que o usuário não tem permissão para consultar determinado histórico
    Quando solicita os registros
    Então o servidor nega a consulta
    E não expõe dados do histórico

  @RF-11 @RNF-07 @RNF-12
  Esquema do Cenário: Notificar sem alterar o resultado da reserva
    Dado que uma reserva válida foi persistida
    E o adaptador de notificação retorna "<resultado_entrega>"
    Quando o sistema processa a notificação
    Então o estado da reserva permanece confirmado
    E a entrega é registrada como "<estado_notificacao>"

    Exemplos:
      | resultado_entrega       | estado_notificacao |
      | sucesso                 | enviada            |
      | rejeição                | falha              |
      | serviço indisponível    | falha              |

  @RF-12 @RNF-03 @RNF-05 @RNF-15 @RNF-16
  Cenário: Gerar relatório de utilização e carga horária por recurso
    Dado que existem reservas no período consultado
    Quando o Responsável solicita relatório filtrado por período e recurso
    Então o relatório apresenta utilização e carga horária alocada para os filtros
    E os dados correspondem às reservas persistidas

  @RF-12 @RNF-03 @RNF-05
  Cenário: Apresentar conflitos evitados com base em eventos registrados
    Dado que existem tentativas rejeitadas por conflito no período
    Quando o Administrador solicita o relatório de conflitos evitados
    Então o relatório contabiliza as tentativas registradas
    E não conta uma solicitação sem evento de conflito como conflito evitado

  @RF-12 @RNF-15
  Cenário: Negar relatório a perfil sem autorização
    Dado que o Solicitante não tem permissão para acessar o relatório administrativo
    Quando tenta consultar o relatório
    Então o servidor nega o acesso
    E não expõe dados não autorizados
