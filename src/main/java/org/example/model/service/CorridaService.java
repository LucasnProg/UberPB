package org.example.model.service;

import org.example.model.entity.*;
import org.example.model.repository.CorridaRepository;
import org.example.model.repository.MotoristaRepository;
import org.example.model.repository.PassageiroRepository;
import org.example.model.repository.VeiculoRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service central para a lógica de negócio de Corridas, com busca sequencial de motoristas.
 */
public class CorridaService {

    private final MotoristaRepository motoristaRepository = new MotoristaRepository();
    private final VeiculoRepository veiculoRepository = new VeiculoRepository();
    private final CorridaRepository corridaRepository = new CorridaRepository();
    private final PassageiroRepository passageiroRepository = new PassageiroRepository();

    /**
     * Etapa 1 do Fluxo: Cria a corrida, adiciona à lista de pendentes do passageiro
     * e inicia a busca pelo primeiro motorista mais próximo.
     */
    public Corrida solicitarCorrida(Passageiro passageiro, Localizacao origem, Localizacao destino, CategoriaVeiculo categoria, FormaPagamento formaPagamento) {
        double valorEstimado = calcularPrecoEstimado(origem, destino, categoria);
        Corrida novaCorrida = new Corrida(passageiro.getId(), origem, destino, categoria);
        novaCorrida.setValor(valorEstimado);
        novaCorrida.setFormaPagamento(formaPagamento);
        corridaRepository.salvar(novaCorrida);

        // Adiciona a corrida à lista de pendentes do passageiro para acompanhamento
        Passageiro passageiroAtualizado = passageiroRepository.buscarPorId(passageiro.getId());
        if (passageiroAtualizado != null) {
            passageiroAtualizado.getCorridasPendentes().add(novaCorrida);
            passageiroRepository.atualizar(passageiroAtualizado);
        }

        // Inicia a busca sequencial pelo motorista mais próximo
        encontrarProximoMotoristaDisponivel(novaCorrida);
        return novaCorrida;
    }

    /**
     * Lógica principal de matchmaking: encontra o motorista elegível mais próximo que
     * ainda não rejeitou a corrida e o notifica.
     */
    public void encontrarProximoMotoristaDisponivel(Corrida corrida) {
        Corrida corridaAtual = corridaRepository.buscarPorId(corrida.getId());
        if (corridaAtual == null || corridaAtual.getStatus() != StatusCorrida.SOLICITADA) return;

        List<Motorista> motoristasElegiveis = motoristaRepository.getMotoristas().stream()
                .filter(m -> m.getStatus() == MotoristaStatus.DISPONIVEL)
                .filter(m -> {
                    Veiculo v = veiculoRepository.buscarPorId(m.getIdVeiculo());
                    return v != null && v.getCategoria() == corridaAtual.getCategoriaVeiculo();
                })
                .filter(m -> !corridaAtual.getMotoristasQueRejeitaram().contains(m.getId()))
                .sorted(Comparator.comparingDouble(m ->
                        Localizacao.calcularDistancia(m.getLocalizacao(), corridaAtual.getOrigem())
                ))
                .collect(Collectors.toList());

        if (motoristasElegiveis.isEmpty()) {
            System.out.println("\n[INFO] Não há mais motoristas disponíveis para esta solicitação. Corrida cancelada.");
            corridaAtual.setStatus(StatusCorrida.CANCELADA);
            corridaRepository.atualizar(corridaAtual);
            return;
        }

        Motorista motoristaMaisProximo = motoristasElegiveis.get(0);

        System.out.println("\nNotificando o motorista mais próximo: " + motoristaMaisProximo.getNome());
        motoristaMaisProximo.adicionarCorridaNotificada(corridaAtual);
        motoristaRepository.atualizar(motoristaMaisProximo);
    }

    /**
     * Um motorista tenta aceitar uma corrida. Contém a lógica de concorrência.
     */
    public boolean aceitarCorrida(Motorista motorista, Corrida corrida) {
        Corrida corridaAtual = corridaRepository.buscarPorId(corrida.getId());
        if (corridaAtual.getStatus() != StatusCorrida.SOLICITADA) {
            System.out.println("\n[AVISO] Outro motorista já aceitou esta corrida.");
            limparNotificacaoUnica(motorista, corrida);
            return false;
        }

        corridaAtual.setMotoristaId(motorista.getId());
        corridaAtual.setStatus(StatusCorrida.ACEITA);
        corridaRepository.atualizar(corridaAtual);

        Motorista motoristaAtualizado = motoristaRepository.buscarPorId(motorista.getId());
        motoristaAtualizado.setStatus(MotoristaStatus.EM_CORRIDA);
        motoristaAtualizado.getCorridasAceitas().add(corridaAtual);
        motoristaAtualizado.getCorridasNotificadas().removeIf(c -> c.getId() == corrida.getId());
        motoristaRepository.atualizar(motoristaAtualizado);

        return true;
    }

