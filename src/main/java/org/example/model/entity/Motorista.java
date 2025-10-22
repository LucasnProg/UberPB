package org.example.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um usuário do tipo Motorista no sistema.
 */
public class Motorista extends Usuario {

    private Localizacao localizacaoAtual;
    private int idVeiculo;
    private MotoristaStatus status;
    private List<Corrida> corridasNotificadas = new ArrayList<>();
    private List<Corrida> corridasAceitas = new ArrayList<>();
    private double mediaAvaliacao;
    private List<Integer> avaliacoesRecebidas = new ArrayList<>();
    private String chavePix;

    public Motorista(String nome, String email, String senha, String cpf, String telefone) {
        super(nome, email, senha, cpf, telefone);
        this.status = MotoristaStatus.DISPONIVEL;
    }

    public void adicionarCorridaNotificada(Corrida corrida) {
        this.corridasNotificadas.add(corrida);
    }

    // --- Getters e Setters ---
    public String getChavePix() {
        return chavePix;
    }

    public void setChavePix(String chavePix) {
        this.chavePix = chavePix;
    }
    public Localizacao getLocalizacao() { return localizacaoAtual; }
    public void setLocalizacao(Localizacao localizacao) { this.localizacaoAtual = localizacao; }
    public int getIdVeiculo() { return idVeiculo; }
    public void setIdVeiculo(int idVeiculo) { this.idVeiculo = idVeiculo; }
    public MotoristaStatus getStatus() { return status; }
    public void setStatus(MotoristaStatus status) { this.status = status; }
    public List<Corrida> getCorridasNotificadas() { return corridasNotificadas; }
    public void setCorridasNotificadas(List<Corrida> corridasNotificadas) { this.corridasNotificadas = corridasNotificadas; }
    public List<Corrida> getCorridasAceitas() { return corridasAceitas; }
    public void setCorridasAceitas(List<Corrida> corridasAceitas) { this.corridasAceitas = corridasAceitas; }
    public double getMediaAvaliacao() {
        return mediaAvaliacao;
    }
    public void setMediaAvaliacao(double mediaAvaliacao) {
        this.mediaAvaliacao = mediaAvaliacao;
    }
    public List<Integer> getAvaliacoesRecebidas() {
        if (this.avaliacoesRecebidas == null) {
            this.avaliacoesRecebidas = new ArrayList<>();
        }
        return this.avaliacoesRecebidas;
    }

    public void adicionarAvaliacao(int nota) {this.getAvaliacoesRecebidas().add(nota);}
}