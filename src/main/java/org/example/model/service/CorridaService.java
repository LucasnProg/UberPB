package org.example.model.service;

import org.example.controller.Sistema;
import org.example.model.entity.Corrida;
import org.example.model.entity.Motorista;
import org.example.model.repository.CorridaRepository;
import org.example.util.CrudUserError;
import org.example.util.StatusCorrida;

import java.time.LocalDateTime;
import java.util.List;

public class CorridaService {

    private final MotoristaService motoristaService = new MotoristaService();
    private final CorridaRepository corridaRepository = new CorridaRepository();

    public static boolean procurarCorrida(int idPassageiro, String origem, String destino, String categoriaVeiculoDesejada) {
        Corrida corridaSolicitada = new Corrida(idPassageiro,origem,destino,categoriaVeiculoDesejada);

        Sistema.procurarMotoristas(corridaSolicitada);


        double precoEstimado = calcularPreco(origem, destino, categoriaVeiculoDesejada);

        /*Motorista motoristaEncontrado = encontrarMotoristaParaCorrida();

        if (motoristaEncontrado != null) {
            System.out.println("Encontrado motorista para a corrida! Aguardando aceite...");
        } else {
            throw new CrudUserError("Nenhum motorista disponível no momento.");
        }*/

        return true;
    }

    public void aceitarCorrida(Corrida corrida, String cpfMotorista) {
        if (corrida.getStatus() != StatusCorrida.SOLICITADA) {
            throw new CrudUserError("A corrida não está disponível para ser aceita.");
        }

        Motorista motorista = motoristaService.getMotorista(cpfMotorista);
        if (motorista == null) {
            throw new CrudUserError("Motorista não encontrado.");
        }

        corrida.setMotoristaId(motorista.getId());
        corrida.setStatus(StatusCorrida.ACEITA);

        List<Corrida> corridas = corridaRepository.carregar();
        corridas.add(corrida);
        corridaRepository.salvar(corridas);

        System.out.println("Corrida aceita pelo motorista " + motorista.getNome() + "!");
        System.out.println("A corrida está a caminho do passageiro.");
    }

    public void finalizarCorrida(Corrida corrida) {
        if (corrida.getStatus() != StatusCorrida.ACEITA) {
            throw new CrudUserError("A corrida não está no estado 'aceita'. Não pode ser finalizada.");
        }

        corrida.setHoraFim(LocalDateTime.now());
        corrida.setStatus(StatusCorrida.CONCLUIDA);

        List<Corrida> corridas = corridaRepository.carregar();

        for (Corrida c : corridas) {
            if (c.getId() == corrida.getId()) {
                c.setHoraFim(corrida.getHoraFim());
                c.setStatus(StatusCorrida.CONCLUIDA);
                break;
            }
        }

        corridaRepository.salvar(corridas);

        System.out.println("Corrida finalizada");
    }

<<<<<<< Updated upstream
    private double calcularDistanciaEstimada(String origem, String destino) {
        if (origem.equalsIgnoreCase("Centro") && destino.equalsIgnoreCase("Aeroporto")) 
        {
            return 12.0;
        } 
        else if (origem.equalsIgnoreCase("Centro") && destino.equalsIgnoreCase("Shopping")) 
        {
            return 5.5;
        } 
        else if (origem.equalsIgnoreCase("Universidade") && destino.equalsIgnoreCase("Centro")) 
        {
            return 8.0;
        } 
        else 
        {
            //Se desconhecido, gera distância aleatória entre 2 e 15km
            int hash = Math.abs((origem + destino).hashCode());
            return 2 + (hash % 14); 
        }
=======
    private static double calcularPreco(String origem, String destino, String categoria) {
        //TODO = calculo de preço por corrida
        double preco = 0;
        return preco;
>>>>>>> Stashed changes
    }

    private double calcularTempoEstimado(double distanciaKm) {
        double velocidadeMediaKmH = 50.0; 
        return (distanciaKm / velocidadeMediaKmH) * 60;
    }

    private double calcularPreco(String origem, String destino, String categoria) {
        double distanciaKm = calcularDistanciaEstimada(origem, destino);
        double tempoMinutos = calcularTempoEstimado(distanciaKm);

        double tarifaBase = 5.0;
        double precoPorKm = 2.0;
        double precoPorMinuto = 0.5;

        double preco = tarifaBase + (distanciaKm * precoPorKm) + (tempoMinutos * precoPorMinuto);

        switch (categoria.toLowerCase()) {
            case "Luxo":
                preco *= 1.8;
                break;
            case "SUV":
                preco *= 1.5;
                break;
            case "Economico":
            default:
                preco *= 1.0;
                break;
        }

        return Math.round(preco * 100.0) / 100.0;
    }


    private Motorista encontrarMotoristaParaCorrida() {
        //TODO = metodo para encontrar o motorista mais próximo
        return null;
    }
}