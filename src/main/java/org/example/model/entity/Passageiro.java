package org.example.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um usuário do tipo Passageiro no sistema, que pode solicitar corridas.
 * Herda os atributos básicos da classe Usuario.
 */
public class Passageiro extends Usuario {

    private Localizacao localCasa;
    private List<Corrida> corridasPendentes;
    private List<Corrida> historicoCorridas;
    private double mediaAvaliacao;
    private List<Integer> avaliacoesRecebidas = new ArrayList<>();

    /**
     * Construtor para a classe Passageiro.
     */
    public Passageiro(String nome, String email, String senha, String cpf, String telefone) {
        super(nome, email, senha, cpf, telefone);
        this.corridasPendentes = new ArrayList<>();
        this.historicoCorridas = new ArrayList<>();
    }

    public Localizacao getLocalCasa() {
        return localCasa;
    }

    public void setLocalCasa(Localizacao localCasa) {
        this.localCasa = localCasa;
    }
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



    /**
     * Retorna a lista de corridas pendentes. Garante a inicialização da lista se for nula.
     * @return Uma lista de corridas pendentes, nunca nula.
     */
    public List<Corrida> getCorridasPendentes() {
        if (this.corridasPendentes == null) {
            this.corridasPendentes = new ArrayList<>();
        }
        return this.corridasPendentes;
    }

    /**
     * Retorna o histórico de corridas. Garante a inicialização da lista se for nula.
     * @return Uma lista com o histórico de corridas, nunca nula.
     */
    public List<Corrida> getHistoricoCorridas() {
        if (this.historicoCorridas == null) {
            this.historicoCorridas = new ArrayList<>();
        }
        return this.historicoCorridas;
    }
}