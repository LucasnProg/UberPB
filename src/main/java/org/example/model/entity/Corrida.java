package org.example.model.entity;

import org.example.util.StatusCorrida;

import java.time.LocalDateTime;

public class Corrida {
    private int id;
    private Passageiro passageiro;
    private Motorista motorista;
    private String origem;
    private String destino;
    private double valor;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFim;
    private String categoriaVeiculo;
    private StatusCorrida status;
    private boolean concluida;

    public Corrida(Passageiro passageiro, String origem, String destino, String categoriaVeiculo) {
        this.passageiro = passageiro;
        this.origem = origem;
        this.destino = destino;
        this.categoriaVeiculo = categoriaVeiculo;
        this.horaInicio = LocalDateTime.now();
        this.status = StatusCorrida.SOLICITADA; // quando uma instancia de corrida for criada ela será por padrão classificada como solicitaada
        this.concluida = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Passageiro getPassageiro() {
        return passageiro;
    }

    public void setPassageiro(Passageiro passageiro) {
        this.passageiro = passageiro;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
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

    public String getCategoriaVeiculo() {
        return categoriaVeiculo;
    }

    public void setCategoriaVeiculo(String categoriaVeiculo) {
        this.categoriaVeiculo = categoriaVeiculo;
    }

    public StatusCorrida getStatus() {
        return status;
    }

    public void setStatus(StatusCorrida status) {
        this.status = status;
    }
}