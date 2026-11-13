package com.taskflow.qa.persistence.entity;

import com.taskflow.qa.domain.StatusReserva;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "reservas")
@Check(constraints = "fim > inicio")
public class ReservaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "solicitante_id", nullable = false)
    private UsuarioEntity solicitante;

    @Column(nullable = false)
    private Instant inicio;

    @Column(nullable = false)
    private Instant fim;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private StatusReserva status = StatusReserva.PENDENTE;

    @NotBlank
    @Column(nullable = false, length = 500)
    private String finalidade;

    @Column(name = "criada_em", nullable = false, updatable = false)
    private Instant criadaEm = Instant.now();

    @OneToMany(mappedBy = "reserva", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservaRecursoEntity> recursos = new ArrayList<>();

    protected ReservaEntity() {
    }

    public ReservaEntity(UsuarioEntity solicitante, Instant inicio, Instant fim, String finalidade) {
        this.solicitante = Objects.requireNonNull(solicitante, "solicitante é obrigatório");
        this.inicio = Objects.requireNonNull(inicio, "início é obrigatório");
        this.fim = Objects.requireNonNull(fim, "fim é obrigatório");
        if (!inicio.isBefore(fim)) {
            throw new IllegalArgumentException("fim deve ser posterior ao início");
        }
        this.finalidade = Objects.requireNonNull(finalidade, "finalidade é obrigatória").trim();
        if (this.finalidade.isEmpty()) {
            throw new IllegalArgumentException("finalidade é obrigatória");
        }
    }

    public Long getId() {
        return id;
    }

    public UsuarioEntity getSolicitante() {
        return solicitante;
    }

    public Instant getInicio() {
        return inicio;
    }

    public Instant getFim() {
        return fim;
    }

    public String getFinalidade() {
        return finalidade;
    }

    public StatusReserva getStatus() {
        return status;
    }

    public Instant getCriadaEm() {
        return criadaEm;
    }

    public List<ReservaRecursoEntity> getRecursos() {
        return List.copyOf(recursos);
    }

    public void adicionarRecurso(RecursoEntity recurso, int quantidade) {
        recursos.add(new ReservaRecursoEntity(this, recurso, quantidade));
    }

    public void setStatus(StatusReserva status) {
        this.status = Objects.requireNonNull(status, "status é obrigatório");
    }

    @AssertTrue(message = "O fim da reserva deve ser posterior ao início")
    private boolean isIntervaloValido() {
        return inicio != null && fim != null && inicio.isBefore(fim);
    }
}
