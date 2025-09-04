package org.example;

import org.example.model.entity.Passageiro;
import org.example.model.service.GerenteService;
import org.example.model.service.MotoristaService;
import org.example.model.service.PassageiroService;

public class Main {
    public static void main(String[] args) {
        PassageiroService passageiroManager = new PassageiroService();
        MotoristaService motoristaManager = new MotoristaService();
        GerenteService gerenteManager = new GerenteService();

        passageiroManager.criar("Lucas", "lucas1@gmail.com", "senha123", "11111111111", "83987380331");
        passageiroManager.criar("Manoel", "Manoel1@gmail.com", "senha123", "22222222222", "83987380332");
        passageiroManager.criar("Mateus", "Mateus1@gmail.com", "senha123", "33333333333", "83987380333");
        passageiroManager.criar("Eric", "Eric1@gmail.com", "senha123", "44444444444", "83987380334");


        motoristaManager.criar("Lucas", "lucas2@gmail.com", "senha123", "12222222222", "83987380335");
        motoristaManager.criar("Manoel", "Manoel2@gmail.com", "senha123", "13333333333", "83987380336");
        motoristaManager.criar("Rufino", "Mateus2@gmail.com", "senha123", "14444444444", "83987380337");
        motoristaManager.criar("Eric", "Eric2@gmail.com", "senha123", "15555555555", "83987380338");

        gerenteManager.criar("Lucas", "lucas3@gmail.com", "senha333", "12345678910", "83987380339");
        gerenteManager.criar("Manoel", "Manoel3@gmail.com", "senha333", "12345678911", "83987380320");
        gerenteManager.criar("Mateus", "Mateus3@gmail.com", "senha333", "12345678912", "83987380360");
        gerenteManager.criar("Eric", "Eric3@gmail.com", "senha333", "12345678913", "83987380370");


        passageiroManager.login("lucas1@gmail.com", "senha123");
        passageiroManager.login("Manoel1@gmail.com", "senha123");
        passageiroManager.login("Mateus1@gmail.com", "senha123");
        passageiroManager.login("Eric1@gmail.com", "senha123");


        motoristaManager.login("lucas2@gmail.com", "senha123");
        motoristaManager.login("Manoel2@gmail.com", "senha123");
        motoristaManager.login("Mateus2@gmail.com", "senha123");
        motoristaManager.login("Eric2@gmail.com", "senha123");

        gerenteManager.login("lucas1@gmail.com", "senha123");
        gerenteManager.login("Manoel1@gmail.com", "senha123");
        gerenteManager.login("Mateus1@gmail.com", "senha123");
        gerenteManager.login("Eric1@gmail.com","senha123");



    }

}