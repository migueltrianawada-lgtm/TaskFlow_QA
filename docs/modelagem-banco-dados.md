# Modelagem e documentação do banco de dados

## Objetivo e escopo

Este documento descreve o modelo lógico de dados necessário para atender às personas e aos requisitos funcionais e não funcionais. O modelo é agnóstico a fabricante e tecnologia de banco de dados. Nomes, tipos físicos e sintaxe de implementação serão definidos posteriormente.

## Modelo estático de dados

O diagrama de classes e entidades atualizado está em [`modelagens-estaticas/diagrama-classes-entidades.md`](modelagens-estaticas/diagrama-classes-entidades.md).

## Entidades

| Entidade | Responsabilidade |
| --- | --- |
| Usuário | Conta, autenticação e identidade de quem solicita ou executa ações. |
| Perfil | Papel de acesso: Solicitante, Responsável ou Administrador. |
| Usuário_Perfil | Associação entre contas e perfis; permite atribuir os perfis necessários sem duplicar contas. |
| Recurso | Cadastro pesquisável de salas, professores e materiais/equipamentos, incluindo atributos aplicáveis ao tipo. Para recurso do tipo professor, pode ser associado à conta de usuário do docente e guardar sua competência. |
| Reserva | Solicitação, intervalo de uso, solicitante e estado do fluxo. |
| Reserva_Recurso | Recursos e quantidades alocados pela reserva; professor também é recurso para controle de agenda. |
| Aprovação | Decisão do Responsável sobre solicitação que exige aprovação, com justificativa e data. |
| Bloqueio_Manutenção | Período em que um recurso não pode ser alocado. |
| Movimentação_Material | Registro de retirada e devolução de materiais e equipamentos. |
| Auditoria | Histórico de alterações com autor, ação, entidade afetada e momento da operação. |
| Evento_Conflito | Tentativa de alocação rejeitada por conflito, necessária ao cálculo de conflitos evitados. |
| Notificação | Estado da notificação simulada ou encaminhada a serviço externo, incluindo falhas. |

## Regras de integridade

- Identificadores de cada entidade são únicos; chaves estrangeiras referenciam registros existentes.
- E-mail de usuário é único. Credenciais são armazenadas somente como hash.
- O fim de uma reserva ou bloqueio deve ser posterior ao início.
- Reservas canceladas ou recusadas não consomem disponibilidade.
- Não pode haver intervalos sobrepostos para o mesmo recurso reservado. A regra deve cobrir salas e professores; para materiais deve validar a soma das quantidades simultâneas contra o estoque disponível.
- A verificação de conflito e a gravação da reserva devem ser uma única operação transacional com proteção a solicitações concorrentes.
- Reservas para recursos restritos só podem alcançar o estado aprovado após decisão autorizada do Responsável.
- Uma reserva que coincida com bloqueio de manutenção não pode ser confirmada.
- Quantidade reservada e movimentada deve ser positiva e não pode ultrapassar o saldo disponível.
- Decisão de aprovação, movimentação e operação de reserva relevante deve gerar auditoria com usuário e data/hora.
- Histórico de auditoria não pode ser alterado por perfis sem autorização.
- Tentativas de alocação rejeitadas por conflito podem ser registradas como eventos para compor o relatório de conflitos evitados.
- A indisponibilidade do serviço de notificação não altera o estado persistido da reserva; a falha fica registrada em Notificação.

## Consultas cobertas pelo modelo

- Pesquisar recursos por tipo, capacidade, localização, competência e disponibilidade em um intervalo.
- Consultar reservas de uma sala ou professor e detectar sobreposições.
- Calcular saldo de materiais considerando estoque, reservas e movimentações.
- Listar pedidos pendentes de aprovação e decisões registradas.
- Consultar bloqueios de manutenção por recurso e período.
- Gerar utilização por recurso, carga horária alocada e conflitos evitados com base em reservas e tentativas registradas.
- Consultar histórico por usuário, entidade, reserva e período.

Os fluxos dinâmicos de persistência estão em [`modelagens-dinamicas/sequencias-requisitos-funcionais.md`](modelagens-dinamicas/sequencias-requisitos-funcionais.md), incluindo reserva, concorrência, auditoria e movimentação.

## Limites do modelo

- O diagrama define o modelo lógico; não prescreve tipos SQL, índices específicos, comandos DDL ou fabricante de banco de dados.
- A estratégia física para impedir sobreposição concorrente deve ser escolhida conforme a tecnologia de persistência durante a implementação, mantendo a garantia transacional descrita.
- Dados opcionais e atributos específicos de cada tipo de recurso devem ser refinados antes da implementação física.
