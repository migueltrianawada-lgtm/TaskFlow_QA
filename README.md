# TaskFlow_QA

## TaskFlow

Aplicação de organização de recursos acadêmicos, com API Java 21/Spring Boot, interface React/Vite e persistência PostgreSQL no Neon em produção. O ambiente local usa H2 em memória. O banco Neon não recebe usuário inicial automaticamente.

### Backend

Requer Java 21. O Maven Wrapper baixa e usa a versão definida pelo projeto. Para validar:

```powershell
cd backend
.\mvnw.cmd verify
.\mvnw.cmd spring-boot:run
```

O endpoint público de saúde é `GET http://localhost:8080/api/health`. A documentação OpenAPI/Swagger fica em `http://localhost:8080/swagger-ui.html`; os endpoints de negócio exigem token Bearer.

O perfil local `dev` usa H2 em memória, recriado a cada inicialização, e cria uma conta fictícia de demonstração de Administrador: `admin@taskflow.local` / `TaskFlow-Demo-2026!`. A conta só existe no H2 local. Use-a para entrar pela tela real de login; crie usuários de teste na área **Gestão → Usuários**. Não reutilize essa senha fora do ambiente local.

Para conectar um projeto Neon, copie `backend/.env.example` para `backend/.env`, substitua os valores pelos dados de **Neon → Connect** e rode a API no perfil `prod`:

```powershell
cd backend
Copy-Item .env.example .env
# Edite .env localmente; não o envie ao Git.
$env:SPRING_PROFILES_ACTIVE = "prod"
.\mvnw.cmd spring-boot:run
```

O perfil `prod` exige `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD` e `TASKFLOW_JWT_SECRET` (mínimo 32 bytes), aplica as migrações Flyway e valida o schema com Hibernate. Use a URL JDBC da Neon com `sslmode=require`; mantenha credenciais somente em arquivo local ignorado pelo Git ou gerenciador de segredos do deploy. Não cole credenciais em issues, commits ou conversas. Por decisão de escopo, nenhum administrador é criado automaticamente no Neon; o acesso de produção permanece indisponível até existir um procedimento privado de provisionamento inicial. O plano gratuito da Neon é para desenvolvimento/demonstração e não garante a meta de 2.000 usuários simultâneos.

### Frontend

Requer Node.js e npm:

```powershell
cd frontend
npm ci
npm test
npm run build
npm run dev
```

O servidor de desenvolvimento encaminha chamadas `/api` para `http://localhost:8080`. Inicie o backend antes do frontend. O token Bearer é mantido na sessão do navegador e removido ao sair ou quando expira.

### Fluxos e API

| Operação | Endpoint | Acesso |
|---|---|---|
| Login e usuário atual | `POST /api/auth/login`, `GET /api/auth/me` | Login público; perfil exige token |
| Consultar, cadastrar, alterar e desativar recursos | `GET/POST /api/recursos`, `PUT/DELETE /api/recursos/{id}` | Consulta autenticada; escrita Administrador |
| Criar, listar, alterar, cancelar e aprovar reserva | `GET/POST /api/reservas`, `PUT /api/reservas/{id}`, `POST /api/reservas/{id}/cancelamento`, `POST /api/reservas/{id}/aprovacao` | Token; aprovação Responsável/Administrador |
| Usuários e manutenção | `/api/usuarios`, `/api/manutencoes` | Administrador; leitura de manutenção também Responsável |
| Retirada e devolução | `GET/POST /api/reservas/{id}/movimentacoes` | Titular para consulta; Responsável/Administrador para registrar |
| Auditoria, notificações e relatório | `/api/auditorias`, `/api/notificacoes`, `/api/relatorios/utilizacao` | Auditoria e relatórios: Responsável/Administrador; notificações: titular |

As reservas usam transação e bloqueio das linhas de recursos para serializar pedidos concorrentes. Sobreposição de recursos, agenda de docentes e manutenção retorna HTTP 409; a tentativa conflitante é registrada para o relatório. Materiais e equipamentos aceitam alocação parcial conforme estoque. Operações relevantes gravam auditoria e notificações simuladas sem serviço externo.

Swagger publica os contratos, parâmetros, modelos e respostas de erro da API. A suíte BDD documenta cenários adicionais, mas apenas cenários explicitamente habilitados como executáveis são validados pelo Maven. Os requisitos de carga de 2.000 usuários e a inicialização segura de usuários em produção ainda dependem de ambiente e decisões operacionais próprias; não são atestados por estes testes locais.