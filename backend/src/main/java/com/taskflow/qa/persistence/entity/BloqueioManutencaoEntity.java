package com.taskflow.qa.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.AssertTrue;
import java.time.Instant;
import java.util.Objects;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "bloqueios_manutencao")
@Check(constraints = "fim > inicio")
public class BloqueioManutencaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recurso_id", nullable = false)
    private RecursoEntity recurso;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "criado_por_id", nullable = false)
    private UsuarioEntity criadoPor;

    @Column(nullable = false)
    private Instant inicio;

    @Column(nullable = false)
    private Instant fim;

    @NotBlank
    @Column(nullable = false, length = 500)
    private String justificativa;

    protected BloqueioManutencaoEntity() {
    }

    public BloqueioManutencaoEntity(RecursoEntity recurso, UsuarioEntity criadoPor, Instant inicio, Instant fim,
                                    String justificativa) {
        this.recurso = Objects.requireNonNull(recurso, "recurso é obrigatório");
        this.criadoPor = Objects.requireNonNull(criadoPor, "autor do bloqueio é obrigatório");
        this.inicio = Objects.requireNonNull(inicio, "início é obrigatório");
        this.fim = Objects.requireNonNull(fim, "fim é obrigatório");
        if (!inicio.isBefore(fim)) {
            throw new IllegalArgumentException("fim deve ser posterior ao início");
        }
        this.justificativa = Objects.requireNonNull(justificativa, "justificativa é obrigatória").trim();
        if (this.justificativa.isEmpty()) {
            throw new IllegalArgumentException("justificativa é obrigatória");
        }
    }

    public Long getId() {
        return id;
    }

    public RecursoEntity getRecurso() {
        return recurso;
    }

    public Instant getInicio() {
        return inicio;
    }

    public Instant getFim() {
        return fim;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public UsuarioEntity getCriadoPor() {
        return criadoPor;
    }

    @AssertTrue(message = "O fim do bloqueio deve ser posterior ao início")
    private boolean isIntervaloValido() {
        return inicio != null && fim != null && inicio.isBefore(fim);
    }
}
