package org.example.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma Corrida no sistema, contendo todas as informações da viagem.
 */
public class Corrida {
    private int id;
    private int passageiroId;
    private int motoristaId;
    private Localizacao origem;
    private Localizacao destino;
    private double valor;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFim;
    private CategoriaVeiculo categoriaVeiculo;
    private StatusCorrida status;
    private List<Integer> motoristasQueRejeitaram = new ArrayList<>();
    private FormaPagamento formaPagamento;
    private int avaliacaoMotorista;
    private int avaliacaoPassageiro;
    private boolean motoristaAvaliou = false;
    private boolean passageiroAvaliou = false;

    private boolean precisaTroco;
    /**
     * Construtor padrão utilizado para criar objetos temporários (ex: estimativas).
     */
    public Corrida() {}

    /**
     * Construtor principal para criar uma nova solicitação de corrida.
     * @param passageiroId O ID do passageiro que solicitou a corrida.
     * @param origem O local de partida.
     * @param destino O local de chegada.
     * @param categoriaVeiculo A categoria de veículo desejada.
     */
    public Corrida(int passageiroId, Localizacao origem, Localizacao destino, CategoriaVeiculo categoriaVeiculo) {
        this.passageiroId = passageiroId;
        this.origem = origem;
        this.destino = destino;
        this.categoriaVeiculo = categoriaVeiculo;
        this.status = StatusCorrida.SOLICITADA;
    }

    public List<Integer> getMotoristasQueRejeitaram() {
        return motoristasQueRejeitaram;
    }

    public void setMotoristasQueRejeitaram(List<Integer> motoristasQueRejeitaram) {
        this.motoristasQueRejeitaram = motoristasQueRejeitaram;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
    public void adicionarRejeicao(int motoristaId) {
        this.motoristasQueRejeitaram.add(motoristaId);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPassageiroId() { return passageiroId; }
    public void setPassageiroId(int passageiroId) { this.passageiroId = passageiroId; }
    public int getMotoristaId() { return motoristaId; }
    public void setMotoristaId(int motoristaId) { this.motoristaId = motoristaId; }
    public Localizacao getOrigem() { return origem; }
    public void setOrigem(Localizacao origem) { this.origem = origem; }
    public Localizacao getDestino() { return destino; }
    public void setDestino(Localizacao destino) { this.destino = destino; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public LocalDateTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalDateTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalDateTime getHoraFim() { return horaFim; }
    public void setHoraFim(LocalDateTime horaFim) { this.horaFim = horaFim; }
    public CategoriaVeiculo getCategoriaVeiculo() { return categoriaVeiculo; }
    public void setCategoriaVeiculo(CategoriaVeiculo categoriaVeiculo) { this.categoriaVeiculo = categoriaVeiculo; }
    public StatusCorrida getStatus() { return status; }
    public void setStatus(StatusCorrida status) { this.status = status; }
    public boolean isPrecisaTroco() { return precisaTroco;}
    public void setPrecisaTroco(boolean precisaTroco) {this.precisaTroco = precisaTroco;}
    public int getAvaliacaoMotorista() {
        return avaliacaoMotorista;
    }
    public void setAvaliacaoMotorista(int avaliacaoMotorista) {
        this.avaliacaoMotorista = avaliacaoMotorista;
    }
    public int getAvaliacaoPassageiro() {
        return avaliacaoPassageiro;
    }
    public void setAvaliacaoPassageiro(int avaliacaoPassageiro) {
        this.avaliacaoPassageiro = avaliacaoPassageiro;
    }
    public boolean isMotoristaAvaliou() { return motoristaAvaliou; }
    public void setMotoristaAvaliou(boolean motoristaAvaliou) { this.motoristaAvaliou = motoristaAvaliou; }

    public boolean isPassageiroAvaliou() { return passageiroAvaliou; }
    public void setPassageiroAvaliou(boolean passageiroAvaliou) { this.passageiroAvaliou = passageiroAvaliou; }
}