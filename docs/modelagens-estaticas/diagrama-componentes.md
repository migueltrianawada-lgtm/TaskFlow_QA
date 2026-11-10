# Diagrama estático de componentes

## Escopo

Arquitetura lógica MVC agnóstica a linguagem, framework, protocolo, banco de dados e infraestrutura. O diagrama cobre os componentes necessários aos requisitos funcionais e não funcionais documentados.

```mermaid
flowchart LR
    SOL[Solicitante]
    RES[Responsável]
    ADM[Administrador]

    subgraph FRONT["Frontend"]
        UI[Interface web responsiva]
        CLIENT[Cliente de contratos públicos]
        DOCVIEW[Visualizador de fluxos e documentação pública]
        UI --> CLIENT
        UI --> DOCVIEW
    end

    subgraph APP["Aplicação MVC"]
        ENTRY[Camada de entrada e validação]
        AUTH[Autenticação e autorização]
        CTRL[Controllers]
        USECASES[Casos de uso]
        DOMAIN[Model e regras de negócio]
        REPORT[Serviço de relatórios]
        AUDIT[Serviço de auditoria]
        NOTIFY[Serviço de notificação]
        ENTRY --> AUTH
        ENTRY --> CTRL
        CTRL --> USECASES
        USECASES --> DOMAIN
        USECASES --> REPORT
        USECASES --> AUDIT
        USECASES --> NOTIFY
        REPORT --> DOMAIN
        DOMAIN --> AUDIT
    end

    subgraph PORTS["Contratos e adaptadores agnósticos"]
        REPOS[Contratos de repositório]
        NOTIFYPORT[Contrato de notificação]
        SIM[Adaptador simulador]
        EXT[Adaptador de integração externa]
        APIREF[Contrato e documentação da API]
        NOTIFY --> NOTIFYPORT
        NOTIFYPORT --> SIM
        NOTIFYPORT --> EXT
    end

    subgraph DATA["Persistência lógica"]
        DB[(Repositório persistente)]
        USERDATA[Usuários e perfis]
        RESDATA[Recursos e reservas]
        MAINTDATA[Bloqueios e movimentações]
        AUDITDATA[Auditoria e eventos de conflito]
        NOTIFDATA[Notificações]
        DB --- USERDATA
        DB --- RESDATA
        DB --- MAINTDATA
        DB --- AUDITDATA
        DB --- NOTIFDATA
    end

    SOL --> UI
    RES --> UI
    ADM --> UI
    CLIENT --> ENTRY
    DOCVIEW --> APIREF
    CTRL --> REPOS
    DOMAIN --> REPOS
    REPORT --> REPOS
    AUDIT --> REPOS
    NOTIFY --> REPOS
    REPOS --> DB
```

## Responsabilidades e limites

- **Frontend:** apresenta os fluxos de consulta, gestão, reserva, aprovação, manutenção, movimentação e relatórios; informa estados e erros. A interface não substitui autorização e validação no servidor.
- **Camada de entrada:** recebe operações, valida sua estrutura e encaminha os pedidos aos Controllers.
- **Autenticação e autorização:** identifica a sessão e autoriza cada operação por perfil e contexto do recurso.
- **Controllers:** adaptam solicitações e respostas da interface aos casos de uso; não implementam regras de conflito ou acesso a dados.
- **Casos de uso:** coordenam os fluxos e as transações que envolvem domínio, persistência, auditoria e notificações.
- **Model e regras de negócio:** aplicam as regras de disponibilidade, sobreposição, concorrência, aprovação, manutenção, estoque e movimentação.
- **Repositórios:** isolam as operações de leitura e gravação do mecanismo físico de persistência.
- **Auditoria:** registra ator, operação, entidade afetada e data/hora de maneira associada à operação de negócio.
- **Relatórios:** consultam dados filtrados para utilização, carga horária e conflitos evitados.
- **Notificação:** usa o adaptador simulado ou externo; falhas são registradas e não desfazem reservas válidas.
- **Contrato/API e documentação:** descrevem os fluxos públicos e suas entradas, respostas, erros e requisitos de autorização.
- **Persistência lógica:** agrupa os dados descritos no diagrama de classes estático de entidades.

## Restrições arquiteturais

- Nenhum componente depende de fabricante, framework, linguagem, serviço de nuvem ou banco de dados específico.
- A validação de acesso, entradas e regras de negócio ocorre no servidor, mesmo que o frontend também apresente validações de usabilidade.
- Verificação de disponibilidade e gravação de uma alocação concorrente devem ocorrer com garantias transacionais.
- A gravação da operação de negócio e da respectiva auditoria deve manter consistência.
