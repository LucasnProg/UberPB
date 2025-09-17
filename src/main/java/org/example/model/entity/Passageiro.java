package org.example.model.entity;

/**
 * Representa um usuário do tipo Passageiro no sistema.
 * Herda os atributos básicos da classe Usuario.
 */
public class Passageiro extends Usuario {
    private Localizacao localCasa;

    public Passageiro(String nome, String email, String senha, String cpf, String telefone) {
        super(nome, email, senha, cpf, telefone);
    }

    public Localizacao getLocalCasa() {
        return localCasa;
    }

    public void setLocalCasa(Localizacao localCasa) {
        this.localCasa = localCasa;
    }
}