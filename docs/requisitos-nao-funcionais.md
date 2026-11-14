# Requisitos não funcionais

## Base e critérios

Os requisitos estão organizados em oito eixos de qualidade, com foco em requisitos verificáveis, critérios de aceitação e rastreabilidade, conforme as práticas de engenharia de requisitos e qualidade de software abordadas no SWEBOK. Os oito eixos abaixo são a organização adotada para este projeto; não são apresentados como uma lista oficial de eixos do SWEBOK.

Todos os requisitos indicam os requisitos funcionais relacionados e as personas impactadas. A validação deve considerar a interface frontend e os serviços do sistema, conforme aplicável.

### Personas
- **SOL:** Solicitante.
- **RES:** Responsável.
- **ADM:** Administrador.

### Requisitos funcionais referenciados
- **RF-01:** Autenticação e autorização por perfil.
- **RF-02:** Cadastro e consulta de salas, professores e materiais.
- **RF-03:** Pesquisa por tipo, capacidade, localização, competência e disponibilidade.
- **RF-04:** Criação, alteração e cancelamento de reservas.
- **RF-05:** Detecção de sobreposição de horários, inclusive na agenda do professor.
- **RF-06:** Proteção contra dupla reserva em solicitações simultâneas.
- **RF-07:** Aprovação obrigatória para recursos restritos.
- **RF-08:** Bloqueio de recursos em manutenção.
- **RF-09:** Registro de retirada e devolução de materiais e equipamentos.
- **RF-10:** Histórico auditável de mudanças.
- **RF-11:** Notificação simulada ou integração com API externa.
- **RF-12:** Relatório de utilização por recurso, carga horária alocada e conflitos evitados.
- **RF-13:** Interface responsiva e mensagens de erro compreensíveis.
- **RF-14:** Documentação de API ou dos fluxos públicos do sistema.

## 1. Adequação funcional

- **RNF-01:** Em testes de aceitação, 100% dos fluxos associados aos RFs 01 a 14 devem produzir o resultado esperado e persistir estados consistentes. A evidência será o resultado dos testes por requisito. **Relaciona:** RF-01 a RF-14. **Personas:** SOL, RES, ADM.
- **RNF-02:** Para cada operação concluída ou rejeitada, a interface frontend deve apresentar estado e mensagem correspondentes ao resultado recebido do servidor; nenhum fluxo deve indicar sucesso quando a operação não foi persistida. **Verificação:** testes de integração entre interface e serviços para sucesso, validação rejeitada e falha. **Relaciona:** RF-01 a RF-13. **Personas:** SOL, RES, ADM.

## 2. Eficiência de desempenho

> Meta revisada pelo grupo para 2.000 usuários simultâneos nesta versão dos requisitos. A evidência de teste deve ser coletada para esse alvo; o smoke local de 10 usuários não o comprova.

- **RNF-03:** Em teste controlado com **2.000 usuários simultâneos autenticados**, executando uma carga representativa dos RFs 01 a 13, pelo menos 95% das consultas de disponibilidade devem responder em até 2 segundos e pelo menos 95% das demais operações interativas devem responder em até 3 segundos, medidos da ação no frontend até a resposta renderizada. A taxa de erros técnicos ou timeouts deve ser inferior a 1%. O relatório do teste deve registrar duração, distribuição de carga, dados utilizados, latências percentis e erros. **Relaciona:** RF-01 a RF-13, especialmente RF-03, RF-04, RF-09 e RF-12. **Personas:** SOL, RES, ADM.
- **RNF-04:** Sob solicitações concorrentes para o mesmo recurso e intervalo, o sistema deve confirmar no máximo uma alocação incompatível; todas as demais devem receber resposta de conflito sem criar alocações parciais. **Verificação:** teste de concorrência automatizado e inspeção dos dados persistidos. **Relaciona:** RF-04, RF-05 e RF-06. **Personas:** SOL, RES, ADM.
- **RNF-05:** Com 2.000 usuários simultâneos no perfil de carga definido para o RNF-03, pelo menos 95% dos relatórios devem ser apresentados em até 5 segundos e refletir os filtros informados. **Relaciona:** RF-03 e RF-12. **Personas:** RES, ADM.

## 3. Compatibilidade

- **RNF-06:** O frontend deve permitir executar os fluxos principais nos navegadores Chrome, Firefox, Edge e Safari em suas versões estáveis vigentes no início de cada ciclo de validação, sem diferença que impeça o uso dos RFs 01 a 13. **Verificação:** execução da suíte de fluxos principais em cada navegador e registro das versões. **Relaciona:** RF-01 a RF-13. **Personas:** SOL, RES, ADM.
- **RNF-07:** A integração de notificação deve usar o contrato documentado e, quando estiver indisponível, a interface deve preservar e exibir o estado real da reserva sem apresentá-la como falha de agendamento. **Verificação:** testes com adaptador simulado para sucesso, rejeição e indisponibilidade. **Relaciona:** RF-04, RF-07 e RF-11. **Personas:** SOL, RES, ADM.

## 4. Usabilidade e experiência do frontend

- **RNF-08:** As telas dos fluxos RF-01 a RF-13 devem se adaptar a larguras de viewport de 360 px, 768 px e 1280 px, mantendo conteúdo e ações sem sobreposição ou rolagem horizontal da página. **Verificação:** testes responsivos nos três tamanhos. **Relaciona:** RF-01 a RF-13. **Personas:** SOL, RES, ADM.
- **RNF-09:** Em validações e falhas recuperáveis, a interface deve informar o que ocorreu e a ação possível em linguagem compreensível; não deve mostrar stack traces, mensagens internas ou detalhes técnicos. **Verificação:** revisão dos estados de erro e testes de interface. **Relaciona:** RF-01 a RF-13. **Personas:** SOL, RES, ADM.
- **RNF-10:** Estados e ações disponíveis de reserva devem ser identificados por texto e não somente por cor, ícone ou posição visual. **Verificação:** inspeção de todas as telas de consulta, criação, alteração, aprovação e cancelamento. **Relaciona:** RF-04, RF-07, RF-08 e RF-13. **Personas:** SOL, RES, ADM.

