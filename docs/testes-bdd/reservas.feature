# language: pt

@aceitacao
Funcionalidade: Solicitação, validação e aprovação de reservas
  Para alocar recursos acadêmicos sem conflitos
  Como Solicitante ou Responsável
  Quero criar e gerenciar reservas com disponibilidade consistente

  Contexto:
    Dado que o usuário está autenticado
    E existem salas, professores e materiais cadastrados

  @RF-04 @RNF-01 @RNF-02 @RNF-11 @RNF-13
  Cenário: Criar reserva com recursos disponíveis
    Dado que os recursos solicitados estão disponíveis no período
    Quando o Solicitante envia uma reserva válida
    Então a reserva é persistida com o estado correspondente
    E a alteração e o autor são registrados em auditoria
    E o frontend apresenta o estado persistido

  @RF-04 @RNF-02 @RNF-09 @RNF-11
  Cenário: Alterar reserva própria sem conflito
    Dado que o Solicitante possui uma reserva que pode ser alterada
    E os novos recursos e horários estão disponíveis
    Quando altera a reserva
    Então a alteração é persistida sem alocação parcial
    E o frontend apresenta os novos dados e o estado atual

  @RF-04 @RNF-02 @RNF-11 @RNF-15
  Cenário: Cancelar reserva própria permitida
    Dado que o Solicitante possui uma reserva que pode ser cancelada
    Quando solicita o cancelamento
    Então a reserva passa para o estado cancelada
    E os recursos deixam de ser considerados ocupados
    E o frontend confirma o estado persistido

  @RF-04 @RNF-09 @RNF-15
  Cenário: Impedir alteração de reserva pertencente a outro solicitante
    Dado que a reserva pertence a outro usuário
    Quando o Solicitante tenta alterá-la
    Então o servidor nega a operação
    E os dados da reserva permanecem inalterados

  @RF-05 @RNF-04 @RNF-11 @RNF-20
  Esquema do Cenário: Rejeitar sobreposição de recurso
    Dado que o recurso do tipo <tipo> possui reserva de 10:00 a 11:00
    Quando o Solicitante pede esse recurso de <inicio> a <fim>
    Então o sistema <resultado>

    Exemplos:
      | tipo       | inicio | fim   | resultado                                  |
      | sala       | 10:30  | 11:30 | rejeita por conflito de horário            |
      | professor  | 10:30  | 11:30 | rejeita por conflito na agenda do professor|
      | sala       | 11:00  | 12:00 | permite continuar por ser intervalo adjacente|

  @RF-06 @RNF-03 @RNF-04 @RNF-11 @RNF-20
  Cenário: Duas solicitações concorrentes disputam o mesmo recurso e período
    Dado que duas solicitações simultâneas pedem o mesmo recurso no mesmo intervalo
    Quando ambas tentam confirmar a reserva
    Então no máximo uma alocação incompatível é confirmada
    E a outra solicitação recebe um conflito compreensível
    E nenhuma das operações deixa dados parcialmente persistidos

  @RF-06 @RNF-04
  Cenário: Solicitações concorrentes alocam recursos distintos
    Dado que duas solicitações simultâneas usam recursos e horários compatíveis
    Quando ambas tentam confirmar a reserva
    Então ambas podem ser confirmadas
    E cada reserva mantém seus próprios recursos e período

  @RF-07 @RNF-02 @RNF-07 @RNF-13 @RNF-15
  Cenário: Solicitação de recurso restrito aguarda aprovação
    Dado que o recurso solicitado exige aprovação
    Quando o Solicitante envia uma reserva sem conflito
    Então a reserva fica pendente de aprovação
    E o Responsável é informado conforme a configuração de notificação
    E a interface não apresenta a reserva como aprovada

  @RF-07 @RNF-13 @RNF-15
  Esquema do Cenário: Responsável decide solicitação especial
    Dado que existe uma solicitação pendente de recurso restrito
    Quando o Responsável decide "<decisao>" e informa uma justificativa
    Então o sistema registra a decisão, o responsável e a data e hora
    E a reserva assume o estado "<estado>"

    Exemplos:
      | decisao  | estado   |
      | aprovar  | aprovada |
      | rejeitar | recusada |

  @RF-07 @RNF-15 @RNF-18
  Cenário: Impedir aprovação por Solicitante
    Dado que existe uma solicitação pendente
    E o usuário autenticado tem o perfil Solicitante
    Quando tenta aprovar a solicitação
    Então o servidor nega a operação
    E o estado da reserva não é alterado

  @RF-08 @RNF-11 @RNF-13 @RNF-20
  Cenário: Bloquear recurso durante manutenção
    Dado que o Administrador registrou um bloqueio de manutenção para um período
    Quando um Solicitante tenta reservar o recurso dentro desse período
    Então a reserva é rejeitada por indisponibilidade
    E o bloqueio e a tentativa são rastreáveis
    E o frontend informa o período indisponível

  @RF-08 @RNF-09 @RNF-16
  Cenário: Rejeitar bloqueio de manutenção com intervalo inválido
    Dado que o Administrador informa um fim anterior ou igual ao início
    Quando solicita o bloqueio
    Então o sistema não persiste o bloqueio
    E o frontend orienta a corrigir o intervalo
