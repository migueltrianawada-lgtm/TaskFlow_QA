# Diagrama estático de classes e entidades

## Escopo

Modelo lógico das entidades e associações persistentes. Ele delimita os conceitos do domínio sem definir tipos físicos, sintaxe ou fabricante de banco de dados. A classe `Recurso` representa sala, professor ou material/equipamento; seus atributos opcionais são aplicáveis conforme o tipo.

```mermaid
classDiagram
    class Usuario {
      +id
      +nome
      +email
      +senhaHash
      +ativo
      +criadoEm
    }
    class Perfil {
      +id
      +nome
    }
    class UsuarioPerfil {
      +usuarioId
      +perfilId
    }
    class Recurso {
      +id
      +tipo
      +usuarioDocenteId
      +nome
      +capacidade
      +localizacao
      +competencia
      +quantidadeTotal
      +restrito
      +ativo
    }
    class Reserva {
      +id
      +solicitanteId
      +inicio
      +fim
      +estado
      +finalidade
      +criadaEm
    }
    class ReservaRecurso {
      +reservaId
      +recursoId
      +quantidade
    }
    class Aprovacao {
      +id
      +reservaId
      +responsavelId
      +decisao
      +justificativa
      +decididaEm
    }
    class BloqueioManutencao {
      +id
      +recursoId
      +inicio
      +fim
      +justificativa
      +criadoPor
    }
    class MovimentacaoMaterial {
      +id
      +reservaId
      +recursoId
      +responsavelId
      +tipo
      +quantidade
      +registradaEm
    }
    class Auditoria {
      +id
      +usuarioId
      +entidade
      +entidadeId
      +acao
      +dadosAnteriores
      +dadosNovos
      +ocorridaEm
    }
    class EventoConflito {
      +id
      +usuarioId
      +recursoId
      +inicio
      +fim
      +motivo
      +ocorridoEm
    }
    class Notificacao {
      +id
      +destinatarioId
      +reservaId
      +canal
      +estado
      +mensagem
      +criadaEm
      +enviadaEm
    }

    Usuario "1" --> "0..*" UsuarioPerfil : possui
    Perfil "1" --> "0..*" UsuarioPerfil : atribuido
    Usuario "0..1" --> "0..1" Recurso : representa docente
    Usuario "1" --> "0..*" Reserva : solicita
    Reserva "1" --> "1..*" ReservaRecurso : aloca
    Recurso "1" --> "0..*" ReservaRecurso : participa
    Reserva "1" --> "0..*" Aprovacao : recebe
    Usuario "1" --> "0..*" Aprovacao : decide
    Recurso "1" --> "0..*" BloqueioManutencao : possui bloqueio
    Reserva "1" --> "0..*" MovimentacaoMaterial : origina
    Recurso "1" --> "0..*" MovimentacaoMaterial : movimentado
    Usuario "1" --> "0..*" MovimentacaoMaterial : registra
    Usuario "1" --> "0..*" Auditoria : executa
    Usuario "1" --> "0..*" EventoConflito : inicia tentativa
    Recurso "1" --> "0..*" EventoConflito : alvo
    Usuario "1" --> "0..*" Notificacao : recebe
    Reserva "0..1" --> "0..*" Notificacao : relacionada
```

## Definições e regras do modelo

- **Usuario / Perfil / UsuarioPerfil:** identidade, autenticação e associação aos perfis Solicitante, Responsável e Administrador. Senhas são representadas apenas por hash.
- **Recurso:** sala, professor ou material/equipamento. Capacidade, localização, competência e quantidade aplicam-se conforme o tipo.
- **Reserva / ReservaRecurso:** solicitação com período e associação aos recursos; a quantidade atende a recursos inventariáveis.
- **Aprovacao:** decisão do Responsável sobre reserva que exige aprovação, incluindo autoria, justificativa e momento.
- **BloqueioManutencao:** indisponibilidade temporal de um recurso.
- **MovimentacaoMaterial:** retirada ou devolução de material/equipamento por quantidade e responsável.
- **Auditoria:** registro append-only das ações relevantes, associado ao usuário e à entidade afetada.
- **EventoConflito:** tentativa rejeitada por conflito, usada para calcular conflitos evitados no relatório RF-12.
- **Notificacao:** registro de notificação simulada ou integrada, incluindo estado de falha.

Intervalos de reserva e bloqueio têm início anterior ao fim. Para salas e professores não pode haver reserva sobreposta. Para materiais/equipamentos, a soma de quantidades alocadas no intervalo não pode superar o estoque disponível. A validação e a gravação devem ser protegidas contra concorrência.
