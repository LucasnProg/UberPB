package org.example.model.service;

import org.example.model.entity.*;
import org.example.model.repository.CorridaRepository;
import org.example.model.repository.MotoristaRepository;
import org.example.model.repository.VeiculoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CorridaService {

    private final MotoristaRepository motoristaRepository = new MotoristaRepository();
    private final VeiculoRepository veiculoRepository = new VeiculoRepository();
    private final CorridaRepository corridaRepository = new CorridaRepository();

    /**
     * Calcula o preço estimado de uma corrida com base na distância real e na categoria.
     * Este método é chamado pela View para mostrar a estimativa ao usuário ANTES da confirmação.
     */
    public double calcularPrecoEstimado(Localizacao origem, Localizacao destino, CategoriaVeiculo categoria) {
        // LÓGICA INTEGRADA: Usa o cálculo de distância real (Haversine)
        double distanciaKm = Localizacao.calcularDistancia(origem, destino);

        // MANTIDO: Lógica de cálculo de preço detalhada e realista
        double tempoEstimadoMinutos = (distanciaKm / 40.0) * 60; // Assumindo velocidade média de 40 km/h
        double tarifaBase = 4.50;
        double precoPorKm = 1.75;
        double precoPorMinuto = 0.30;
        double preco = tarifaBase + (distanciaKm * precoPorKm) + (tempoEstimadoMinutos * precoPorMinuto);

        switch (categoria) {
            case UBER_BLACK: preco *= 1.8; break;
            case UBER_XL: preco *= 1.5; break;
            case UBER_COMFORT: preco *= 1.3; break;
            case UBER_BAG: preco *= 1.2; break;
            default: break;
        }

        return Math.round(preco * 100.0) / 100.0;
    }

    /**
     * Cria uma nova solicitação de corrida, salva no sistema e inicia a busca por um motorista.
     * Este é o ponto de entrada principal após o usuário confirmar a corrida.
     */
    public Corrida solicitarCorrida(Passageiro passageiro, Localizacao origem, Localizacao destino, CategoriaVeiculo categoria) {
        double valorEstimado = calcularPrecoEstimado(origem, destino, categoria);

        Corrida novaCorrida = new Corrida(passageiro.getId(), origem, destino, categoria);
        novaCorrida.setValor(valorEstimado); // Armazena o valor estimado na corrida
        corridaRepository.salvar(novaCorrida); // Salva a corrida com status SOLICITADA

        System.out.println("Procurando o motorista mais próximo para sua corrida...");
        encontrarEAtribuirMotorista(novaCorrida);

        return novaCorrida;
    }

    /**
     * Lógica central de matchmaking: encontra o motorista disponível mais próximo e o atribui à corrida.
     */
    private void encontrarEAtribuirMotorista(Corrida corrida) {
        List<Motorista> motoristasDisponiveis = motoristaRepository.getMotoristas().stream()
                .filter(m -> m.getStatus() == MotoristaStatus.DISPONIVEL)
                .filter(m -> {
                    Veiculo v = veiculoRepository.buscarPorId(m.getIdVeiculo());
                    return v != null && v.getCategoria() == corrida.getCategoriaVeiculo();
                })
                .collect(Collectors.toList());

        if (motoristasDisponiveis.isEmpty()) {
            System.out.println("Nenhum motorista da categoria selecionada está disponível no momento.");
            return;
        }

        Motorista motoristaMaisProximo = null;
        double menorDistancia = Double.MAX_VALUE;

        for (Motorista motorista : motoristasDisponiveis) {
            double distancia = Localizacao.calcularDistancia(motorista.getLocalizacao(), corrida.getOrigem());
            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                motoristaMaisProximo = motorista;
            }
        }

        if (motoristaMaisProximo != null) {
            aceitarCorrida(corrida, motoristaMaisProximo);
        }
    }

    /**
     * Atribui um motorista a uma corrida e atualiza o status de ambos.
     */
    public void aceitarCorrida(Corrida corrida, Motorista motorista) {
        if (corrida.getStatus() != StatusCorrida.SOLICITADA) {
            System.out.println("A corrida não está mais aguardando um motorista.");
            return;
        }

        // Atribui o motorista e atualiza o status
        corrida.setMotoristaId(motorista.getId());
        corrida.setStatus(StatusCorrida.ACEITA);
        corridaRepository.atualizar(corrida); // Usa o novo método eficiente

        // Atualiza o status do motorista
        motorista.setStatus(MotoristaStatus.EM_CORRIDA);
        motoristaRepository.atualizarMotorista(motorista);

        System.out.println("Corrida aceita pelo motorista " + motorista.getNome() + "!");
    }

    /**
     * Altera o status da corrida de ACEITA para EM_CURSO.
     */
    public void iniciarCorrida(Corrida corrida) {
        if (corrida.getStatus() != StatusCorrida.ACEITA) {
            System.out.println("A corrida não pode ser iniciada neste estado.");
            return;
        }
        corrida.setHoraInicio(LocalDateTime.now());
        corrida.setStatus(StatusCorrida.EM_CURSO);
        corridaRepository.atualizar(corrida);
        System.out.println("Corrida iniciada! Boa viagem.");
    }

    /**
     * Finaliza uma corrida, registrando a hora de término e liberando o motorista.
     */
    public void finalizarCorrida(Corrida corrida) {
        // ADAPTADO: Uma corrida deve estar EM_CURSO para ser finalizada.
        if (corrida.getStatus() != StatusCorrida.EM_CURSO) {
            System.out.println("A corrida não pode ser finalizada neste estado.");
            return;
        }

        corrida.setHoraFim(LocalDateTime.now());
        corrida.setStatus(StatusCorrida.FINALIZADA);
        corridaRepository.atualizar(corrida);

        Motorista motorista = motoristaRepository.buscarPorId(corrida.getMotoristaId());
        if (motorista != null) {
            motorista.setStatus(MotoristaStatus.DISPONIVEL);
            motoristaRepository.atualizarMotorista(motorista);
        }
        System.out.println("Corrida finalizada!");
    }

    // Métodos de busca para a View
    public Corrida buscarCorridaPorId(int id) {
        return corridaRepository.buscarPorId(id);
    }

    public CategoriaVeiculo categoriaPorOpcao(int opcao) {
        switch (opcao) {
            case 1:
                return CategoriaVeiculo.UBER_X;
            case 2:
                return CategoriaVeiculo.UBER_COMFORT;
            case 3:
                return CategoriaVeiculo.UBER_BLACK;
            case 4:
                return CategoriaVeiculo.UBER_BAG;
            case 5:
                return CategoriaVeiculo.UBER_XL;
            default:
                // Retorna a categoria padrão (a mais comum) caso a opção seja inválida.
                // Isso adiciona uma camada de segurança para evitar erros.
                return CategoriaVeiculo.UBER_X;
        }
    }
}