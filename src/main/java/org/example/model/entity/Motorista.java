package org.example.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um usuário do tipo Motorista no sistema, que pode aceitar e realizar corridas.
 * Contém informações específicas como localização, veículo e status.
 */
public class Motorista extends Usuario {

    private Localizacao localizacaoAtual;
    private int idVeiculo;
    private MotoristaStatus status;
    private List<Corrida> corridasNotificadas = new ArrayList<>();
    private List<Corrida> corridasAceitas = new ArrayList<>();

    /**
     * Construtor para a classe Motorista.
     * O status inicial é definido como INDISPONIVEL.
     */
    public Motorista(String nome, String email, String senha, String cpf, String telefone) {
        super(nome, email, senha, cpf, telefone);
        this.status = MotoristaStatus.INDISPONÍVEL;
    }

    /**
     * Adiciona uma corrida à lista de notificações do motorista.
     * @param corrida A corrida a ser adicionada.
     */
    public void adicionarCorridaNotificada(Corrida corrida) {
        this.corridasNotificadas.add(corrida);
    }

    // --- Getters e Setters ---

    public Localizacao getLocalizacao() {
        return localizacaoAtual;
    }

    public void setLocalizacao(Localizacao localizacao) {
        this.localizacaoAtual = localizacao;
    }

    public int getIdVeiculo() {
        return idVeiculo;
    }

    public void setIdVeiculo(int idVeiculo) {
        this.idVeiculo = idVeiculo;
    }


    public MotoristaStatus getStatus() {
        return status;
    }

    public void setStatus(MotoristaStatus status) {
        this.status = status;
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
}