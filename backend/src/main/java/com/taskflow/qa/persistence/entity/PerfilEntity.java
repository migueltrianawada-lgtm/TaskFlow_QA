package com.taskflow.qa.persistence.entity;

import com.taskflow.qa.domain.PerfilAcesso;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "perfis", uniqueConstraints = @UniqueConstraint(columnNames = "nome"))
public class PerfilEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private PerfilAcesso nome;

    protected PerfilEntity() {
    }

    public PerfilEntity(PerfilAcesso nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public PerfilAcesso getNome() {
        return nome;
    }
}
