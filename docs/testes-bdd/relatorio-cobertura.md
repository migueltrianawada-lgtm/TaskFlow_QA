# Relatório de cobertura BDD

Relatório gerado automaticamente por `scripts/report-bdd-coverage.mjs`, contando cenários e tags de rastreabilidade nos arquivos Gherkin.

## Resumo

- Arquivos Gherkin: **6**
- Cenários/esquemas especificados: **59** (esquemas contados por declaração, sem expandir linhas de Exemplos)
- Cenários marcados `@executavel`: **3/59**
- RFs rastreados por pelo menos um cenário: **14/14 (100%)**
- RNFs rastreados por pelo menos um cenário: **22/22 (100%)**
- Cobertura de código: **não medida por este relatório**. A cobertura de tags Gherkin não representa cobertura de código nem prova que regras não implementadas funcionam.

## Cenários por arquivo

| Arquivo | Cenários especificados | Executáveis |
| --- | ---: | ---: |
| `autenticacao-recursos.feature` | 8 | 0 |
| `frontend-e-documentacao.feature` | 6 | 0 |
| `fundacao-implementada.feature` | 3 | 3 |
| `operacao-recursos.feature` | 8 | 0 |
| `qualidade.feature` | 22 | 0 |
| `reservas.feature` | 12 | 0 |

## Rastreabilidade de requisitos funcionais

| Requisito | Cobertura documental | Cenários etiquetados |
| --- | --- | ---: |
| RF-01 | Rastreado | 12 |
| RF-02 | Rastreado | 4 |
| RF-03 | Rastreado | 4 |
| RF-04 | Rastreado | 12 |
| RF-05 | Rastreado | 6 |
| RF-06 | Rastreado | 4 |
| RF-07 | Rastreado | 6 |
| RF-08 | Rastreado | 5 |
| RF-09 | Rastreado | 5 |
| RF-10 | Rastreado | 5 |
| RF-11 | Rastreado | 3 |
| RF-12 | Rastreado | 7 |
| RF-13 | Rastreado | 10 |
| RF-14 | Rastreado | 7 |

## Rastreabilidade de requisitos não funcionais

| Requisito | Cobertura documental | Cenários etiquetados |
| --- | --- | ---: |
| RNF-01 | Rastreado | 4 |
| RNF-02 | Rastreado | 8 |
| RNF-03 | Rastreado | 5 |
| RNF-04 | Rastreado | 7 |
| RNF-05 | Rastreado | 4 |
| RNF-06 | Rastreado | 3 |
| RNF-07 | Rastreado | 3 |
| RNF-08 | Rastreado | 2 |
| RNF-09 | Rastreado | 8 |
| RNF-10 | Rastreado | 2 |
| RNF-11 | Rastreado | 9 |
| RNF-12 | Rastreado | 2 |
| RNF-13 | Rastreado | 8 |
| RNF-14 | Rastreado | 3 |
| RNF-15 | Rastreado | 16 |
| RNF-16 | Rastreado | 8 |
| RNF-17 | Rastreado | 2 |
| RNF-18 | Rastreado | 7 |
| RNF-19 | Rastreado | 2 |
| RNF-20 | Rastreado | 9 |
| RNF-21 | Rastreado | 2 |
| RNF-22 | Rastreado | 2 |

## Limites e próximos critérios de cobertura

- A suíte Cucumber executa somente cenários marcados `@executavel`; demais arquivos especificam critérios para fluxos ainda não implementados.
- Os testes do PostgreSQL Testcontainers verificam a infraestrutura de teste e conectividade SQL. Não representam cobertura de persistência de negócio.
- A validação de 10.000 usuários simultâneos, navegadores, viewports e métricas ponta a ponta requer ambiente/carga controlados; este relatório não afirma que esses testes foram executados.
- Para elevar a cobertura executável, implementar os RFs, adicionar passos Cucumber ligados aos casos de uso reais e medir cobertura de código separadamente.
