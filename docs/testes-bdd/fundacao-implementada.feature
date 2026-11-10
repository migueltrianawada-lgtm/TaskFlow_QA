# language: pt

@executavel @RF-05 @RNF-04 @RNF-20
Funcionalidade: Validar intervalos de reserva
  Para evitar conflitos de horário
  Como solicitante
  Quero que intervalos sobrepostos sejam rejeitados

  Cenário: Detectar intervalos sobrepostos
    Dado que existe uma reserva no intervalo de "10:00" a "11:00"
    Quando o usuario solicita o intervalo de "10:30" a "11:30"
    Então o sistema identifica sobreposicao

  Cenário: Permitir intervalos adjacentes
    Dado que existe uma reserva no intervalo de "10:00" a "11:00"
    Quando o usuario solicita o intervalo de "11:00" a "12:00"
    Então o sistema nao identifica sobreposicao

  Cenário: Rejeitar intervalo sem duração positiva
    Dado que existe uma reserva no intervalo de "10:00" a "11:00"
    Quando o usuario solicita o intervalo de "11:00" a "10:00"
    Então o sistema rejeita o intervalo como invalido
