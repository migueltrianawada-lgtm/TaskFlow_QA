# Modelagens dinâmicas — sequências dos requisitos funcionais

Cada RF possui um diagrama de sequência próprio. Os requisitos não funcionais relacionados estão indicados junto a cada fluxo; os códigos e textos completos estão em `../requisitos-nao-funcionais.md`. Os diagramas descrevem o fluxo lógico; a implementação correspondente usa React/Vite, Spring Boot, JWT, Spring Data JPA e H2/PostgreSQL. Os principais endpoints e classes estão em `../rtm.md`. Os diagramas não implicam que todos os fluxos já tenham teste de aceitação executável.

## RF-01 — Autenticação e autorização por perfil

**RNFs relacionados:** RNF-01, RNF-03, RNF-06, RNF-09, RNF-14, RNF-15, RNF-17 e RNF-18. **Personas:** SOL, RES, ADM.

```mermaid
sequenceDiagram
    actor Usuario
    participant V as Frontend
    participant C as Controller
    participant A as Servico de autenticacao e autorizacao
    participant R as Repositorio de usuarios
    Usuario->>V: Informa credenciais
    V->>C: Envia solicitacao de autenticacao por canal protegido
    C->>A: Solicita autenticacao
    A->>R: Consulta conta e verificacao de credencial
    R-->>A: Dados da conta e hash
    A->>A: Verifica credencial e estado da conta
    alt Credenciais validas e conta ativa
        A-->>C: Identidade e perfil autorizado
        C-->>V: Sessao autenticada
        V-->>Usuario: Exibe funcoes do perfil
    else Credenciais invalidas ou conta inativa
        A-->>C: Falha de autenticacao
        C-->>V: Erro sem revelar qual dado falhou
        V-->>Usuario: Exibe orientacao segura
    end
```

## RF-02 — Cadastro e consulta de salas, professores e materiais

**RNFs relacionados:** RNF-01, RNF-03, RNF-06, RNF-08, RNF-15, RNF-16, RNF-19 e RNF-20. **Personas:** ADM para cadastro; SOL, RES e ADM para consulta conforme permissão.

```mermaid
sequenceDiagram
    actor Usuario as Administrador ou usuario autorizado
    participant V as Frontend
    participant C as Controller
    participant A as Autorizacao
    participant M as Model de recursos
    participant R as Repositorio
    Usuario->>V: Cadastra ou solicita consulta de recurso
    V->>C: Envia dados ou filtros
    C->>A: Verifica identidade e permissao
    alt Operacao autorizada
        C->>M: Valida dados e executa operacao
        M->>R: Persiste recurso e auditoria atomicamente ou consulta registros filtrados
        R-->>M: Resultado
        M-->>C: Recurso ou lista resultante
        C-->>V: Retorna resultado
        V-->>Usuario: Apresenta resultado
    else Operacao nao autorizada ou dados invalidos
        C-->>V: Retorna erro seguro
        V-->>Usuario: Exibe orientacao
    end
```

## RF-03 — Pesquisa por tipo, capacidade, localização, competência e disponibilidade

**RNFs relacionados:** RNF-01, RNF-03, RNF-05, RNF-06, RNF-08, RNF-16 e RNF-20. **Personas:** SOL, RES, ADM.

```mermaid
sequenceDiagram
    actor Usuario
    participant V as Frontend
    participant C as Controller
    participant M as Model de pesquisa
    participant RR as Repositorio de recursos
    participant RB as Repositorio de reservas e bloqueios
    Usuario->>V: Informa filtros e periodo
    V->>C: Envia criterios de pesquisa
    C->>C: Autentica, autoriza e valida filtros
    C->>M: Solicita pesquisa
    M->>RR: Consulta recursos por atributos
    RR-->>M: Recursos candidatos
    M->>RB: Consulta reservas e bloqueios no periodo
    RB-->>M: Ocupacoes e indisponibilidades
    M->>M: Calcula disponibilidade
    M-->>C: Resultado filtrado
    C-->>V: Retorna recursos disponiveis
    V-->>Usuario: Apresenta resultados e estados
```

