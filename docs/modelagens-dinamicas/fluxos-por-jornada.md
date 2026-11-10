# Fluxos por jornada — diagramas UML de sequência

Os diagramas representam jornadas sugeridas para as três personas, da interação no frontend até a validação e persistência lógica. São modelos documentais agnósticos a tecnologia. Os diagramas de sequência por requisito funcional estão em [`sequencias-requisitos-funcionais.md`](sequencias-requisitos-funcionais.md).

## Jornada do Solicitante — pesquisar e acompanhar reserva

**RFs:** RF-01, RF-03, RF-04, RF-05, RF-06, RF-07, RF-11 e RF-13. **RNFs:** RNF-02, RNF-03, RNF-04, RNF-07, RNF-08, RNF-09, RNF-10, RNF-11 e RNF-15.

```mermaid
sequenceDiagram
    actor Solicitante
    participant V as Frontend
    participant C as Controller
    participant A as Autenticacao e autorizacao
    participant M as Model e casos de uso
    participant DB as Persistencia
    participant N as Notificacao
    Solicitante->>V: Acessa e entra na aplicacao
    V->>C: Envia credenciais
    C->>A: Autentica e identifica perfil
    A-->>C: Sessao e perfil Solicitante
    C-->>V: Acesso autenticado
    Solicitante->>V: Pesquisa recursos e periodo
    V->>C: Envia filtros
    C->>A: Verifica permissao
    C->>M: Consulta disponibilidade
    M->>DB: Busca recursos, reservas e bloqueios filtrados
    DB-->>M: Dados de disponibilidade
    M-->>C: Recursos disponiveis
    C-->>V: Resultados da pesquisa
    V-->>Solicitante: Apresenta resultados e estados
    Solicitante->>V: Seleciona recursos e envia reserva
    V->>C: Envia pedido
    C->>A: Valida autorizacao e escopo
    C->>M: Solicita reserva
    M->>DB: Valida conflitos e grava atomicamente
    alt Conflito ou recurso indisponivel
        M->>DB: Reverte operacao e registra conflito quando aplicavel
        M-->>C: Retorna conflito
        C-->>V: Retorna orientacao segura
        V-->>Solicitante: Sugere revisar horario ou recursos
    else Disponibilidade valida
        alt Recurso restrito
            M->>DB: Persiste reserva pendente e auditoria atomicamente
            DB-->>M: Reserva pendente de aprovacao
            M->>N: Notifica Responsavel
            N-->>M: Entrega ou falha registrada
            M-->>C: Reserva pendente
            C-->>V: Retorna estado pendente
            V-->>Solicitante: Informa que aguarda aprovacao
        else Sem aprovacao especial
            M->>DB: Persiste reserva e auditoria atomicamente
            DB-->>M: Reserva confirmada
            M->>N: Solicita notificacao
            N-->>M: Entrega ou falha registrada
            M-->>C: Reserva confirmada
            C-->>V: Retorna estado atualizado
            V-->>Solicitante: Exibe confirmacao
        end
    end
    opt Alterar ou cancelar reserva propria
        Solicitante->>V: Solicita alteracao ou cancelamento
        V->>C: Envia acao sobre reserva
        C->>A: Verifica perfil, escopo e estado
        C->>M: Executa alteracao ou cancelamento
        M->>DB: Valida e persiste mudanca com auditoria
        DB-->>M: Novo estado
        M-->>C: Resultado atualizado
        C-->>V: Apresenta novo estado
        V-->>Solicitante: Confirma alteracao ou cancelamento
    end
```

## Jornada do Responsável — decidir solicitação e acompanhar material

**RFs:** RF-01, RF-03, RF-04, RF-07, RF-09, RF-10, RF-11 e RF-13. **RNFs:** RNF-02, RNF-07, RNF-08, RNF-09, RNF-11, RNF-13 e RNF-15.

