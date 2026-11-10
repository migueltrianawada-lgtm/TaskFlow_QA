# language: pt

@aceitacao
Funcionalidade: Acesso e consulta de recursos
  Para acessar as funções permitidas e localizar recursos adequados
  Como usuário do TaskFlow
  Quero autenticar meu perfil e consultar os recursos cadastrados

  Contexto:
    Dado que existem usuários ativos com os perfis Solicitante, Responsável e Administrador
    E que existem salas, professores e materiais cadastrados

  @RF-01 @RNF-01 @RNF-14 @RNF-15 @RNF-17 @RNF-18
  Cenário: Autenticar usuário com credenciais válidas
    Dado que o Solicitante possui credenciais válidas
    Quando informa suas credenciais no frontend
    Então o sistema inicia uma sessão autenticada
    E apresenta somente as operações autorizadas ao perfil Solicitante

  @RF-01 @RNF-09 @RNF-14 @RNF-18
  Esquema do Cenário: Rejeitar autenticação inválida sem revelar dados sensíveis
    Dado que a tentativa de autenticação contém <condicao>
    Quando o usuário envia as credenciais
    Então o acesso é negado
    E a mensagem informa que não foi possível autenticar sem indicar qual credencial falhou
    E a resposta não expõe detalhes internos

    Exemplos:
      | condicao                         |
      | senha incorreta                  |
      | conta inexistente                |
      | conta inativa                    |

  @RF-01 @RNF-15
  Esquema do Cenário: Aplicar autorização por perfil em operação protegida
    Dado que o usuário autenticado possui o perfil <perfil>
    Quando tenta executar a operação <operacao>
    Então o servidor <resultado>
    E nenhuma alteração ocorre quando a operação não é autorizada

    Exemplos:
      | perfil        | operacao                         | resultado             |
      | Solicitante   | aprovar solicitação especial     | nega o acesso         |
      | Responsável   | cadastrar novo usuário           | nega o acesso         |
      | Administrador | aprovar solicitação especial     | permite conforme regra|

  @RF-02 @RNF-01 @RNF-13 @RNF-15 @RNF-16 @RNF-20
  Esquema do Cenário: Administrador cadastra recurso válido
    Dado que o Administrador está autenticado
    E informa os dados válidos de um <tipo_recurso>
    Quando solicita o cadastro
    Então o recurso é persistido e fica disponível para consulta
    E a ação é registrada no histórico de auditoria

    Exemplos:
      | tipo_recurso |
      | sala         |
      | professor    |
      | material     |

  @RF-02 @RNF-09 @RNF-16
  Cenário: Rejeitar cadastro com dados obrigatórios inválidos
    Dado que o Administrador está autenticado
    E informa dados inválidos para um recurso
    Quando solicita o cadastro
    Então o recurso não é persistido
    E o frontend identifica os dados que precisam de correção

  @RF-02 @RNF-15 @RNF-18
  Cenário: Impedir cadastro por perfil sem permissão
    Dado que o Solicitante está autenticado
    Quando tenta cadastrar uma sala
    Então o servidor nega a operação
    E nenhum recurso é criado
    E o frontend apresenta uma mensagem segura

  @RF-03 @RNF-03 @RNF-05 @RNF-16
  Esquema do Cenário: Pesquisar recursos aplicando filtros
    Dado que há recursos cadastrados com atributos diferentes de <filtro>
    Quando o usuário pesquisa usando <filtro> igual a "<valor>"
    Então o resultado contém somente recursos compatíveis com o filtro
    E a disponibilidade considera o período solicitado

    Exemplos:
      | filtro        | valor             |
      | tipo          | sala              |
      | capacidade    | 30                |
      | localização   | Bloco A           |
      | competência  | Matemática        |
      | disponibilidade | disponível no período |

  @RF-03 @RNF-09 @RNF-16
  Cenário: Informar ausência de resultados na pesquisa
    Dado que nenhum recurso satisfaz os filtros informados
    Quando o usuário executa a pesquisa
    Então o frontend informa que não foram encontrados recursos
    E permite revisar os filtros
