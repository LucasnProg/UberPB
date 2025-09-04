package org.example.model.entity;

import java.util.ArrayList;
import java.util.List;

public class Motorista extends Usuario{

    private String localizacao;
    private String catVeiculo;
    private List<Corrida> corridasNotificadas = new ArrayList<Corrida>();
    private List<Corrida> corridasAceitas = new ArrayList<Corrida>();;


    public Motorista(String nome, String email, String senha, String cpf, String telefone) {
        super(nome, email, senha, cpf, telefone);
    }

    public String getCatVeiculo() {
        return catVeiculo;
    }

    public void setCatVeiculo(String catVeiculo) {
        this.catVeiculo = catVeiculo;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
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
}