```mermaid
sequenceDiagram
    actor Responsavel
    participant V as Frontend
    participant C as Controller
    participant A as Autorizacao
    participant M as Model e casos de uso
    participant DB as Persistencia
    participant N as Notificacao
    Responsavel->>V: Acessa fila de solicitacoes
    V->>C: Solicita pendencias
    C->>A: Verifica perfil Responsavel
    C->>M: Consulta pedidos pendentes
    M->>DB: Busca reservas restritas e dados dos recursos
    DB-->>M: Detalhes da solicitacao
    M-->>C: Fila de pendencias
    C-->>V: Apresenta detalhes e estado
    V-->>Responsavel: Exibe recursos, docente, periodo e justificativa
    Responsavel->>V: Aprova ou rejeita com justificativa
    V->>C: Envia decisao
    C->>A: Revalida autorizacao
    C->>M: Solicita decisao
    M->>DB: Revalida estado e disponibilidade
    alt Decisao permitida
        M->>DB: Persiste decisao e auditoria atomicamente
        DB-->>M: Estado atualizado
        M->>N: Notifica Solicitante
        N-->>M: Entrega ou falha registrada
        M-->>C: Retorna decisao
        C-->>V: Apresenta resultado
        V-->>Responsavel: Confirma decisao registrada
    else Estado alterado ou reserva invalida
        M-->>C: Informa que a decisao nao pode ser aplicada
        C-->>V: Retorna mensagem compreensivel
        V-->>Responsavel: Atualiza detalhes e solicita nova revisao
    end
    opt Material reservado precisa ser entregue ou recebido
        Responsavel->>V: Registra retirada ou devolucao e quantidade
        V->>C: Envia movimentacao
        C->>A: Verifica permissao
        C->>M: Solicita movimentacao
        M->>DB: Valida reserva, quantidade e saldo
        alt Movimentacao valida
            M->>DB: Persiste movimentacao e auditoria atomicamente
            DB-->>M: Saldo e estado atualizados
            M-->>C: Retorna confirmacao
            C-->>V: Exibe movimento registrado
            V-->>Responsavel: Confirma retirada ou devolucao
        else Dados invalidos
            M-->>C: Retorna erro de dominio
            C-->>V: Exibe orientacao segura
            V-->>Responsavel: Solicita correcao dos dados
        end
    end
```

## Jornada do Administrador — manter cadastros e disponibilidade

**RFs:** RF-01, RF-02, RF-03, RF-08, RF-10, RF-12 e RF-13. **RNFs:** RNF-02, RNF-03, RNF-05, RNF-08, RNF-09, RNF-13, RNF-15 e RNF-16.

```mermaid
sequenceDiagram
    actor Administrador
    participant V as Frontend
    participant C as Controller
    participant A as Autorizacao
    participant M as Model e casos de uso
    participant DB as Persistencia
    Administrador->>V: Acessa area administrativa
    V->>C: Solicita autenticacao
    C->>A: Verifica identidade e perfil
    A-->>C: Perfil Administrador
    C-->>V: Exibe operacoes autorizadas
    Administrador->>V: Cadastra ou atualiza recurso
    V->>C: Envia dados do recurso
    C->>A: Verifica permissao
    C->>M: Valida e salva cadastro
    M->>DB: Persiste recurso e auditoria atomicamente
    DB-->>M: Cadastro atualizado
    M-->>C: Retorna resultado
    C-->>V: Apresenta confirmacao
    V-->>Administrador: Exibe recurso atualizado
    Administrador->>V: Define periodo de manutencao
    V->>C: Envia recurso, intervalo e justificativa
    C->>A: Verifica permissao
    C->>M: Solicita bloqueio
    M->>DB: Valida intervalo, reservas e persiste bloqueio com auditoria
    alt Bloqueio valido
        DB-->>M: Bloqueio registrado
        M-->>C: Disponibilidade atualizada
        C-->>V: Exibe bloqueio no calendario
        V-->>Administrador: Confirma indisponibilidade
    else Periodo invalido ou incompatibilidade
        M-->>C: Retorna motivo da rejeicao
        C-->>V: Exibe erro compreensivel
        V-->>Administrador: Permite revisar periodo ou recurso
    end
    opt Consultar operacao institucional
        Administrador->>V: Solicita historico ou relatorio
        V->>C: Envia filtros de periodo e recurso
        C->>A: Verifica permissao
        C->>M: Consulta auditoria ou relatorio
        M->>DB: Busca registros filtrados
        DB-->>M: Dados correspondentes
        M-->>C: Resultado consolidado
        C-->>V: Apresenta dados e filtros aplicados
        V-->>Administrador: Exibe historico ou relatorio
    end
```

## Relação com os diagramas por RF

Estes diagramas descrevem jornadas ponta a ponta centradas nas personas. Para detalhes isolados de cada RF e sua relação com RNFs, consultar [`sequencias-requisitos-funcionais.md`](sequencias-requisitos-funcionais.md).