## RF-04 — Criação, alteração e cancelamento de reservas

**RNFs relacionados:** RNF-01 a RNF-04, RNF-07 a RNF-13, RNF-15, RNF-16, RNF-19 e RNF-20. **Personas:** SOL, RES e ADM de acordo com autorização.

```mermaid
sequenceDiagram
    actor Usuario
    participant V as Frontend
    participant C as Controller de reservas
    participant M as Model de reservas
    participant DB as Persistencia
    participant AU as Auditoria
    participant N as Notificacao
    Usuario->>V: Cria, altera ou cancela reserva
    V->>C: Envia operacao e dados
    C->>C: Autentica, autoriza e valida entrada
    C->>M: Solicita operacao
    M->>DB: Inicia transacao e valida estado atual
    M->>DB: Verifica disponibilidade, conflitos e bloqueios
    alt Operacao permitida
        M->>DB: Persiste reserva e auditoria atomicamente
        DB-->>M: Confirma transacao
        M-->>C: Retorna estado atualizado
        C->>N: Solicita notificacao
        N-->>C: Entrega ou registra falha sem reverter reserva
        C-->>V: Retorna confirmacao
        V-->>Usuario: Apresenta estado atualizado
    else Operacao invalida ou conflituosa
        M->>DB: Desfaz transacao sem deixar alocacao parcial
        opt Conflito de disponibilidade
            M->>DB: Registra evento de conflito para o relatorio
        end
        DB-->>M: Nenhuma alocacao parcial
        M-->>C: Retorna motivo de rejeicao
        C-->>V: Retorna mensagem compreensivel
        V-->>Usuario: Apresenta orientacao
    end
```

## RF-05 — Detecção de sobreposição, inclusive na agenda do professor

**RNFs relacionados:** RNF-01, RNF-03, RNF-04, RNF-11, RNF-16 e RNF-20. **Personas:** SOL, RES e ADM.

```mermaid
sequenceDiagram
    actor Usuario
    participant V as Frontend
    participant C as Controller de reservas
    participant M as Validador de disponibilidade
    participant DB as Persistencia
    Usuario->>V: Solicita reserva ou validacao de horario
    V->>C: Envia periodo, sala e professor
    C->>C: Valida autenticacao e dados
    C->>M: Solicita verificacao de sobreposicao
    M->>DB: Consulta reservas ativas da sala no intervalo
    M->>DB: Consulta reservas ativas do professor no intervalo
    M->>DB: Consulta bloqueios aplicaveis
    DB-->>M: Reservas e bloqueios encontrados
    alt Nenhuma sobreposicao
        M-->>C: Horario disponivel
        C-->>V: Permite continuar o fluxo
        V-->>Usuario: Apresenta disponibilidade
    else Sobreposicao encontrada
        opt Tentativa de criar reserva
            M->>DB: Registra evento de conflito para o relatorio
        end
        M-->>C: Conflito e recurso afetado
        C-->>V: Retorna conflito sem detalhes internos
        V-->>Usuario: Solicita outro horario ou recurso
    end
```

## RF-06 — Proteção contra dupla reserva em solicitações simultâneas

**RNFs relacionados:** RNF-01, RNF-03, RNF-04, RNF-11 e RNF-20. **Personas:** SOL, RES e ADM.

