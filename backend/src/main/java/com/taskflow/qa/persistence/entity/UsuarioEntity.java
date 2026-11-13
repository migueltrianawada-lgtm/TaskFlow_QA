package com.taskflow.qa.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Entity
@Table(name = "usuarios", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String nome;

    @Email
    @NotBlank
    @Column(nullable = false, length = 254)
    private String email;

    @NotBlank
    @Column(name = "senha_hash", nullable = false, length = 255)
    private String senhaHash;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UsuarioPerfilEntity> perfis = new ArrayList<>();

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm = Instant.now();

    protected UsuarioEntity() {
    }

    public UsuarioEntity(String nome, String email, String senhaHash) {
        this.nome = Objects.requireNonNull(nome, "nome é obrigatório").trim();
        this.email = Objects.requireNonNull(email, "email é obrigatório").trim().toLowerCase(Locale.ROOT);
        this.senhaHash = Objects.requireNonNull(senhaHash, "hash da senha é obrigatório");
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public List<UsuarioPerfilEntity> getPerfis() {
        return List.copyOf(perfis);
    }

    public boolean isAtivo() {
        return ativo;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public void adicionarPerfil(PerfilEntity perfil) {
        perfis.add(new UsuarioPerfilEntity(this, perfil));
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