    /**
     * O motorista inicia a viagem, alterando o status da corrida.
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
     * O motorista finaliza a viagem, atualizando o histórico do passageiro.
     */
    public void finalizarCorrida(Corrida corrida) {
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
            motoristaRepository.atualizar(motorista);
        }

        Passageiro passageiro = passageiroRepository.buscarPorId(corrida.getPassageiroId());
        if (passageiro != null) {
            passageiro.getCorridasPendentes().removeIf(c -> c.getId() == corrida.getId());
            passageiro.getHistoricoCorridas().add(corrida);
            passageiroRepository.atualizar(passageiro);
        }

        System.out.println("Corrida finalizada!");
    }

    private void limparNotificacaoUnica(Motorista motorista, Corrida corrida) {
        Motorista m = motoristaRepository.buscarPorId(motorista.getId());
        if (m != null) {
            m.getCorridasNotificadas().removeIf(c -> c.getId() == corrida.getId());
            motoristaRepository.atualizar(m);
        }
    }

    public double calcularPrecoEstimado(Localizacao origem, Localizacao destino, CategoriaVeiculo categoria) {
        double distanciaKm = Localizacao.calcularDistancia(origem, destino);
        double tempoEstimadoMinutos = (distanciaKm / 40.0) * 60;
        double tarifaBase = 4.50;
        double precoPorKm = 1.75;
        double precoPorMinuto = 0.30;
        double precoBase = tarifaBase + (distanciaKm * precoPorKm) + (tempoEstimadoMinutos * precoPorMinuto);
        double precoComCategoria = aplicarMultiplicadorCategoria(precoBase, categoria);
        double fatorDemanda = obterFatorDeDemanda();
        double precoFinal = precoComCategoria * fatorDemanda;
        return Math.round(precoFinal * 100.0) / 100.0;
    }

    private double obterFatorDeDemanda() {
        int horaAtual = LocalDateTime.now().getHour();
        if ((horaAtual >= 7 && horaAtual < 9) || (horaAtual >= 17 && horaAtual < 19)) {
            return 1.25; // Tarifa de pico
        }
        if (horaAtual >= 0 && horaAtual < 5) {
            return 1.35; // Tarifa noturna
        }
        return 1.0;
    }

    private double aplicarMultiplicadorCategoria(double precoBase, CategoriaVeiculo categoria) {
        switch (categoria) {
            case UBER_BLACK: return precoBase * 1.8;
            case UBER_XL: return precoBase * 1.5;
            case UBER_COMFORT: return precoBase * 1.3;
            case UBER_BAG: return precoBase * 1.2;
            default: return precoBase;
        }
    }

    public Corrida buscarCorridaAtivaPorMotorista(Motorista motorista) {
        return corridaRepository.getCorridas().stream()
                .filter(c -> c.getMotoristaId() == motorista.getId() &&
                        (c.getStatus() == StatusCorrida.ACEITA || c.getStatus() == StatusCorrida.EM_CURSO))
                .findFirst()
                .orElse(null);
    }

    public Corrida buscarCorridaPorId(int id) {
        return corridaRepository.buscarPorId(id);
    }

    public Motorista getMotoristaById(int id) {
        return motoristaRepository.buscarPorId(id);
    }

    public Passageiro getPassageiroById(int id) {
        return passageiroRepository.buscarPorId(id);
    }

    /**
     * Etapa PRELIMINAR: Verifica se existem motoristas elegíveis para uma corrida.
     * Este método é chamado ANTES da tela de pagamento.
     * @return true se houver pelo menos um motorista disponível, false caso contrário.
     */
    public boolean verificarMotoristasDisponiveis(Localizacao origem, CategoriaVeiculo categoria) {
        long countMotoristas = motoristaRepository.getMotoristas().stream()
                .filter(m -> m.getStatus() == MotoristaStatus.DISPONIVEL)
                .filter(m -> {
                    Veiculo v = veiculoRepository.buscarPorId(m.getIdVeiculo());
                    return v != null && v.getCategoria() == categoria;
                })
                .count();
        return countMotoristas > 0;
    }
}