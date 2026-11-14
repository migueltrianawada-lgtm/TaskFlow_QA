# Plano de desempenho — PERF-01

## Objetivo e critério

Verificar o requisito RNF-03/RNF-05 de eficiência para 2.000 usuários autenticados simultâneos. O critério de aprovação definido no requisito é: p95 até 2 s para consultas de disponibilidade, p95 até 3 s para as demais operações interativas, p95 até 5 s para relatórios e menos de 1% de erros/timeouts, medidos da ação no frontend até a resposta renderizada.

## Preparação do ambiente

1. Usar ambiente isolado de teste, nunca produção, com configuração de aplicação, banco e rede comparável à implantação pretendida.
2. Registrar SHA do código, versões de Java/Node/PostgreSQL, CPU/memória, configuração do pool de conexões, topologia, região e parâmetros do banco.
3. Criar contas sintéticas para os perfis e massa de dados representativa de recursos, docentes, reservas, bloqueios e materiais. Não usar dados pessoais reais.
4. Confirmar saúde da aplicação, estado inicial e limpeza/recriação dos dados entre execuções.
5. Não incluir credenciais no repositório nem nos relatórios; injetá-las pelo ambiente seguro do executor.

## Perfil de carga e execução

- 2.000 sessões autenticadas com rampa documentada até o nível-alvo; manter a carga estável por pelo menos 15 minutos após aquecimento.
- Misturar consulta por disponibilidade, listagem de reservas, criação/alteração/cancelamento, aprovação, movimentação de materiais e relatório, distribuídos entre os três perfis.
- Incluir pedidos concorrentes ao mesmo recurso/período e também a recursos distintos.
- Repetir pelo menos três vezes; registrar cada execução separadamente, sem combinar percentis de execuções diferentes.
- Coletar p50/p95/p99 e máximo, taxa de respostas por segundo, erros HTTP, timeouts, uso de CPU/memória, pool e conexões/latência do banco.
- Conferir ao final que não houve dupla reserva, movimentação acima do reservado, escrita parcial ou divergência entre resposta e estado persistido.
- Avaliar também a jornada renderizada no navegador para os limiares RNF-03/RNF-05; teste HTTP isolado não demonstra tempo de renderização.

## Ferramentas e segurança

O script `../../scripts/performance-smoke.py` é apenas um smoke de leitura autenticada para uma carga pequena local. Ele não é ferramenta de carga em larga escala, não cria dados e não deve ser interpretado como teste dos 2.000 usuários. Pode ser executado a partir da raiz do repositório, após iniciar a API e definir `TASKFLOW_USERNAME`/`TASKFLOW_PASSWORD`, com `python scripts/performance-smoke.py --base-url http://localhost:8080 --duration-seconds 10 --concurrency 10`.

O teste de aceitação completo deve usar um gerador de carga aprovado pela equipe e controlado pelo responsável do ambiente; configurar limites e monitoramento para evitar impacto em serviços compartilhados.

## Estado

O ensaio de 2.000 usuários ainda não foi executado. O resultado disponível está separado em `resultado-01.md` e não substitui esse teste.
