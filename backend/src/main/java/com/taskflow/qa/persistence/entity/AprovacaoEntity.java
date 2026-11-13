package com.taskflow.qa.persistence.entity;

import com.taskflow.qa.domain.StatusAprovacao;
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
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "aprovacoes")
public class AprovacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reserva_id", nullable = false)
    private ReservaEntity reserva;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "responsavel_id", nullable = false)
    private UsuarioEntity responsavel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private StatusAprovacao decisao;

    @Column(length = 1000)
    private String justificativa;

    @Column(name = "decidida_em", nullable = false, updatable = false)
    private Instant decididaEm = Instant.now();

    protected AprovacaoEntity() {
    }

    public AprovacaoEntity(ReservaEntity reserva, UsuarioEntity responsavel, StatusAprovacao decisao,
                           String justificativa) {
        this.reserva = Objects.requireNonNull(reserva, "reserva é obrigatória");
        this.responsavel = Objects.requireNonNull(responsavel, "responsável é obrigatório");
        this.decisao = Objects.requireNonNull(decisao, "decisão é obrigatória");
        this.justificativa = justificativa == null ? null : justificativa.trim();
    }

    public Long getId() {
        return id;
    }

    public ReservaEntity getReserva() {
        return reserva;
    }

    public UsuarioEntity getResponsavel() {
        return responsavel;
    }

    public StatusAprovacao getDecisao() {
        return decisao;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public Instant getDecididaEm() {
        return decididaEm;
    }
}
