# Arquitetura MVC

## Objetivo e escopo

Este documento descreve a arquitetura lógica Model-View-Controller (MVC) para o sistema de alocação de salas, professores e materiais. A proposta é baseada em `personas.md`, `requisitos-funcionais.md` e `requisitos-nao-funcionais.md`. A stack escolhida para a espinha dorsal está em [`arquitetura.md`](arquitetura.md) e no ADR-009; o banco de dados físico e a infraestrutura de produção continuam sem escolha.

A visão arquitetural consolidada e as decisões táticas estão em [`arquitetura.md`](arquitetura.md) e [`adrs/`](adrs/README.md).

## Organização MVC

### Model
- Representa usuários, perfis, recursos, reservas, aprovações, bloqueios de manutenção, movimentações, notificações e auditoria.
- Contém as regras de negócio para disponibilidade, conflitos de horário, recursos restritos e quantidades de materiais.
- Persiste e consulta os dados por meio de um repositório, sem depender da tecnologia de armazenamento.
- Executa alterações críticas de reserva e disponibilidade em transações consistentes.

### View
- Apresenta pesquisa, disponibilidade, cadastros, reservas, aprovações, movimentações e relatórios.
- Adapta a interface a dispositivos móveis, tablets e computadores.
- Apresenta estados, confirmações e erros em linguagem compreensível, sem depender apenas de cor ou ícone.
- Exibe somente ações e informações permitidas ao perfil autenticado.

### Controller
- Recebe ações e dados da View e direciona a operação ao Model.
- Valida a entrada, verifica autenticação e autorização no servidor e seleciona o fluxo correspondente.
- Devolve à View o resultado ou uma mensagem de erro segura.
- Não contém regras de negócio de conflito ou persistência; delega essas responsabilidades ao Model.

## Componentes de apoio

- **Autenticação e autorização:** identifica usuários e aplica as permissões de Solicitante, Responsável e Administrador em cada operação protegida.
- **Persistência:** armazena os dados e garante integridade e transações. O modelo de dados está em `modelagem-banco-dados.md`.
- **Auditoria:** registra autor, ação, data/hora e entidade afetada junto às operações relevantes.
- **Notificação:** usa um simulador ou integração externa. Falhas de notificação não desfazem uma reserva persistida com sucesso e são registradas.
- **Relatórios:** consulta os dados necessários para utilização por recurso, carga horária alocada e conflitos evitados.

## Modelo estático

Os diagramas estáticos completos foram organizados em [`modelagens-estaticas/diagrama-componentes.md`](modelagens-estaticas/diagrama-componentes.md) e [`modelagens-estaticas/diagrama-classes-entidades.md`](modelagens-estaticas/diagrama-classes-entidades.md).

## Modelo dinâmico

Os diagramas de sequência, um para cada RF e ligados aos RNFs, estão em [`modelagens-dinamicas/sequencias-requisitos-funcionais.md`](modelagens-dinamicas/sequencias-requisitos-funcionais.md).

## Requisitos de qualidade aplicados

- **Segurança:** autorização no Controller no servidor; validação no servidor; credenciais e sessões protegidas; erros sem informação sensível.
- **Desempenho:** pesquisa filtrada e consultas de disponibilidade eficientes; meta existente de 95% das consultas em até 2 segundos em ambiente representativo.
- **Confiabilidade:** operações críticas transacionais; auditoria consistente; notificação desacoplada da persistência da reserva.
- **Manutenibilidade:** separação entre View, Controller, Model e componentes de apoio.
- **Compatibilidade e portabilidade:** responsabilidades e contratos MVC não dependem de navegador, provedor, linguagem ou ambiente específico.
