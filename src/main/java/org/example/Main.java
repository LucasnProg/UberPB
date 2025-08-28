package org.example;

import org.example.model.entity.Passageiro;
import org.example.model.repository.PassageirosRepository;

public class Main {
    public static void main(String[] args) {
        Passageiro p1 = new Passageiro("Lucas", "lucas@gmail.com", "12345678910", "83987380330");
        Passageiro p2 = new Passageiro("Manoel", "lucas@gmail.com", "12345678910", "83987380330");
        Passageiro p3 = new Passageiro("Mateus", "lucas@gmail.com", "12345678910", "83987380330");
        Passageiro p4 = new Passageiro("Eric", "lucas@gmail.com", "12345678910", "83987380330");

        PassageirosRepository passageirosRepo = new PassageirosRepository();

        passageirosRepo.salvarPassageiro(p1);
        passageirosRepo.salvarPassageiro(p2);
        passageirosRepo.salvarPassageiro(p3);
        passageirosRepo.salvarPassageiro(p4);
    }

}