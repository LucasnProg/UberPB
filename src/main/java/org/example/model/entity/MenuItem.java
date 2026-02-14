package org.example.model.entity;

import java.util.ArrayList;

public class MenuItem {
    private int id;
    private String nome;
    private String ingredientes;
    private double preco;
    private int tempoPreparo;

    public MenuItem(String nome, String ingredientes, double preco, int tempoPreparo) {
        this.nome = nome;
        this.ingredientes = ingredientes;
        this.preco = preco;
        this.tempoPreparo = tempoPreparo;
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

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getTempoPreparo() {
        return tempoPreparo;
    }

    public void setTempoPreparo(int tempoPreparo) {
        this.tempoPreparo = tempoPreparo;
    }
}
