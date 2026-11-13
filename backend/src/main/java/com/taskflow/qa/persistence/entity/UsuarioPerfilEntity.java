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

@Entity
@Table(name = "usuario_perfis",
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "perfil_id"}))
public class UsuarioPerfilEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "perfil_id", nullable = false)
    private PerfilEntity perfil;

    protected UsuarioPerfilEntity() {
    }

    UsuarioPerfilEntity(UsuarioEntity usuario, PerfilEntity perfil) {
        this.usuario = usuario;
        this.perfil = perfil;
    }

    public Long getId() {
        return id;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public PerfilEntity getPerfil() {
        return perfil;
    }
}
