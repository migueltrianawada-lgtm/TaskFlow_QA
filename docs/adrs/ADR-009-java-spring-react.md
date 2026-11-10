# ADR-009: Java Spring Boot e React

- **Status:** Aceito
- **Data:** 2026-11-09

## Contexto

A arquitetura e os contratos foram documentados inicialmente de forma agnóstica. Para criar a espinha dorsal solicitada, é necessário escolher tecnologias para backend e frontend. O sistema exige aplicação web responsiva e cobertura em navegadores, e a implementação também deve manter uma base Java/Spring.

## Decisão

- **Backend:** Java 21 com Spring Boot 3.5.7, usando Maven Wrapper para tornar a execução de testes e aplicação reprodutível.
- **Frontend:** React 19 com Vite 6 para interface web responsiva.
- **Testes de frontend:** Vitest e Testing Library.
- **Testes de backend:** JUnit 5 e Spring Boot Test.
- **Persistência:** permanece sem tecnologia selecionada. A implementação inicial não adiciona um banco físico; a decisão deve seguir os contratos e táticas transacionais dos ADRs 004 e 008.

## Consequências

- A implementação atual deixa de ser agnóstica nas camadas de execução do backend e frontend, mas mantém domínio e contratos desacoplados de banco e provedores.
- A interface web React atende ao requisito documentado de navegador, responsividade e medição de latência renderizada.
- A versão e a configuração de produção ainda precisam ser avaliadas e mantidas por política de atualização e segurança.
- A arquitetura MVC deve refletir responsabilidades do Spring no backend e componentes React no frontend, sem deslocar regras de negócio críticas para o cliente.

## Rastreabilidade

- **RFs:** RF-01 a RF-14.
- **RNFs:** RNF-03, RNF-06, RNF-08, RNF-19, RNF-21 e RNF-22.
- **Personas:** SOL, RES e ADM.