```mermaid
sequenceDiagram
    actor U1 as Solicitante A
    actor U2 as Solicitante B
    participant C1 as Controller A
    participant C2 as Controller B
    participant M as Servico de reserva
    participant DB as Persistencia transacional
    U1->>C1: Solicita mesmo recurso e horario
    U2->>C2: Solicita mesmo recurso e horario
    C1->>M: Criar reserva A
    C2->>M: Criar reserva B
    par Solicitacao A
        M->>DB: Tenta proteger recurso e validar A
    and Solicitacao B
        M->>DB: Tenta proteger recurso e validar B
    end
    DB-->>M: Serializa as tentativas conflitantes
    alt Solicitacao A confirma primeiro
        M->>DB: Confirma reserva A
        DB-->>M: A persistida
        M->>DB: Registra evento de conflito da tentativa B
        M-->>C1: Reserva confirmada
        M-->>C2: Conflito; nenhuma alocacao B persistida
    else Solicitacao B confirma primeiro
        M->>DB: Confirma reserva B
        DB-->>M: B persistida
        M->>DB: Registra evento de conflito da tentativa A
        M-->>C2: Reserva confirmada
        M-->>C1: Conflito; nenhuma alocacao A persistida
    else Falha antes de confirmar uma alocacao
        DB-->>M: Transacao revertida
        M-->>C1: Erro sem alocacao parcial
        M-->>C2: Erro sem alocacao parcial
    end
```

## RF-07 — Aprovação obrigatória para recursos restritos

**RNFs relacionados:** RNF-01, RNF-02, RNF-03, RNF-07, RNF-10, RNF-13, RNF-15, RNF-16 e RNF-20. **Personas:** SOL e RES; ADM conforme permissão.

```mermaid
sequenceDiagram
    actor Solicitante
    actor Responsavel
    participant V as Frontend
    participant C as Controller de aprovacao
    participant M as Model de reservas
    participant DB as Persistencia
    participant AU as Auditoria
    participant N as Notificacao
    Solicitante->>V: Solicita recurso restrito
    V->>C: Envia solicitacao
    C->>M: Valida recurso e permissao
    M->>DB: Grava reserva pendente e auditoria
    DB-->>M: Reserva pendente
    M-->>C: Retorna pendencia
    C->>N: Solicita aviso ao Responsavel
    N-->>C: Resultado de notificacao
    Responsavel->>V: Analisa e decide com justificativa
    V->>C: Envia decisao
    C->>C: Verifica perfil de Responsavel
    C->>M: Solicita aprovacao ou rejeicao
    M->>DB: Revalida estado e registra decisao e auditoria
    DB-->>M: Novo estado persistido
    M-->>C: Retorna resultado
    C-->>V: Exibe decisao
    V-->>Solicitante: Apresenta estado da solicitacao
```

## RF-08 — Bloqueio de recursos em manutenção

**RNFs relacionados:** RNF-01, RNF-02, RNF-03, RNF-10, RNF-11, RNF-13, RNF-15, RNF-16 e RNF-20. **Personas:** ADM cadastra; SOL e RES recebem disponibilidade atualizada.

```mermaid
sequenceDiagram
    actor Administrador
    participant V as Frontend
    participant C as Controller de manutencao
    participant M as Model de recursos
    participant DB as Persistencia
    Administrador->>V: Define recurso e periodo de manutencao
    V->>C: Envia bloqueio e justificativa
    C->>C: Autentica, autoriza e valida intervalo
    C->>M: Solicita criacao do bloqueio
    M->>DB: Verifica reservas existentes e salva bloqueio e auditoria atomicamente
    alt Periodo permitido
        DB-->>M: Bloqueio persistido
        M-->>C: Retorna confirmacao
        C-->>V: Atualiza calendario e disponibilidade
        V-->>Administrador: Apresenta bloqueio
    else Dados invalidos ou conflito conforme regra
        DB-->>M: Rejeita ou reverte operacao
        M-->>C: Retorna motivo
        C-->>V: Exibe orientacao
    end
```

## RF-09 — Registro de retirada e devolução de materiais e equipamentos

**RNFs relacionados:** RNF-01, RNF-02, RNF-03, RNF-08, RNF-11, RNF-13, RNF-15, RNF-16 e RNF-20. **Personas:** RES registra; SOL e ADM consultam conforme autorização.

