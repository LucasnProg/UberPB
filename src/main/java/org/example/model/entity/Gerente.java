package org.example.model.entity;

/**
 * Representa um usuário do tipo Gerente no sistema.
 * Herda os atributos básicos da classe Usuario.
 */
public class Gerente extends Usuario {

    /**
     * Construtor para a classe Gerente.
     */
    public Gerente(String nome, String email, String senha, String cpf, String telefone) {
        super(nome, email, senha, cpf, telefone);
    }
}