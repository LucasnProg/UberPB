package org.example.controller;

import org.example.model.entity.Corrida;
import org.example.model.entity.Motorista;
import org.example.model.entity.Passageiro;

import java.util.ArrayList;
import java.util.List;

public class Sistema {

    // Inicializa as listas para evitar NullPointerException
    public static List<Motorista> motoristasOnline = new ArrayList<>();
    public static List<Passageiro> passageirosOnline = new ArrayList<>();
    public static List<Corrida> corridas = new ArrayList<>();

    public static List<Passageiro> getPassageirosOnline() {
        return passageirosOnline;
    }

    public static void setPassageirosOnline(List<Passageiro> passageirosOnline) {
        Sistema.passageirosOnline = passageirosOnline;
    }

    public static List<Motorista> getMotoristasOnline() {
        return motoristasOnline;
    }

    public static void setMotoristasOnline(List<Motorista> motoristasOnline) {
        Sistema.motoristasOnline = motoristasOnline;
    }

    public static List<Corrida> getCorridas() {
        return corridas;
    }

    public static void setCorridas(List<Corrida> corridas) {
        Sistema.corridas = corridas;
    }

    public static void adicionarPassageiroOnline(Passageiro passageiro){
        if (passageirosOnline == null) passageirosOnline = new ArrayList<>();
        Sistema.passageirosOnline.add(passageiro);
    }

    public static void adicionarMotoristaOnline(Motorista motorista){
        if (motoristasOnline == null) motoristasOnline = new ArrayList<>();
        Sistema.motoristasOnline.add(motorista);
    }

    public static void adicionarCorridas(Corrida corridaSolicitada){
        if (corridas == null) corridas = new ArrayList<>();
        Sistema.corridas.add(corridaSolicitada);
    }

    public static void procurarMotoristas(Corrida corrida){
        adicionarCorridas(corrida);

        for(Motorista moto : motoristasOnline){
            if (moto.getLocalizacao() != null &&
                moto.getLocalizacao().equals(corrida.getOrigem()) &&
                moto.getCatVeiculo() != null &&
                moto.getCatVeiculo().equals(corrida.getCategoriaVeiculo())){
                notificarMotorista(corrida, moto);
            }
        }
    }

    public static void notificarMotorista(Corrida corrida, Motorista motorista){
        motorista.adicionarCorridaNotificada(corrida);
    }
}
