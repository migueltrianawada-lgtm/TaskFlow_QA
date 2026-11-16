# Matriz de Autorização e Especificação de Segurança (Aula 13) — TaskFlow_QA

## 1. Visão Geral
* **Projeto:** TaskFlow_QA (Sistema de Organização e Reserva de Recursos)
* **Data:** 16/11/2026
* **Objetivo:** Definir o modelo de controle de acesso baseado em papéis (RBAC - *Role-Based Access Control*) para todas as rotas da API REST, garantindo que usuários acessem apenas os recursos condizentes com seu perfil.

---

## 2. Perfis de Acesso (Roles)
* **ANONIMO:** Usuário não autenticado (requisições sem cabeçalho `Authorization: Bearer <token>`).
* **SOLICITANTE:** Usuário autenticado comum. Pode visualizar recursos, solicitar reservas e cancelar suas próprias reservas.
* **RESPONSAVEL:** Gestor de recursos. Pode aprovar/rejeitar reservas e consultar relatórios de uso dos recursos sob sua alçada.
* **ADMINISTRADOR:** Usuário com acesso pleno. Pode cadastrar recursos, aplicar bloqueios de manutenção, gerenciar perfis e consultar auditorias.

---

## 3. Matriz de Permissões por Rota REST

| Rota / Endpoint | Método HTTP | ANONIMO | SOLICITANTE | RESPONSAVEL | ADMINISTRADOR | Resposta Esperada se Negado |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `/api/health` | `GET` | ✅ Permite (200) | ✅ Permite (200) | ✅ Permite (200) | ✅ Permite (200) | — |
| `/api/auth/login` | `POST` | ✅ Permite (200) | ✅ Permite (200) | ✅ Permite (200) | ✅ Permite (200) | — |
| `/api/recursos` | `GET` | ❌ Nega (401) | ✅ Permite (200) | ✅ Permite (200) | ✅ Permite (200) | `401 Unauthorized` |
| `/api/recursos` | `POST` | ❌ Nega (401) | ❌ Nega (403) | ❌ Nega (403) | ✅ Permite (201) | `403 Forbidden` |
| `/api/reservas` | `POST` | ❌ Nega (401) | ✅ Permite (201) | ✅ Permite (201) | ✅ Permite (201) | `401 Unauthorized` |
| `/api/reservas/minhas` | `GET` | ❌ Nega (401) | ✅ Permite (200) | ✅ Permite (200) | ✅ Permite (200) | `401 Unauthorized` |
| `/api/reservas/{id}/aprovar` | `PATCH` | ❌ Nega (401) | ❌ Nega (403) | ✅ Permite (200) | ✅ Permite (200) | `403 Forbidden` |
| `/api/reservas/{id}/cancelar`| `DELETE`| ❌ Nega (401) | ✅ (Apenas Própria)| ❌ Nega (403) | ✅ Permite (200) | `403 Forbidden` |
| `/api/manutencao/bloquear` | `POST` | ❌ Nega (401) | ❌ Nega (403) | ❌ Nega (403) | ✅ Permite (200) | `403 Forbidden` |
| `/relatorios/utilizacao` | `GET` | ❌ Nega (401) | ❌ Nega (403) | ✅ Permite (200) | ✅ Permite (200) | `403 Forbidden` |

---

## 4. Sanitização e Padrão de Resposta de Erro (`ProblemDetail` - RFC 7807)
* **Formato de Erro:** Falhas de autenticação (`401`) e autorização (`403`) retornam o objeto padrão `ProblemDetail` do Spring Boot 3.
* **Proteção contra Vazamento de Informações (Information Disclosure):**
  * Nenhuma mensagem de erro de segurança deve expor *stack traces*, nomes de tabelas, estruturas internas de banco ou classes Java.
  * Exemplo de resposta sanitizada (`403 Forbidden`):
    ```json
    {
      "type": "[suspicious link removed]",
      "title": "Acesso Negado",
      "status": 403,
      "detail": "O perfil do usuário não possui permissão para executar esta operação.",
      "instance": "/api/manutencao/bloquear"
    }
    ```

---

## 5. Automação de Testes Parametrizados de Autorização
* **Localização:** `backend/src/test/java/com/taskflow/security/MatrizAutorizacaoTest.java`.
* **Estratégia:** Utilização de `@ParameterizedTest` (JUnit 5) com `@WithMockUser` ou envio de tokens JWT simulados via MockMvc para validar em loop todas as combinações da matriz.

---

## 6. Evidência de Teste de Mutação Manual
* **Mutação Aplicada:** Alteração temporária na anotação do controller em `/api/manutencao/bloquear`, mudando `@PreAuthorize("hasRole('ADMIN')")` para `@PreAuthorize("hasRole('SOLICITANTE')")`.
* **Resultado Obtido:** A suíte de testes parametrizados detectou imediatamente a vulnerabilidade no pipeline de CI, resultando em **BUILD FAILURE** devido ao retorno inesperado de código `200` em vez de `403` para o perfil Solicitante.