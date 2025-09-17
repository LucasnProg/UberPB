package org.example.model.entity;

public class Localizacao {
    private String descricao;
    private double latitude;
    private double longitude;

    public Localizacao(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Lat: " + latitude + ", Lng: " + longitude;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public static double calcularDistancia(Localizacao locMotorista, Localizacao locPassageiro) {
        double EARTH_RADIUS_KM = 6371;
        double latDistance = Math.toRadians(locPassageiro.getLatitude() - locMotorista.getLatitude());
        double lonDistance = Math.toRadians(locPassageiro.getLongitude() - locMotorista.getLongitude());

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(locMotorista.getLatitude())) * Math.cos(Math.toRadians(locPassageiro.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }
}
