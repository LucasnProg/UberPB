package org.example.model.entity;

/**
 * Representa um estabelecimento (Restaurante) no sistema Uber Eats.
 * Herda de Usuario para aproveitar atributos de autenticação e contato.
 * O campo 'cpf' da classe Usuario será utilizado para armazenar o CNPJ.
 */
public class Restaurante extends Usuario {

    private String categoria;
    private Localizacao endereco;

    /**
     * Construtor para a classe Restaurante.
     * 
     * @param nome     Nome do restaurante.
     * @param email    Email de contato/login.
     * @param senha    Senha de acesso.
     * @param cnpj     CNPJ do restaurante (passado para o campo CPF da classe pai).
     * @param telefone Telefone do estabelecimento.
     */
    public Restaurante(String nome, String email, String senha, String cnpj, String telefone) {
        super(nome, email, senha, cnpj, telefone); // CNPJ é armazenado no campo CPF
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

    public void setEndereco(Localizacao endereco) {
        this.endereco = endereco;
    }
}