## 5. Confiabilidade

- **RNF-11:** Uma falha durante criação, alteração ou cancelamento não deve deixar dados parciais nem alterar indevidamente a disponibilidade de salas, professores ou materiais. **Verificação:** injeção controlada de falhas em cada etapa de persistência e conferência do estado final. **Relaciona:** RF-04, RF-05, RF-06, RF-08 e RF-09. **Personas:** SOL, RES, ADM.
- **RNF-12:** A indisponibilidade da integração de notificação não deve desfazer uma operação de reserva válida; a falha deve ficar registrada para consulta operacional. **Verificação:** teste com serviço de notificação indisponível. **Relaciona:** RF-04, RF-10 e RF-11. **Personas:** SOL, RES, ADM.
- **RNF-13:** Cada mudança relevante concluída deve ter registro de auditoria associado ao usuário, ação, entidade afetada e data/hora; usuários sem permissão não podem alterá-lo. **Verificação:** comparação de operações executadas com os registros de auditoria e tentativa controlada de alteração não autorizada. **Relaciona:** RF-04, RF-07, RF-08, RF-09 e RF-10. **Personas:** SOL, RES, ADM.

## 6. Segurança

- **RNF-14:** Senhas não devem ser persistidas em texto simples; a inspeção dos dados de autenticação deve confirmar que somente material de verificação protegido é armazenado. **Relaciona:** RF-01. **Personas:** SOL, RES, ADM.
- **RNF-15:** Toda operação protegida deve verificar autenticação, perfil e autorização no servidor; tentativas sem permissão devem ser negadas sem alterar dados. **Verificação:** testes positivos e negativos para cada perfil e operação. **Relaciona:** RF-01 a RF-12. **Personas:** SOL, RES, ADM.
- **RNF-16:** Entradas recebidas pelo servidor devem ser validadas e consultas persistentes devem usar parâmetros, impedindo que conteúdo de entrada altere indevidamente comandos ou consultas. **Verificação:** testes de validação de entrada e revisão automatizada/manual das operações de persistência. **Relaciona:** RF-02 a RF-12. **Personas:** SOL, RES, ADM.
- **RNF-17:** Credenciais de sessão e dados transmitidos entre frontend e servidor devem usar canais protegidos em produção; cookies de sessão, quando utilizados, devem ter atributos de segurança apropriados ao contexto. **Verificação:** inspeção de configuração e teste de transporte em ambiente de produção controlado. **Relaciona:** RF-01 a RF-14. **Personas:** SOL, RES, ADM.
- **RNF-18:** Respostas apresentadas no frontend não devem expor credenciais, dados pessoais sem necessidade, caminhos internos ou detalhes de implementação. **Verificação:** revisão das respostas de erro e dos dados retornados em operações por perfil. **Relaciona:** RF-01 a RF-14. **Personas:** SOL, RES, ADM.

## 7. Manutenibilidade

- **RNF-19:** A implementação deve manter separação verificável entre frontend/View, Controllers, regras de negócio/Model e persistência, para que alterações em uma camada não exijam mudanças não relacionadas nas demais. **Verificação:** revisão da estrutura e testes das camadas. **Relaciona:** RF-01 a RF-14. **Personas:** SOL, RES, ADM.
- **RNF-20:** Regras de conflito, autorização, manutenção e disponibilidade de materiais devem ter testes automatizados para resultados permitidos, rejeitados e concorrentes. **Verificação:** relatório de testes vinculados às regras. **Relaciona:** RF-01, RF-04 a RF-10. **Personas:** SOL, RES, ADM.
- **RNF-21:** Cada endpoint ou fluxo público do frontend deve ter contrato documentado com finalidade, dados de entrada, resposta, erros e requisitos de autorização; alterações devem atualizar a documentação correspondente. **Verificação:** revisão cruzada entre contratos publicados e documentação. **Relaciona:** RF-01 a RF-14. **Personas:** SOL, RES, ADM.

## 8. Portabilidade

- **RNF-22:** A aplicação deve ser executável nos ambientes de desenvolvimento, teste controlado e produção sem alteração do código-fonte; diferenças devem ser externas ao código e documentadas. A interface deve preservar os fluxos principais nos navegadores definidos no RNF-06. **Verificação:** implantação e execução da suíte de fumaça em cada ambiente. **Relaciona:** RF-01 a RF-14. **Personas:** SOL, RES, ADM.

## Perfil do teste controlado de 2.000 usuários simultâneos

- O teste de carga deve representar os perfis e fluxos reais: consultas e reservas do Solicitante, aprovações e movimentações do Responsável, e consultas/gestão do Administrador.
- A carga simultânea significa usuários com sessões ativas realizando operações no período do teste; não significa apenas 2.000 contas cadastradas.
- O cenário deve incluir pesquisas por disponibilidade, tentativas concorrentes sobre recursos coincidentes, consultas de relatórios, operações de aprovação e uso das telas frontend.
- O relatório deve registrar ambiente, configuração, dados de teste, duração, distribuição das operações, latências p50/p95/p99, taxa de erros/timeouts e resultado por perfil.
- O teste deve verificar consistência após a execução: ausência de dupla reserva, alocações parciais ou divergência entre estado exibido pela interface e estado persistido.
