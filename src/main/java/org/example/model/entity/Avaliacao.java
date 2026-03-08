package org.example.model.entity;

import java.time.LocalDateTime;

/**
 * Avaliação (Rating/Review)
 *
 * Estrutura genérica para suportar avaliações em:
 * - Corridas: Passageiro <-> Motorista
 * - Pedidos: Cliente <-> Entregador/Restaurante
 */
public class Avaliacao {

    private int id;

    // Quem avaliou
    private TipoParteAvaliacao tipoAvaliador;
    private int avaliadorId;

    // Quem foi avaliado
    private TipoParteAvaliacao tipoAvaliado;
    private int avaliadoId;

    // Contexto
    private TipoAvaliacaoContexto contexto;

    // Referência ao contexto (apenas um deve ser preenchido)
    private Integer corridaId;
    private Integer pedidoId;

    // Conteúdo
    private int nota; // 1..5
    private String comentario;
    private LocalDateTime dataHora;

    /** Construtor padrão (necessário para Gson). */
    public Avaliacao() {}

    public Avaliacao(
            TipoParteAvaliacao tipoAvaliador,
            int avaliadorId,
            TipoParteAvaliacao tipoAvaliado,
            int avaliadoId,
            TipoAvaliacaoContexto contexto,
            Integer corridaId,
            Integer pedidoId,
            int nota,
            String comentario
    ) {
        this.tipoAvaliador = tipoAvaliador;
        this.avaliadorId = avaliadorId;
        this.tipoAvaliado = tipoAvaliado;
        this.avaliadoId = avaliadoId;
        this.contexto = contexto;
        this.corridaId = corridaId;
        this.pedidoId = pedidoId;
        this.nota = nota;
        this.comentario = comentario;
        this.dataHora = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public TipoParteAvaliacao getTipoAvaliador() { return tipoAvaliador; }
    public void setTipoAvaliador(TipoParteAvaliacao tipoAvaliador) { this.tipoAvaliador = tipoAvaliador; }

    public int getAvaliadorId() { return avaliadorId; }
    public void setAvaliadorId(int avaliadorId) { this.avaliadorId = avaliadorId; }

    public TipoParteAvaliacao getTipoAvaliado() { return tipoAvaliado; }
    public void setTipoAvaliado(TipoParteAvaliacao tipoAvaliado) { this.tipoAvaliado = tipoAvaliado; }

    public int getAvaliadoId() { return avaliadoId; }
    public void setAvaliadoId(int avaliadoId) { this.avaliadoId = avaliadoId; }

    public TipoAvaliacaoContexto getContexto() { return contexto; }
    public void setContexto(TipoAvaliacaoContexto contexto) { this.contexto = contexto; }

    public Integer getCorridaId() { return corridaId; }
    public void setCorridaId(Integer corridaId) { this.corridaId = corridaId; }

    public Integer getPedidoId() { return pedidoId; }
    public void setPedidoId(Integer pedidoId) { this.pedidoId = pedidoId; }

    public int getNota() { return nota; }
    public void setNota(int nota) { this.nota = nota; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
}