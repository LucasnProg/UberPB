package org.example.model.entity;

import java.util.ArrayList;
import java.util.List;

public class Motorista extends Usuario{

    private Localizacao localizacaoAtual;
    private int idVeiculo;
    private List<Corrida> corridasNotificadas = new ArrayList<Corrida>();
    private List<Corrida> corridasAceitas = new ArrayList<Corrida>();;
    private MotoristaStatus status;

    public Motorista(String nome, String email, String senha, String cpf, String telefone) {
        super(nome, email, senha, cpf, telefone);
    }

    public int getIdVeiculo() {
        return idVeiculo;
    }

    public void setIdVeiculo(int idVeiculo) {
        this.idVeiculo = idVeiculo;
    }

    public Localizacao getLocalizacao() {
        return localizacaoAtual;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacaoAtual = localizacao;
    }

    public List<Corrida> getCorridasNotificadas() {
        return corridasNotificadas;
    }

    public void setCorridasNotificadas(List<Corrida> corridasNotificadas) {
        this.corridasNotificadas = corridasNotificadas;
    }

    public List<Corrida> getCorridasAceitas() {
        return corridasAceitas;
    }

    public void setCorridasAceitas(List<Corrida> corridasAceitas) {
        this.corridasAceitas = corridasAceitas;
    }

    public void adicionarCorridaNotificada(Corrida corrida){
        this.corridasNotificadas.add(corrida);
    }

    public MotoristaStatus getStatus() {
        return status;
    }

    public void setStatus(MotoristaStatus status) {
        this.status = status;
    }
}
