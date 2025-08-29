package org.example;

import org.example.model.entity.Motorista;
import org.example.model.entity.Passageiro;
import org.example.model.repository.PassageiroRepository;
import org.example.model.service.MotoristaService;
import org.example.model.service.PassageiroService;

public class Main {
    public static void main(String[] args) {
        PassageiroService passageiroManager = new PassageiroService();
        MotoristaService motoristaManager = new MotoristaService();

        passageiroManager.criarPassageiro("Lucas", "lucas@gmail.com", "senha123", "12345678910", "83987380330");
        passageiroManager.criarPassageiro("Manoel", "lucas@gmail.com", "senha123", "12345678911", "83987380330");
        passageiroManager.criarPassageiro("Mateus", "lucas@gmail.com", "senha123", "12345678912", "83987380330");
        passageiroManager.criarPassageiro("Eric", "lucas@gmail.com", "senha123", "12345678913", "83987380330");


        motoristaManager.criarMotorista("Lucas", "lucas@gmail.com", "senha123", "12345678910", "83987380330");
        motoristaManager.criarMotorista("Rufino", "lucas@gmail.com", "senha123", "12345678911", "83987380330");
        motoristaManager.criarMotorista("Manoel", "lucas@gmail.com", "senha123", "12345678912", "83987380330");
        motoristaManager.criarMotorista("Eric", "lucas@gmail.com", "senha123", "12345678913", "83987380330");
    }

}