# Diagnóstico de Status dos Requisitos Funcionais (RTM & Aula 12) — TaskFlow_QA

## 1. Visão Geral
* **Projeto:** TaskFlow_QA (Sistema de Organização e Reserva de Recursos)
* **Data da Avaliação:** 16/11/2026
* **Versão do Repositório:** Skeleton Base (Java 21 / Spring Boot 3 + React / Vite)

---

## 2. Matriz de Situação dos Requisitos Funcionais (RF01 a RF14)

| ID | Descrição do Requisito Funcional | Status Atual | Evidência no Repositório / Código | Ação Pendente / Próximos Passos |
| :--- | :--- | :--- | :--- | :--- |
| **RF01** | Cadastro e Gestão de Usuários | **Pendente** | `backend/src/main/` | Criar entidade `Usuario`, DTOs e endpoints de CRUD. |
| **RF02** | Autenticação e Emissão de Sessão (JWT) | **Pendente** | `README.md` (Escopo atual) | Implementar Spring Security, filtro JWT e endpoint `/api/auth/login`. |
| **RF03** | Cadastro e Categorização de Recursos | **Pendente** | `backend/src/main/` | Criar entidade `Recurso` (salas, equipamentos) e repositório JPA. |
| **RF04** | Solicitação de Reserva de Recurso | **Parcial** | `backend/` (Validação de horários) | Conectar lógica de conflito ao banco de dados e expor `POST /api/reservas`. |
| **RF05** | Validação de Conflito de Sobreposição | **Concluído** | `backend/src/main/` | Validação de sobreposição de intervalos implementada no backend. |
| **RF06** | Aprovação / Rejeição por Perfil Autorizado | **Pendente** | `README.md` (Escopo atual) | Criar máquina de estados da reserva (`PENDENTE`, `APROVADA`, `REJEITADA`). |
| **RF07** | Bloqueio de Recurso por Manutenção | **Pendente** | `backend/src/main/` | Implementar regra de bloqueio prioritário por administrador. |
| **RF08** | Cancelamento de Reserva pelo Solicitante | **Pendente** | `backend/src/main/` | Expor endpoint `DELETE /api/reservas/{id}` com validação de propriedade. |
| **RF09** | Checagem de Disponibilidade (`/api/health`) | **Concluído** | `backend/src/main/`, `README.md` | Endpoint `GET /api/health` operante retornando status 200 OK. |
| **RF10** | Histórico Auditável e Imutável de Mudanças | **Pendente** | `backend/src/main/` | Criar tabela `ReservaAuditLog` gravando alterações na mesma transação. |
| **RF11** | Consulta de Relatório de Utilização | **Pendente** | `backend/src/main/` | Implementar endpoint `GET /relatorios/utilizacao` com filtros por data e recurso. |
| **RF12** | Matriz de Autorização por Perfil de Acesso | **Pendente** | `README.md` (Escopo atual) | Mapear e aplicar anotações `@PreAuthorize` nos controllers REST. |
| **RF13** | Interface Web Responsiva para Reservas | **Parcial** | `frontend/` (React + Vite) | Estrutura web criada com proxy para porta 8080; faltam as telas funcionais. |
| **RF14** | Execução de Testes BDD Executáveis | **Concluído** | `.github/workflows/` | Pipeline de CI configurado com BDD executável e Testcontainers. |

---

## 3. Resumo Diagnóstico
* **Requisitos Concluídos:** 3 (RF05, RF09, RF14)
* **Requisitos Parciais:** 2 (RF04, RF13)
* **Requisitos Pendentes:** 9 (RF01, RF02, RF03, RF06, RF07, RF08, RF10, RF11, RF12)

---

## 4. Plano de Ação Prioritário para Fechamento da RTM
1. **Sprint de Persistência:** Configurar o PostgreSQL real via Spring Data JPA e conectar o Testcontainers.
2. **Sprint de Segurança:** Implementar a matriz de autorização por perfil e a autenticação JWT.
3. **Sprint de Auditoria:** Desenvolver a gravação transacional do histórico imutável (RF10) e o relatório de utilização (RF11).