```mermaid
sequenceDiagram
    actor Responsavel
    participant V as Frontend
    participant C as Controller de movimentacao
    participant M as Model de estoque
    participant DB as Persistencia
    Responsavel->>V: Informa retirada ou devolucao e quantidade
    V->>C: Envia movimentacao
    C->>C: Autentica, autoriza e valida dados
    C->>M: Solicita registro
    M->>DB: Inicia transacao e consulta reserva e saldo
    alt Quantidade e estado validos
        M->>DB: Registra movimentacao e auditoria atomicamente
        DB-->>M: Confirma transacao
        M-->>C: Retorna saldo e estado atualizados
        C-->>V: Apresenta confirmacao
        V-->>Responsavel: Exibe movimentacao registrada
    else Quantidade ou estado invalido
        M->>DB: Desfaz transacao
        M-->>C: Retorna erro de dominio
        C-->>V: Exibe mensagem compreensivel
    end
```

## RF-10 — Histórico auditável de mudanças

**RNFs relacionados:** RNF-01, RNF-02, RNF-13, RNF-15, RNF-18 e RNF-20. **Personas:** RES e ADM consultam; SOL consulta dados permitidos.

```mermaid
sequenceDiagram
    actor Usuario
    participant V as Frontend
    participant C as Controller de auditoria
    participant A as Autorizacao
    participant M as Servico de auditoria
    participant DB as Persistencia
    Usuario->>V: Consulta historico por filtros
    V->>C: Envia filtros
    C->>A: Verifica identidade, perfil e escopo
    alt Consulta autorizada
        C->>M: Solicita registros filtrados
        M->>DB: Consulta auditoria sem permitir alteracao
        DB-->>M: Eventos correspondentes
        M-->>C: Retorna historico autorizado
        C-->>V: Apresenta registros
        V-->>Usuario: Exibe autor, acao e data/hora
    else Consulta nao autorizada
        A-->>C: Nega acesso
        C-->>V: Retorna erro seguro
        V-->>Usuario: Exibe orientacao
    end
```

## RF-11 — Notificação simulada ou integração com API externa

**RNFs relacionados:** RNF-01, RNF-02, RNF-03, RNF-07, RNF-12, RNF-16 e RNF-18. **Personas:** SOL, RES e ADM, conforme evento e destinatário.

```mermaid
sequenceDiagram
    actor Usuario
    participant V as Frontend
    participant C as Controller
    participant M as Model de notificacao
    participant DB as Persistencia
    participant A as Adaptador configurado
    participant EXT as Simulador ou servico externo
    Usuario->>V: Conclui operacao que gera aviso
    V->>C: Envia operacao
    C->>M: Executa fluxo de negocio
    M->>DB: Persiste operacao e estado consistente
    DB-->>M: Operacao confirmada
    M->>A: Solicita notificacao
    A->>EXT: Envia mensagem pelo contrato
    alt Entrega confirmada
        EXT-->>A: Confirmacao
        A->>DB: Registra estado enviada
    else Integracao indisponivel ou rejeicao
        EXT-->>A: Falha
        A->>DB: Registra estado falha
    end
    M-->>C: Retorna resultado da operacao independente da entrega
    C-->>V: Apresenta estado real da operacao
    V-->>Usuario: Exibe confirmacao ou estado atualizado
```

## RF-12 — Relatório de utilização, carga horária e conflitos evitados

**RNFs relacionados:** RNF-01, RNF-02, RNF-03, RNF-05, RNF-15 e RNF-16. **Personas:** RES e ADM.

```mermaid
sequenceDiagram
    actor Usuario as Responsavel ou Administrador
    participant V as Frontend
    participant C as Controller de relatorios
    participant A as Autorizacao
    participant S as Servico de relatorios
    participant R as Repositorios
    Usuario->>V: Seleciona periodo e recursos
    V->>C: Solicita relatorio
    C->>A: Verifica perfil e permissao
    alt Autorizado
        C->>S: Solicita calculo com filtros
        S->>R: Consulta reservas, recursos e eventos de conflito
        R-->>S: Dados filtrados
        S->>S: Calcula utilizacao, carga horaria e conflitos evitados
        S-->>C: Retorna resultados
        C-->>V: Apresenta relatorio e filtros aplicados
        V-->>Usuario: Renderiza relatorio
    else Nao autorizado ou filtros invalidos
        C-->>V: Retorna erro compreensivel
        V-->>Usuario: Exibe orientacao
    end
```

