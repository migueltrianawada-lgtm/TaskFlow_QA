# Resultado de desempenho — PERF-01

## Conclusão

**A meta de 2.000 usuários foi exercitada, mas RNF-03/RNF-05 não foi aprovada.** A taxa de erros foi 0%, abaixo do limite de 1%, porém o p95 da consulta de disponibilidade foi **2.688,28 ms**, acima do limite de 2.000 ms. Além disso, o ensaio mediu resposta HTTP da API, não a jornada renderizada no navegador. Trata-se de uma execução local exploratória, não de validação de produção.

## Execução local de 2.000 usuários

- Código: commit `0425ed73bdedea13f093e489db737856fde19ef3`.
- Ambiente: API local TaskFlow em `127.0.0.1:18080`, Java 21/Spring Boot, perfil `dev`, H2 em memória descartável; não usou Neon nem PostgreSQL.
- Dados e perfis: 20 salas sintéticas; 2.000 contas sintéticas autenticadas com perfil `SOLICITANTE`.
- Operação: `GET /api/recursos` com período de disponibilidade `2026-11-16T12:00:00Z`–`13:00:00Z`.
- Perfil: rampa de 120 s, aquecimento de 60 s e carga estável por 905,01 s (15 minutos completos), com intervalo de 5 s entre consultas por usuário.
- Ajustes somente para este processo local: compilação JIT Java Tier 4; Tomcat com até 400 threads, 20 mínimas, fila de 1.000 e até 5.000 conexões; Hikari com pool máximo de 50 e mínimo de 10.
- Resultado da fase estável: 350.614 respostas; 0 falhas; 0% de erros; p50 9,56 ms, p95 2.688,28 ms, p99 7.762,61 ms e máximo 13.573,34 ms.
- Resultado total: 399.507 respostas HTTP 200, throughput agregado de 389,57 solicitações/s; 2.000 renovações de token sem falha; 36 verificações de saúde sem falha.

Os números de latência são da API e incluem efeitos observados durante essa execução; não medem tempo de renderização frontend. Os ajustes de JVM e pools não foram persistidos no repositório e não devem ser considerados configuração aprovada para produção. A distribuição exercitou somente leitura de disponibilidade com usuários solicitantes; não cobriu gravações concorrentes, outros perfis, relatórios, operações de materiais ou consistência de dados após escrita. CPU, memória e métricas detalhadas do banco não foram coletadas. Não há artifact externo da execução.

## Evidência local anterior

- Smoke local de 10 workers autenticados; GET `/api/recursos`; 20 salas sintéticas no H2; 10 s.
- Resultado: 2.440 respostas HTTP 200, 0 falhas, 239,63 solicitações/s; p50 28,89 ms, p95 107,43 ms, p99 197,66 ms, máximo 438,29 ms.
- Método: `scripts/performance-smoke.py`; medição HTTP local da API.

Esse smoke pequeno é uma execução separada e não deve ser combinado com os percentis do ensaio de 2.000 usuários.

## Comparação com a tentativa inicial

Uma execução anterior com 2.000 usuários e JVM limitada a `TieredStopAtLevel=1` teve 142 falhas em 310.041 consultas estáveis (0,0458%), p95 de 6.947 ms e p99 de 9.664 ms. A execução documentada acima, feita com JIT completo e pools ajustados apenas localmente, eliminou as falhas observadas, mas ainda excedeu o p95 de 2 s. As duas execuções são diagnósticas e nenhuma aprova o requisito.

## Pendências para resultado de aceitação

Executar medições repetidas em ambiente isolado e representativo da implantação, registrar recursos do executor e métricas de CPU/memória/banco, ampliar o perfil para as operações e perfis requeridos e medir a jornada renderizada no navegador. Verificar também concorrência de gravação e consistência pós-teste. Até essas evidências existirem e o p95 da disponibilidade atender ao limite, RNF-03/RNF-05 permanecem não aprovados.
