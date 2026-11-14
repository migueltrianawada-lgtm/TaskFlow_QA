package com.taskflow.qa.persistence.entity;

import com.taskflow.qa.domain.TipoRecurso;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.AssertTrue;
import java.util.Objects;

@Entity
@Table(name = "recursos")
public class RecursoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 160)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private TipoRecurso tipo;

    @Column(length = 160)
    private String localizacao;

    @Column(length = 240)
    private String competencia;

    @PositiveOrZero
    private Integer capacidade;

    @PositiveOrZero
    @Column(name = "quantidade_total")
    private Integer quantidadeTotal;

    @Column(nullable = false)
    private boolean restrito;

    @Column(nullable = false)
    private boolean ativo = true;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_usuario_id")
    private UsuarioEntity docente;

    protected RecursoEntity() {
    }

    public RecursoEntity(String nome, TipoRecurso tipo, String localizacao, String competencia,
                         Integer capacidade, Integer quantidadeTotal, boolean restrito) {
        this.nome = Objects.requireNonNull(nome, "nome é obrigatório").trim();
        this.tipo = Objects.requireNonNull(tipo, "tipo é obrigatório");
        this.localizacao = localizacao;
        this.competencia = competencia;
        this.capacidade = capacidade;
        this.quantidadeTotal = quantidadeTotal;
        this.restrito = restrito;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public TipoRecurso getTipo() {
        return tipo;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public String getCompetencia() {
        return competencia;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public Integer getQuantidadeTotal() {
        return quantidadeTotal;
    }

    public boolean isRestrito() {
        return restrito;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public UsuarioEntity getDocente() {
        return docente;
    }

    public void setDocente(UsuarioEntity docente) {
        this.docente = docente;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void atualizar(String nome, TipoRecurso tipo, String localizacao, String competencia,
                          Integer capacidade, Integer quantidadeTotal, boolean restrito,
                          UsuarioEntity docente) {
        this.nome = Objects.requireNonNull(nome, "nome é obrigatório").trim();
        this.tipo = Objects.requireNonNull(tipo, "tipo é obrigatório");
        this.localizacao = localizacao;
        this.competencia = competencia;
        this.capacidade = capacidade;
        this.quantidadeTotal = quantidadeTotal;
        this.restrito = restrito;
        this.docente = docente;
    }

    @AssertTrue(message = "Os atributos do recurso devem corresponder ao tipo")
    private boolean isAtributosCompativeisComTipo() {
        if (tipo == null) {
            return false;
        }
        return switch (tipo) {
            case SALA -> capacidade != null && capacidade > 0 && quantidadeTotal == null && docente == null;
            case PROFESSOR -> competencia != null && !competencia.isBlank()
                    && quantidadeTotal == null && docente != null;
            case MATERIAL, EQUIPAMENTO -> capacidade == null && quantidadeTotal != null
                    && quantidadeTotal >= 0 && docente == null;
        };
    }
}
