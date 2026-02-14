package org.example.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um parceiro de entrega (Entregador) no sistema.
 * Herda as funcionalidades básicas de Usuario.
 */
public class Entregador extends Usuario {

    private Localizacao localizacaoAtual;
    private EntregadorStatus status;
    private List<Pedido> entregasNotificadas = new ArrayList<>();
    private List<Pedido> entregasAceitas = new ArrayList<>();

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

    public EntregadorStatus getStatus() {
        return status;
    }

    public void setStatus(EntregadorStatus status) {
        this.status = status;
    }

    public List<Pedido> getEntregasNotificadas() {
        return entregasNotificadas;
    }

    public void setEntregasNotificadas(List<Pedido> entregasNotificadas) {
        this.entregasNotificadas = entregasNotificadas;
    }

    public List<Pedido> getEntregasAceitas() {
        return entregasAceitas;
    }

    public void setEntregasAceitas(List<Pedido> entregasAceitas) {
        this.entregasAceitas = entregasAceitas;
    }

    public void adicionarPedidoNotificada(Pedido pedido) {
        this.entregasNotificadas.add(pedido);
    }
}
