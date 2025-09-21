package org.example.model.entity;

/**
 * Classe abstrata que representa um usuário genérico no sistema.
 * Contém informações comuns a todos os tipos de usuários (Passageiro, Motorista, Gerente).
 */
public abstract class Usuario {

    private int id;
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private String telefone;

    /**
     * Construtor para a classe Usuario.
     * @param nome Nome completo do usuário.
     * @param email Endereço de e-mail do usuário.
     * @param senha Senha de acesso do usuário.
     * @param cpf CPF do usuário.
     * @param telefone Número de telefone do usuário.
     */
    public Usuario(String nome, String email, String senha, String cpf, String telefone) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
        this.senha = senha;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}