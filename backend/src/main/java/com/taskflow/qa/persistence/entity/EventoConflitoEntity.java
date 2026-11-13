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
import java.time.Instant;
import java.util.Objects;
import jakarta.validation.constraints.AssertTrue;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "eventos_conflito")
@Check(constraints = "intervalo_fim > intervalo_inicio")
public class EventoConflitoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recurso_id", nullable = false)
    private RecursoEntity recurso;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @Column(name = "intervalo_inicio", nullable = false)
    private Instant intervaloInicio;

    @Column(name = "intervalo_fim", nullable = false)
    private Instant intervaloFim;

    @NotBlank
    @Column(nullable = false, length = 500)
    private String motivo;

    @Column(name = "registrado_em", nullable = false, updatable = false)
    private Instant registradoEm = Instant.now();

    protected EventoConflitoEntity() {
    }

    public EventoConflitoEntity(RecursoEntity recurso, UsuarioEntity usuario, Instant intervaloInicio,
                                Instant intervaloFim, String motivo) {
        this.recurso = Objects.requireNonNull(recurso, "recurso é obrigatório");
        this.usuario = Objects.requireNonNull(usuario, "usuário é obrigatório");
        this.intervaloInicio = Objects.requireNonNull(intervaloInicio, "início é obrigatório");
        this.intervaloFim = Objects.requireNonNull(intervaloFim, "fim é obrigatório");
        if (!intervaloInicio.isBefore(intervaloFim)) {
            throw new IllegalArgumentException("fim deve ser posterior ao início");
        }
        this.motivo = Objects.requireNonNull(motivo, "motivo é obrigatório").trim();
        if (this.motivo.isEmpty()) {
            throw new IllegalArgumentException("motivo é obrigatório");
        }
    }

    public Long getId() {
        return id;
    }

    public RecursoEntity getRecurso() {
        return recurso;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public Instant getIntervaloInicio() {
        return intervaloInicio;
    }

    public Instant getIntervaloFim() {
        return intervaloFim;
    }

    public String getMotivo() {
        return motivo;
    }

    public Instant getRegistradoEm() {
        return registradoEm;
    }

    @AssertTrue(message = "O fim do evento deve ser posterior ao início")
    private boolean isIntervaloValido() {
        return intervaloInicio != null && intervaloFim != null && intervaloInicio.isBefore(intervaloFim);
    }
}
