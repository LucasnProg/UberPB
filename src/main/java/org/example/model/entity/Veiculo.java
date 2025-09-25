package org.example.model.entity;

/**
 * Representa um Veículo no sistema, associado a um Motorista.
 */
public class Veiculo {
    private int id;
    private String marca;
    private String modelo;
    private String placa;
    private String renavam;
    private int anoFabricacao;
    private String cor;
    private float capacidadeMala;
    private int numAssentos;
    private boolean premium;
    private CategoriaVeiculo categoria;

    /**
     * Construtor para a classe Veiculo.
     */
    public Veiculo(String marca, String modelo, String placa, String renavam, int anoFabricacao, String cor, float capacidadeMala, int numAssentos, boolean premium, CategoriaVeiculo categoria) {
        this.marca = marca;
        this.modelo = modelo;
        this.placa = placa;
        this.renavam = renavam;
        this.anoFabricacao = anoFabricacao;
        this.cor = cor;
        this.capacidadeMala = capacidadeMala;
        this.numAssentos = numAssentos;
        this.premium = premium;
        this.categoria = categoria;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getRenavam() { return renavam; }
    public void setRenavam(String renavam) { this.renavam = renavam; }
    public int getAnoFabricacao() { return anoFabricacao; }
    public void setAnoFabricacao(int anoFabricacao) { this.anoFabricacao = anoFabricacao; }
    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }
    public float getCapacidadeMala() {return capacidadeMala;}
    public void setCapacidadeMala(float capacidadeMala) {this.capacidadeMala = capacidadeMala;}
    public int getNumAssentos() {return numAssentos;}
    public void setNumAssentos(int numAssentos) {this.numAssentos = numAssentos;}
    public boolean isPremium() {return premium;}
    public void setPremium(boolean premium) {this.premium = premium;}
    public CategoriaVeiculo getCategoria() { return categoria; }
    public void setCategoria(CategoriaVeiculo categoria) { this.categoria = categoria; }
}