# Diagrama estático de componentes

## Escopo

O diagrama representa a implementação TaskFlow deste candidato local. A arquitetura documentada ainda precisa ser confirmada no commit integrado à `main`.

```mermaid
flowchart LR
    SOL[Solicitante]
    RES[Responsável]
    ADM[Administrador]

    subgraph FRONT["Frontend — React/Vite"]
        UI[Interface web responsiva]
        APIJS[Cliente HTTP<br/>frontend/src/api.js]
        UI --> APIJS
    end

    subgraph BACK["Backend — Java 21 / Spring Boot"]
        SEC[Spring Security<br/>JWT Bearer e perfis]
        AUTH[AuthController]
        RESOURCE[RecursoController]
        BOOKING[ReservaController]
        ADMIN[GestaoController]
        RS[RecursoServiceImpl]
        BS[ReservaServiceImpl]
        AUDIT[AuditoriaService]
        NOTICE[NotificacaoService<br/>simulada]
        CONFLICT[RegistroConflitoService]
        REPOS[Spring Data JPA repositories]
        ENTITIES[Entidades e regras de domínio]

        SEC --> AUTH
        SEC --> RESOURCE
        SEC --> BOOKING
        SEC --> ADMIN
        RESOURCE --> RS
        BOOKING --> BS
        ADMIN --> AUDIT
        ADMIN --> NOTICE
        BS --> AUDIT
        BS --> NOTICE
        BS --> CONFLICT
        RS --> REPOS
        BS --> REPOS
        AUDIT --> REPOS
        NOTICE --> REPOS
        CONFLICT --> REPOS
        REPOS --> ENTITIES
    end

    subgraph DATA["Persistência configurada por perfil"]
        H2[(H2 em memória<br/>dev/testes)]
        PG[(PostgreSQL / Neon<br/>perfil prod)]
    end

    SPEC[OpenAPI / Swagger]
    CI[GitHub Actions<br/>Maven, frontend, JaCoCo]

    SOL --> UI
    RES --> UI
    ADM --> UI
    APIJS --> SEC
    REPOS --> H2
    REPOS --> PG
    AUTH -. documentação .-> SPEC
    RESOURCE -. documentação .-> SPEC
    BOOKING -. documentação .-> SPEC
    ADMIN -. documentação .-> SPEC
    CI -. valida e publica artifact JaCoCo .-> BACK
```

## Responsabilidades e limites

- **Interface React:** apresenta os fluxos e erros retornados pela API; não substitui autenticação, autorização ou validação no servidor.
- **Spring Security e JWT:** valida token e perfil nas operações protegidas.
- **Controllers:** adaptam contratos HTTP e encaminham operações aos serviços.
- **Serviços de aplicação:** coordenam regras de recursos, reservas, auditoria, notificação simulada e registro de conflitos.
- **Spring Data JPA e entidades:** realizam persistência e representam recursos, reservas, usuários e eventos relacionados.
- **Persistência:** H2 é usada no perfil local; PostgreSQL/Neon é selecionado por configuração no perfil `prod`. Nenhuma conta inicial é criada no Neon.
- **OpenAPI/Swagger:** publica contratos; não participa da execução dos fluxos.
- **GitHub Actions:** executa validações automatizadas e, após sucesso do Maven, publica `jacoco-report` como artifact. A execução que comprovar a entrega ainda precisa corresponder ao SHA da tag.

## Restrições observadas

- A autorização efetiva ocorre no backend mesmo quando a interface oculta ações.
- A criação da reserva usa transação e bloqueio das linhas de recursos selecionados, seguida da verificação de reservas e manutenção.
- Notificações são simuladas e persistidas; não há adaptador externo ativo.
- O teste local de concorrência em H2 não comprova desempenho nem comportamento PostgreSQL sob carga de produção.
