package org.example;

import org.example.model.entity.Passageiro;
import org.example.model.repository.PassageiroRepository;

public class Main {
    public static void main(String[] args) {
        Passageiro p1 = new Passageiro("Lucas", "lucas@gmail.com", "senha123", "12345678910", "83987380330");
        Passageiro p2 = new Passageiro("Manoel", "lucas@gmail.com", "senha123", "12345678910", "83987380330");
        Passageiro p3 = new Passageiro("Mateus", "lucas@gmail.com", "senha123", "12345678910", "83987380330");
        Passageiro p4 = new Passageiro("Eric", "lucas@gmail.com", "senha123", "12345678910", "83987380330");

        PassageiroRepository passageirosRepo = new PassageiroRepository();

        passageirosRepo.salvarPassageiro(p1);
        passageirosRepo.salvarPassageiro(p2);
        passageirosRepo.salvarPassageiro(p3);
        passageirosRepo.salvarPassageiro(p4);
    }

}