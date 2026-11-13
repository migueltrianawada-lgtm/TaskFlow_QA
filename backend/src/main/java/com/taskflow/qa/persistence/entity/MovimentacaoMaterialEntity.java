package com.taskflow.qa.persistence.entity;

import com.taskflow.qa.domain.TipoMovimentacao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "movimentacoes_material")
public class MovimentacaoMaterialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reserva_id", nullable = false)
    private ReservaEntity reserva;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recurso_id", nullable = false)
    private RecursoEntity recurso;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "responsavel_id", nullable = false)
    private UsuarioEntity responsavel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private TipoMovimentacao tipo;

    @Positive
    @Column(nullable = false)
    private int quantidade;

    @Column(name = "registrada_em", nullable = false, updatable = false)
    private Instant registradaEm = Instant.now();

    protected MovimentacaoMaterialEntity() {
    }

    public MovimentacaoMaterialEntity(ReservaEntity reserva, RecursoEntity recurso, UsuarioEntity responsavel,
                                      TipoMovimentacao tipo, int quantidade) {
        this.reserva = Objects.requireNonNull(reserva, "reserva é obrigatória");
        this.recurso = Objects.requireNonNull(recurso, "recurso é obrigatório");
        this.responsavel = Objects.requireNonNull(responsavel, "responsável é obrigatório");
        this.tipo = Objects.requireNonNull(tipo, "tipo de movimentação é obrigatório");
        if (quantidade <= 0) {
            throw new IllegalArgumentException("quantidade deve ser positiva");
        }
        this.quantidade = quantidade;
    }

    public Long getId() {
        return id;
    }

    public ReservaEntity getReserva() {
        return reserva;
    }

    public RecursoEntity getRecurso() {
        return recurso;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public UsuarioEntity getResponsavel() {
        return responsavel;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public Instant getRegistradaEm() {
        return registradaEm;
    }
}
