package org.example.model.service;

import org.example.model.entity.Corrida;
import org.example.model.entity.Motorista;
import org.example.model.entity.Passageiro;
import org.example.model.repository.CorridaRepository;
import org.example.util.CrudUserError;
import org.example.util.StatusCorrida;

import java.time.LocalDateTime;
import java.util.List;

public class CorridaService {

    private final PassageiroService passageiroService = new PassageiroService();
    private final MotoristaService motoristaService = new MotoristaService();
    private final CorridaRepository corridaRepository = new CorridaRepository();

    public Corrida solicitarCorrida(String cpfPassageiro, String origem, String destino, String categoriaVeiculo) {
        Passageiro passageiro = passageiroService.getPassageiro(cpfPassageiro);
        if (passageiro == null) {
            throw new CrudUserError("Passageiro não encontrado.");
        }

        double precoEstimado = calcularPreco(origem, destino, categoriaVeiculo);

        Corrida novaCorrida = new Corrida(passageiro, origem, destino, categoriaVeiculo);
        novaCorrida.setValor(precoEstimado);

        Motorista motoristaEncontrado = encontrarMotoristaParaCorrida();

        if (motoristaEncontrado != null) {
            System.out.println("Encontrado motorista para a corrida! Aguardando aceite...");
        } else {
            throw new CrudUserError("Nenhum motorista disponível no momento.");
        }

        return novaCorrida;
    }

    public void aceitarCorrida(Corrida corrida, String cpfMotorista) {
        if (corrida.getStatus() != StatusCorrida.SOLICITADA) {
            throw new CrudUserError("A corrida não está disponível para ser aceita.");
        }

        Motorista motorista = motoristaService.getMotorista(cpfMotorista);
        if (motorista == null) {
            throw new CrudUserError("Motorista não encontrado.");
        }

        corrida.setMotorista(motorista);
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

    private double calcularPreco(String origem, String destino, String categoria) {
        //TODO = calculo de preço por corrida
        double preco = 0;
        return preco;
    }

    private Motorista encontrarMotoristaParaCorrida() {
        //TODO = metodo para encontrar o motorista mais próximo
        return null;
    }
}