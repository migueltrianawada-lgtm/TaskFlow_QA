package com.taskflow.qa.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Positive;
import java.util.Objects;

@Entity
@Table(name = "reserva_recursos",
        uniqueConstraints = @UniqueConstraint(columnNames = {"reserva_id", "recurso_id"}))
public class ReservaRecursoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reserva_id", nullable = false)
    private ReservaEntity reserva;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recurso_id", nullable = false)
    private RecursoEntity recurso;

    @Positive
    private int quantidade;

    protected ReservaRecursoEntity() {
    }

    ReservaRecursoEntity(ReservaEntity reserva, RecursoEntity recurso, int quantidade) {
        this.reserva = Objects.requireNonNull(reserva, "reserva é obrigatória");
        this.recurso = Objects.requireNonNull(recurso, "recurso é obrigatório");
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
}
