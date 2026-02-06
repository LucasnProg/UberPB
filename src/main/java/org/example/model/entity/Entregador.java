package org.example.model.entity;

/**
 * Representa um parceiro de entrega (Entregador) no sistema.
 * Herda as funcionalidades básicas de Usuario.
 */
public class Entregador extends Usuario {

    private Localizacao localizacaoAtual;
    private String chavePix;
    private EntregadorStatus status;

    /**
     * Construtor para a classe Entregador.
     * 
     * @param nome     Nome completo do entregador.
     * @param email    Email do entregador.
     * @param senha    Senha de acesso.
     * @param cpf      CPF do entregador.
     * @param telefone Telefone de contato.
     */
    public Entregador(String nome, String email, String senha, String cpf, String telefone) {
        super(nome, email, senha, cpf, telefone);
        this.status = EntregadorStatus.DISPONIVEL;
    }

    // --- Getters e Setters ---
    public Localizacao getLocalizacaoAtual() {
        return localizacaoAtual;
    }

    public void setLocalizacaoAtual(Localizacao localizacaoAtual) {
        this.localizacaoAtual = localizacaoAtual;
    }

    public String getChavePix() {
        return chavePix;
    }

    public void setChavePix(String chavePix) {
        this.chavePix = chavePix;
    }

    public EntregadorStatus getStatus() {
        return status;
    }

    public void setStatus(EntregadorStatus status) {
        this.status = status;
    }

}
