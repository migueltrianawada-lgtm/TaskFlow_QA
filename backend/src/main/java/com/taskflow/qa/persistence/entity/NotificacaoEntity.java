package com.taskflow.qa.persistence.entity;

import com.taskflow.qa.domain.StatusNotificacao;
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
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "notificacoes")
public class NotificacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserva_id")
    private ReservaEntity reserva;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "destinatario_id", nullable = false)
    private UsuarioEntity destinatario;

    @NotBlank
    @Column(nullable = false, length = 40)
    private String canal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private StatusNotificacao status = StatusNotificacao.PENDENTE;

    @Column(name = "detalhe_falha", length = 500)
    private String detalheFalha;

    @NotBlank
    @Column(nullable = false, length = 2000)
    private String mensagem;

    @Column(name = "criada_em", nullable = false, updatable = false)
    private Instant criadaEm = Instant.now();

    @Column(name = "enviada_em")
    private Instant enviadaEm;

    protected NotificacaoEntity() {
    }

    public NotificacaoEntity(ReservaEntity reserva, UsuarioEntity destinatario, String canal, String mensagem) {
        this.reserva = reserva;
        this.destinatario = Objects.requireNonNull(destinatario, "destinatário é obrigatório");
        this.canal = Objects.requireNonNull(canal, "canal é obrigatório").trim();
        this.mensagem = Objects.requireNonNull(mensagem, "mensagem é obrigatória").trim();
        if (this.canal.isEmpty() || this.mensagem.isEmpty()) {
            throw new IllegalArgumentException("canal e mensagem são obrigatórios");
        }
    }

    public Long getId() {
        return id;
    }

    public ReservaEntity getReserva() {
        return reserva;
    }

    public UsuarioEntity getDestinatario() {
        return destinatario;
    }

    public String getCanal() {
        return canal;
    }

    public StatusNotificacao getStatus() {
        return status;
    }

    public String getDetalheFalha() {
        return detalheFalha;
    }

    public Instant getCriadaEm() {
        return criadaEm;
    }

    public Instant getEnviadaEm() {
        return enviadaEm;
    }

    public void registrarEnvio(Instant enviadaEm) {
        this.enviadaEm = Objects.requireNonNull(enviadaEm, "data de envio é obrigatória");
        this.status = StatusNotificacao.ENVIADA;
        this.detalheFalha = null;
    }

    public void registrarFalha(String detalheFalha) {
        this.status = StatusNotificacao.FALHA;
        this.detalheFalha = Objects.requireNonNull(detalheFalha, "detalhe da falha é obrigatório");
        this.enviadaEm = null;
    }
}
