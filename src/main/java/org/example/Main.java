package org.example;

import org.example.model.entity.*;
import org.example.model.repository.GerenteRepository;
import org.example.model.repository.MotoristaRepository;
import org.example.model.repository.PassageiroRepository;
import org.example.model.service.*;
import java.util.Scanner;
import org.example.model.entity.CategoriaVeiculo;
import org.example.view.CLIView;

public class Main {
    public static void main(String[] args) {
        PassageiroService passageiroManager = new PassageiroService();
        PassageiroRepository passageiro = new PassageiroRepository();
        MotoristaService motoristaManager = new MotoristaService();
        MotoristaRepository motorista = new MotoristaRepository();
        GerenteService gerenteManager = new GerenteService();
        GerenteRepository gerente = new GerenteRepository();
        VeiculoService veiculoManager = new VeiculoService();

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
        gerenteManager.login("Eric1@gmail.com", "senha123");

        //veiculoManager.criar("ABC-1234", "Onix", "Chevrolet", 2023, CategoriaVeiculo.UBER_X, "Prata");
        //veiculoManager.criar("XYZ-3003", "Corolla", "Toyota", 2024, CategoriaVeiculo.UBER_XL, "Preto");
        //veiculoManager.criar("JKL-6006", "Duster", "Renault", 2023, CategoriaVeiculo.UBER_BAG, "Marrom");
        //veiculoManager.criar("KLZ-7384", "911", "Porsche", 2019, CategoriaVeiculo.UBER_BLACK, "Vermelho");

        CLIView cli = new CLIView();
        cli.iniciar();
    }
}
