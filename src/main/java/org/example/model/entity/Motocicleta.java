package org.example.model.entity;

public class Motocicleta {
    private int id;
    private String fabricante;
    private String modelo;
    private String placa;

    /**
     * Construtor para a classe Veiculo.
     */
    public Motocicleta(String fabricante, String modelo, String placa) {
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.placa = placa;
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFabricante() { return fabricante; }
    public void setFabricante(String fabricante) { this.fabricante = fabricante; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }


}