## RF-13 — Interface responsiva e mensagens de erro compreensíveis

**RNFs relacionados:** RNF-01, RNF-02, RNF-06, RNF-08, RNF-09, RNF-10 e RNF-18. **Personas:** SOL, RES e ADM.

```mermaid
sequenceDiagram
    actor Usuario
    participant V as Frontend responsivo
    participant C as Controller
    participant M as Model
    participant DB as Persistencia
    Usuario->>V: Acessa fluxo em dispositivo ou viewport
    V->>V: Adapta layout e apresenta controles identificados
    Usuario->>V: Executa acao
    V->>C: Envia solicitacao
    C->>M: Valida e executa operacao
    M->>DB: Consulta ou persiste conforme fluxo
    alt Operacao concluida
        DB-->>M: Resultado consistente
        M-->>C: Sucesso e estado
        C-->>V: Resposta funcional
        V-->>Usuario: Apresenta estado por texto e acao seguinte
    else Validacao, autorizacao ou falha recuperavel
        DB-->>M: Rejeicao ou falha sem estado parcial
        M-->>C: Codigo e mensagem de dominio segura
        C-->>V: Erro sem detalhes internos
        V-->>Usuario: Explica problema e correcao possivel
    end
```

## RF-14 — Documentação de API ou dos fluxos públicos do sistema

**RNFs relacionados:** RNF-01, RNF-17, RNF-18, RNF-19, RNF-21 e RNF-22. **Personas:** consumidores dos fluxos públicos e equipe responsável pela documentação; não corresponde a uma ação operacional das três personas de uso.

```mermaid
sequenceDiagram
    actor Mantenedor as Responsavel pela documentacao
    participant F as Fluxo publico ou contrato de API
    participant D as Documentacao versionada
    participant R as Revisao
    participant P as Publicacao
    Mantenedor->>F: Altera ou cria fluxo publico
    Mantenedor->>D: Atualiza finalidade, entradas, respostas, erros e autorizacao
    D->>R: Solicita revisao de consistencia
    R->>F: Confere documentacao contra contrato vigente
    alt Contrato e documentacao consistentes
        R-->>D: Aprova revisao
        D->>P: Publica versao atualizada
        P-->>D: Confirma publicacao
    else Divergencias encontradas
        R-->>Mantenedor: Solicita correcao
        Mantenedor->>D: Ajusta documentacao
    end
```

## Cobertura dos requisitos

| RF | Fluxo modelado | RNFs principais |
| --- | --- | --- |
| RF-01 | Autenticação e autorização por perfil | RNF-14, RNF-15, RNF-17 |
| RF-02 | Cadastro e consulta de recursos | RNF-15, RNF-16, RNF-19 |
| RF-03 | Pesquisa filtrada e disponibilidade | RNF-03, RNF-05, RNF-16 |
| RF-04 | Criar, alterar e cancelar reserva | RNF-03, RNF-04, RNF-11, RNF-13 |
| RF-05 | Detecção de sobreposição | RNF-04, RNF-11, RNF-20 |
| RF-06 | Proteção contra dupla reserva | RNF-03, RNF-04, RNF-11 |
| RF-07 | Aprovação de recurso restrito | RNF-07, RNF-13, RNF-15 |
| RF-08 | Bloqueio por manutenção | RNF-11, RNF-13, RNF-20 |
| RF-09 | Retirada e devolução | RNF-11, RNF-13, RNF-20 |
| RF-10 | Consulta de auditoria | RNF-13, RNF-15, RNF-18 |
| RF-11 | Envio ou simulação de notificação | RNF-07, RNF-12, RNF-18 |
| RF-12 | Relatórios | RNF-03, RNF-05, RNF-15 |
| RF-13 | Uso responsivo e mensagens | RNF-06, RNF-08, RNF-09, RNF-10 |
| RF-14 | Ciclo de documentação dos fluxos públicos | RNF-19, RNF-21, RNF-22 |
