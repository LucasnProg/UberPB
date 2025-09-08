package org.example;

import org.example.model.entity.CategoriaVeiculo;
import org.example.model.entity.Motorista;
import org.example.model.entity.Passageiro;
import org.example.model.entity.Veiculo;
import org.example.model.service.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        PassageiroService passageiroManager = new PassageiroService();
        MotoristaService motoristaManager = new MotoristaService();
        VeiculoService veiculoManager = new VeiculoService();


        //Demonstração das US's solictadas para a Primeira Sprint
        System.out.println("Demonstração das Us's referentes a Cadastro e Autenticação");
        //Cadastro de passageiros e motoristas

        System.out.println("Criando usuários e veículos...");
        passageiroManager.criar("Lucas", "lucas1@gmail.com", "senha123", "11111111111", "83987380331");
        passageiroManager.criar("Manoel", "Manoel1@gmail.com", "senha123", "22222222222", "83987380332");
        passageiroManager.criar("Mateus", "Mateus1@gmail.com", "senha123", "33333333333", "83987380333");
        passageiroManager.criar("Eric", "Eric1@gmail.com", "senha123", "44444444444", "83987380334");


        motoristaManager.criar("Lucas", "lucas2@gmail.com", "senha123", "12222222222", "83987380335");
        motoristaManager.criar("Manoel", "Manoel2@gmail.com", "senha123", "13333333333", "83987380336");
        motoristaManager.criar("Rufino", "Mateus2@gmail.com", "senha123", "14444444444", "83987380337");
        motoristaManager.criar("Eric", "Eric2@gmail.com", "senha123", "15555555555", "83987380338");

        veiculoManager.criar("ABC-1234", "Onix", "Chevrolet", 2023, CategoriaVeiculo.UBER_X, "Prata");
        veiculoManager.criar("XYZ-3003", "Corolla", "Toyota", 2024, CategoriaVeiculo.UBER_XL, "Preto");
        veiculoManager.criar("JKL-6006", "Duster", "Renault", 2023, CategoriaVeiculo.UBER_BAG, "Marrom");
        veiculoManager.criar("KLZ-7384", "911", "Porsche", 2019, CategoriaVeiculo.UBER_BLACK, "Vermelho");

        List<Motorista> motoristasCadastrados =  motoristaManager.listar();
        List<Veiculo> veiculosCadastrados =  veiculoManager.listar();
        List<Passageiro> passageirosCadastrados =  passageiroManager.listar();

        for (int i = 0; i < motoristasCadastrados.size(); i++) {
            Motorista motorista = motoristasCadastrados.get(i);
            Veiculo veiculo = veiculosCadastrados.get(i);

            motorista.setIdVeiculo(veiculo.getId());
        }

        motoristaManager.atualizarDados(motoristasCadastrados);


        // Impressão visual dos passageiros
        System.out.println("=== Passageiros Cadastrados ===");
        for (Passageiro p : passageirosCadastrados) {
            System.out.println("+-----------------------------+");
            System.out.println("| Nome : " + p.getNome());
            System.out.println("| Email: " + p.getEmail());
            System.out.println("+-----------------------------+\n");
        }

        // Impressão visual dos motoristas
        System.out.println("=== Motoristas Cadastrados ===");
        for (Motorista m : motoristasCadastrados) {
            System.out.println("+-----------------------------+");
            System.out.println("| Nome       : " + m.getNome());
            System.out.println("| Email      : " + m.getEmail());
            System.out.println("| Veículo ID : " + m.getIdVeiculo());
            System.out.println("+-----------------------------+\n");
        }

        // Impressão visual dos veículos
        System.out.println("=== Veículos Cadastrados ===");
        for (Veiculo v : veiculosCadastrados) {
            System.out.println("+-----------------------------+");
            System.out.println("| ID       : " + v.getId());
            System.out.println("| Modelo   : " + v.getModelo());
            System.out.println("| Categoria: " + v.getCategoria());
            System.out.println("+-----------------------------+\n");
        }

        //Login de usuários com email
        System.out.println("");
        System.out.println("Logando Passageiros criados");
        System.out.println(passageiroManager.login("lucas1@gmail.com", "senha123")); //true = logado com sucesso
        System.out.println(passageiroManager.login("lucas1@gmail.com", "senha12")); //false = logado sem sucesso
        System.out.println("");
        System.out.println(passageiroManager.login("Manoel1@gmail.com", "senha123")); //true = logado com sucesso
        System.out.println(passageiroManager.login("Manoel1@gmail.com", "snha123")); //false = logado sem sucesso
        System.out.println("");
        System.out.println(passageiroManager.login("lucas1@gmail.com", "senha123")); //true = logado com sucesso
        System.out.println(passageiroManager.login("lucas1@gmail.com", "sena12")); //false = logado sem sucesso
        System.out.println("");
        System.out.println(passageiroManager.login("Mateus1@gmail.com", "senha123")); //true = logado com sucesso
        System.out.println(passageiroManager.login("Mateus1@gmail.com", "sena123")); //false = logado sem sucesso
        System.out.println("");
        System.out.println(passageiroManager.login("Eric1@gmail.com", "senha123")); //true = logado com sucesso
        System.out.println(passageiroManager.login("Eric1@gmail.com", "senha23")); //false = logado sem sucesso


        System.out.println("Logando Motoristas criados");
        System.out.println(motoristaManager.login("lucas2@gmail.com", "senha123")); //true = logado com sucesso
        System.out.println(motoristaManager.login("lucas2@gmail.com", "senha12")); //false = logado sem sucesso
        System.out.println("");
        System.out.println(motoristaManager.login("Manoel2@gmail.com", "senha123")); //true = logado com sucesso
        System.out.println(motoristaManager.login("Manoel2@gmail.com", "snha123")); //false = logado sem sucesso
        System.out.println("");
        System.out.println(motoristaManager.login("lucas2@gmail.com", "senha123")); //true = logado com sucesso
        System.out.println(motoristaManager.login("lucas2@gmail.com", "sena12")); //false = logado sem sucesso
        System.out.println("");
        System.out.println(motoristaManager.login("Mateus2@gmail.com", "senha123")); //true = logado com sucesso
        System.out.println(motoristaManager.login("Mateus2@gmail.com", "sena123")); //false = logado sem sucesso
        System.out.println("");
        System.out.println(motoristaManager.login("Eric2@gmail.com", "senha123")); //true = logado com sucesso
        System.out.println(motoristaManager.login("Eric2@gmail.com", "senha23")); //false = logado sem sucesso


        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.println("Demonstração das Us's referentes a Solicitção de Corrida");

        System.out.println("Exemplo de solicitação de corrida feita pelo passageiro Lucas:");
        Passageiro passLogado = passageiroManager.getPassageiro("11111111111");
        System.out.println("Logado como: "+ passLogado.getNome());
        System.out.println("Solicitando corrida UBER_X para Local");
        passLogado.solicitarCorrida("Casa","UEPB", CategoriaVeiculo.UBER_X);


        System.out.println("Os motoristas da categoria desejada foram notificados");
        System.out.println("----------------------------------------------------------------------------------------------------");

    }

}