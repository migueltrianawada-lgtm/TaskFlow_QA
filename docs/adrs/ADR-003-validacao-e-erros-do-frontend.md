# ADR-003: Validação em camadas e erros seguros no frontend

- **Status:** Proposto
- **Data:** 2026-11-09

## Contexto

Os usuários precisam de uma interface responsiva com mensagens compreensíveis. Entradas inválidas, conflitos e falhas técnicas devem ser apresentados de forma útil sem permitir que validações do navegador sejam contornadas nem expor detalhes internos.

## Decisão

Validar dados no frontend para orientar o usuário e repetir validações necessárias no servidor como controle definitivo. Respostas do servidor devem distinguir sucesso, rejeição de regra e falha recuperável com informações seguras. A View apresenta o estado real da operação, explica a ação possível e identifica estados por texto, sem depender somente de cor ou ícone.

## Consequências

- Há validação apropriada à camada de experiência e validação autoritativa no servidor.
- O contrato entre frontend e servidor precisa definir estados e erros públicos.
- Testes devem cobrir sucesso, validação rejeitada, autorização negada e falha recuperável.
- A interface precisa ser validada nos viewports e navegadores especificados nos RNFs.

## Rastreabilidade

- **RFs:** RF-01 a RF-14, especialmente RF-13.
- **RNFs:** RNF-02, RNF-08, RNF-09, RNF-10, RNF-16 e RNF-18.
- **Personas:** SOL, RES e ADM.
