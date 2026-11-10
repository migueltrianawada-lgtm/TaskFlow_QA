# ADR-005: Auditoria consistente e protegida

- **Status:** Proposto
- **Data:** 2026-11-09

## Contexto

Criação e alteração de recursos, reservas, aprovações, bloqueios e movimentações precisam ser rastreáveis. Um estado persistido sem seu evento de auditoria reduz a confiabilidade do histórico; permitir alteração irrestrita do histórico invalida seu propósito.

## Decisão

Registrar as ações relevantes com usuário, ação, entidade afetada e data/hora. Para mudanças críticas, persistir a alteração de negócio e seu evento de auditoria de forma consistente, de modo que uma não seja confirmada sem a outra. Tratar os registros como append-only para perfis operacionais, permitindo consulta somente conforme autorização.

## Consequências

- O registro de auditoria integra as transações críticas correspondentes.
- Consultas de histórico precisam ser autorizadas e filtradas pelo escopo permitido.
- A retenção, política de dados e eventual acesso administrativo devem ser detalhados antes da implementação.
- Os testes devem comprovar cobertura das ações críticas e rejeição de alteração não autorizada.

## Rastreabilidade

- **RFs:** RF-02, RF-04, RF-07, RF-08, RF-09 e RF-10.
- **RNFs:** RNF-13 e RNF-18.
- **Personas:** SOL, RES e ADM.
