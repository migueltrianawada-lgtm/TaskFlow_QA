# TaskFlow_QA

## Aplicação

Estrutura inicial composta por API Java 21 com Spring Boot e frontend React com Vite. PostgreSQL gerenciado no Neon foi escolhido para persistência hospedada, mantendo os contratos desacoplados do provedor.

### Backend

Requer Java 21. O Maven Wrapper baixa e usa a versão definida pelo projeto:

```powershell
cd backend
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

O endpoint público de disponibilidade é `GET http://localhost:8080/api/health`. A documentação OpenAPI fica em `http://localhost:8080/swagger-ui.html`.

O perfil local `dev` usa H2 em memória, recriado a cada inicialização, e cria uma conta de demonstração de Administrador: `admin@taskflow.local` / `TaskFlow-Demo-2026!`. Essa conta fictícia existe somente no banco H2 local; não é criada no Neon e não autentica o login demonstrativo do frontend.

Para conectar um projeto Neon, copie `backend/.env.example` para `backend/.env`, substitua os valores pelos dados de **Neon → Connect** e rode a API no perfil `prod`:

```powershell
cd backend
Copy-Item .env.example .env
# Edite .env localmente; não o envie ao Git.
$env:SPRING_PROFILES_ACTIVE = "prod"
.\mvnw.cmd spring-boot:run
```

O perfil `prod` exige `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` e `SPRING_DATASOURCE_PASSWORD`, aplica as migrações Flyway e valida o schema com Hibernate. Use a URL JDBC oferecida/construída a partir da conexão Neon com `sslmode=require`; a senha fica somente no arquivo local ignorado pelo Git ou no gerenciador de segredos do ambiente de deploy. Não cole credenciais em issues, commits ou conversas. O plano gratuito é para desenvolvimento/demonstração e pode suspender bancos inativos; não representa garantia para a meta de 10.000 usuários simultâneos.

### Frontend

Requer Node.js e npm:

```powershell
cd frontend
npm ci
npm test
npm run dev
```

O servidor de desenvolvimento encaminha chamadas `/api` para o backend em `http://localhost:8080`.

O frontend apresenta cinco telas navegáveis: login demonstrativo, painel inicial, consulta de recursos, reservas e gestão por perfil. Os dados e ações dessas telas são locais e temporários; a autenticação e a persistência ainda não estão integradas ao backend.

### Estrutura atual

O backend separa API/DTOs, casos de uso, domínio, segurança e persistência. O modelo inicial inclui usuários e perfis, recursos, reservas e seus recursos associados, aprovações, bloqueios de manutenção, movimentações de materiais, auditoria, conflitos e notificações; os repositórios e contratos de serviço iniciam a organização das camadas.

Esta etapa não implementa os fluxos completos de reserva, login/JWT, autorização funcional por perfil ou regras transacionais de concorrência. A segurança mantém o acesso negado por padrão: somente health e documentação OpenAPI são públicos até que a autenticação e as permissões sejam implementadas. A migração cria os três perfis padrão, mas não cria contas ou senhas de demonstração.