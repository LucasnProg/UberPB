package org.example.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa um estabelecimento (Restaurante) no sistema Uber Eats.
 * Herda de Usuario para aproveitar atributos de autenticação e contato.
 * O campo 'cpf' da classe Usuario será utilizado para armazenar o CNPJ.
 */
public class Restaurante extends Usuario {

    private String categoria;
    private Localizacao endereco;
    private ArrayList<MenuItem> menu;
    private List<Pedido> pedidosNotificados = new ArrayList<>();
    private List<Pedido> pedidosAceitos = new ArrayList<>();

    /**
     * Construtor para a classe Restaurante.
     * 
     * @param nome     Nome do restaurante.
     * @param email    Email de contato/login.
     * @param senha    Senha de acesso.
     * @param cnpj     CNPJ do restaurante (passado para o campo CPF da classe pai).
     * @param telefone Telefone do estabelecimento.
     */
    public Restaurante(String nome, String email, String senha, String cnpj, String telefone, String categoria, Localizacao localizacao) {
        super(nome, email, senha, cnpj, telefone); // CNPJ é armazenado no campo CPF
        this.menu = new ArrayList<>();
        this.endereco = localizacao;
        this.categoria = categoria;
    }

    // --- Getters e Setters ---

    /**
     * Retorna o CNPJ do restaurante.
     * Este método é um wrapper para getCpf() para clareza semântica.
     * 
     * @return O CNPJ.
     */
    public String getCnpj() {
        return super.getCpf();
    }

    public void setCnpj(String cnpj) {
        super.setCpf(cnpj);
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Localizacao getEndereco() {
        return endereco;
    }

    public ArrayList<MenuItem> getMenu(){return menu;}

    public void setMenu(ArrayList<MenuItem> menu) {
        this.menu = menu;
    }

    public void setEndereco(Localizacao endereco) {
        this.endereco = endereco;
    }

    public List<Pedido> getPedidosNotificados() {
        return pedidosNotificados;
    }

    public void setPedidosNotificados(List<Pedido> pedidosNotificados) {
        this.pedidosNotificados = pedidosNotificados;
    }

    public List<Pedido> getPedidosAceitos() {
        return pedidosAceitos;
    }

    public void setPedidosAceitos(List<Pedido> pedidosAceitos) {
        this.pedidosAceitos = pedidosAceitos;
    }

    public void adicionarPedidoNotificado(Pedido pedido) {
        this.pedidosNotificados.add(pedido);
    }
}
