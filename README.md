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

O endpoint de disponibilidade inicial é `GET http://localhost:8080/api/health`.

### Frontend

Requer Node.js e npm:

```powershell
cd frontend
npm ci
npm test
npm run dev
```

O servidor de desenvolvimento encaminha chamadas `/api` para o backend em `http://localhost:8080`.

### Escopo atual

Implementação limitada à espinha dorsal, endpoint de saúde e validação de sobreposição de intervalos. Reservas persistentes, autenticação, autorização, banco de dados e demais fluxos funcionais ainda não estão implementados.