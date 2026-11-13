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

@Entity
@Table(name = "auditorias")
public class AuditoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @NotBlank
    @Column(nullable = false, length = 80)
    private String acao;

    @NotBlank
    @Column(name = "tipo_entidade", nullable = false, length = 80)
    private String tipoEntidade;

    @Column(name = "entidade_id")
    private Long entidadeId;

    @Column(name = "dados_anteriores", columnDefinition = "text")
    private String dadosAnteriores;

    @Column(name = "dados_novos", columnDefinition = "text")
    private String dadosNovos;

    @Column(name = "ocorrida_em", nullable = false, updatable = false)
    private Instant ocorridaEm = Instant.now();

    protected AuditoriaEntity() {
    }

    public AuditoriaEntity(UsuarioEntity usuario, String acao, String tipoEntidade, Long entidadeId) {
        this.usuario = Objects.requireNonNull(usuario, "usuário é obrigatório");
        this.acao = Objects.requireNonNull(acao, "ação é obrigatória").trim();
        this.tipoEntidade = Objects.requireNonNull(tipoEntidade, "tipo da entidade é obrigatório").trim();
        this.entidadeId = entidadeId;
    }

    public AuditoriaEntity(UsuarioEntity usuario, String acao, String tipoEntidade, Long entidadeId,
                          String dadosAnteriores, String dadosNovos) {
        this(usuario, acao, tipoEntidade, entidadeId);
        this.dadosAnteriores = dadosAnteriores;
        this.dadosNovos = dadosNovos;
    }

    public Long getId() {
        return id;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public String getAcao() {
        return acao;
    }

    public String getTipoEntidade() {
        return tipoEntidade;
    }

    public Long getEntidadeId() {
        return entidadeId;
    }

    public Instant getOcorridaEm() {
        return ocorridaEm;
    }

    public String getDadosAnteriores() {
        return dadosAnteriores;
    }

    public String getDadosNovos() {
        return dadosNovos;
    }
}
