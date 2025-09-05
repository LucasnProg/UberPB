package org.example.model.entity;

public enum CategoriaVeiculo {
    UBER_X("UberX", "Corrida mais econômica"),
    UBER_COMFORT("Uber Comfort", "Carros mais novos e espaçosos"),
    UBER_BLACK("Uber Black", "Veículos premium"),
    UBER_BAG("Uber Bag", "Veículos com porta-malas maior"),
    UBER_XL("Uber XL", "Capacidade para mais passageiros");

    private final String nome;
    private final String descricao;

    CategoriaVeiculo(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }
}
