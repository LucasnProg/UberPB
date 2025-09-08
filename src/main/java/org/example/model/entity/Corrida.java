package org.example.model.entity;

import java.time.LocalDateTime;

public class Corrida {
    private int id;
    private int passageiroId;
    private int motoristaId;
    private String origem; //Trocar tipo no futuro para localização
    private String destino;
    private double valor;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFim;
    private CategoriaVeiculo categoriaVeiculo;
    private StatusCorrida status;

    public Corrida(int passageiroId, String origem, String destino, CategoriaVeiculo categoriaVeiculo) {
        this.passageiroId = passageiroId;
        this.origem = origem;
        this.destino = destino;
        this.categoriaVeiculo = categoriaVeiculo;
        //this.horaInicio = LocalDateTime.now(); //Mover para iniciarCorrida()
        this.status = StatusCorrida.SOLICITADA; // quando uma instancia de corrida for criada ela será por padrão classificada como solicitaada
    }

    public int getId() {
        return id;
    }

    public int getPassageiroId() {
        return passageiroId;
    }

    public void setPassageiroId(int passageiroId) {
        this.passageiroId = passageiroId;
    }

    public int getMotoristaId() {
        return motoristaId;
    }

    public void setMotoristaId(int motoristaId) {
        this.motoristaId = motoristaId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDateTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalDateTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalDateTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalDateTime horaFim) {
        this.horaFim = horaFim;
    }

    public CategoriaVeiculo getCategoriaVeiculo() {
        return categoriaVeiculo;
    }

    public void setCategoriaVeiculo(CategoriaVeiculo categoriaVeiculo) {
        this.categoriaVeiculo = categoriaVeiculo;
    }

    public StatusCorrida getStatus() {
        return status;
    }

    public void setStatus(StatusCorrida status) {
        this.status = status;
    }
}