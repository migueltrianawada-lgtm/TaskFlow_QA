# TaskFlow_QA

## Aplicação

Estrutura inicial composta por API Java 21 com Spring Boot e frontend React com Vite. O banco de dados físico ainda não foi escolhido; os contratos e decisões permanecem agnósticos conforme a arquitetura documentada.

### Backend

Requer Java 21. O Maven Wrapper baixa e usa a versão definida pelo projeto:

```powershell
cd backend
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

O endpoint público de disponibilidade é `GET http://localhost:8080/api/health`. A documentação OpenAPI fica em `http://localhost:8080/swagger-ui.html`.

O perfil local `dev` usa H2 em memória para validar o mapeamento JPA. Esse banco é descartável e exclusivo para desenvolvimento; o banco de produção ainda não foi escolhido. O PostgreSQL continua configurado somente para os testes de infraestrutura com Testcontainers.

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

Esta etapa não implementa os fluxos completos de reserva, login/JWT, autorização funcional por perfil ou regras transacionais de concorrência. A segurança mantém o acesso negado por padrão: somente health e documentação OpenAPI são públicos até que a autenticação e as permissões sejam implementadas. Não existem usuários, senhas ou segredos de demonstração.