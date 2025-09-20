package org.example.model.entity;

/**
 * Representa uma localização geográfica com coordenadas e uma descrição.
 */
public class Localizacao {
    private String descricao;
    private double latitude;
    private double longitude;

    /**
     * Construtor para a classe Localizacao.
     */
    public Localizacao(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Calcula a distância em quilômetros entre duas coordenadas geográficas.
     * Utiliza a fórmula de Haversine.
     * @param loc1 Ponto de localização 1.
     * @param loc2 Ponto de localização 2.
     * @return A distância em KM.
     */
    public static double calcularDistancia(Localizacao loc1, Localizacao loc2) {
        final double EARTH_RADIUS_KM = 6371;
        double latDistance = Math.toRadians(loc2.getLatitude() - loc1.getLatitude());
        double lonDistance = Math.toRadians(loc2.getLongitude() - loc1.getLongitude());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(loc1.getLatitude())) * Math.cos(Math.toRadians(loc2.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_KM * c;
    }

    @Override
    public String toString() {
        return "Lat: " + latitude + ", Lng: " + longitude;
    }

    // --- Getters e Setters ---

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
}