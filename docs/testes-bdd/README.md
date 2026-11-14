# Especificações de testes BDD

Os arquivos `.feature` desta pasta descrevem cenários de aceitação em Gherkin, no formato Given/When/Then (`Dado/Quando/Então`). Cada cenário possui tags com os requisitos funcionais (`@RF-nn`) e não funcionais (`@RNF-nn`) relacionados.

## Cobertura especificada

- **Requisitos funcionais:** RF-01 a RF-14.
- **Requisitos não funcionais:** RNF-01 a RNF-22.
- **Personas:** Solicitante, Responsável e Administrador, indicadas no contexto dos cenários.
- **Frontend:** estados, mensagens, responsividade, compatibilidade e latência renderizada estão contemplados.
- O relatório de rastreabilidade é gerado por `node scripts/report-bdd-coverage.mjs`.
- A suíte Cucumber Java executa somente cenários marcados `@executavel`; o workflow GitHub Actions também executa testes Java, frontend e PostgreSQL em Testcontainers.

## Arquivos

- `autenticacao-recursos.feature`: autenticação, autorização, cadastros e pesquisa.
- `reservas.feature`: reservas, conflitos, concorrência, aprovação e manutenção.
- `operacao-recursos.feature`: movimentações, auditoria, notificações e relatórios.
- `frontend-e-documentacao.feature`: experiência frontend e contratos/fluxos públicos.
- `qualidade.feature`: critérios não funcionais de aceitação e de validação controlada.
- `fundacao-implementada.feature`: cenários executáveis para a validação de sobreposição atualmente implementada.

## Estado de execução

Os cenários de `fundacao-implementada.feature` são executados com Cucumber contra a regra de domínio de sobreposição. Os outros arquivos são critérios especificados, mas ainda não estão conectados a passos executáveis porque os fluxos de negócio correspondentes não existem na aplicação. Portanto, cobertura total das tags significa **rastreabilidade documental dos RFs/RNFs**, não cobertura de código medida nem validação funcional completa.

Os cenários de carga com 2.000 usuários e validações dependentes de navegadores/ambientes são marcados para execução controlada, não para testes unitários locais.

## Ambiente informado

O comando `source /home/MiguelPalacios/repos/TaksFlow/.venv/bin/activate` foi informado como referência de ambiente. Este repositório usa Java/React e está em ambiente Windows; o comando Linux não foi executado nem alterou o ambiente.
