package org.example.model.service;

import org.example.model.entity.*;
import org.example.model.repository.CorridaRepository;
import org.example.model.repository.MotoristaRepository;
import org.example.model.repository.PassageiroRepository;
import org.example.model.repository.VeiculoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CorridaService {

    private final MotoristaRepository motoristaRepository = new MotoristaRepository();
    private final VeiculoRepository veiculoRepository = new VeiculoRepository();
    private final CorridaRepository corridaRepository = new CorridaRepository();
    private final PassageiroRepository passageiroRepository = new PassageiroRepository();

    /**
     * Etapa 1 do Fluxo: Cria a solicitação de corrida, salva no sistema
     * e notifica todos os motoristas elegíveis.
     */
    public Corrida solicitarCorrida(Passageiro passageiro, Localizacao origem, Localizacao destino, CategoriaVeiculo categoria) {
        double valorEstimado = calcularPrecoEstimado(origem, destino, categoria);
        Corrida novaCorrida = new Corrida(passageiro.getId(), origem, destino, categoria);
        novaCorrida.setValor(valorEstimado);
        corridaRepository.salvar(novaCorrida);

        notificarMotoristasDisponiveis(novaCorrida);
        return novaCorrida;
    }

    /**
     * Etapa 2 do Fluxo: Um motorista tenta aceitar uma corrida.
     * Contém a lógica de concorrência para garantir que apenas um motorista aceite.
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

        limparNotificacoesGeral(corrida);
        return true;
    }

    /**
     * Etapa 3 do Fluxo: O motorista inicia a viagem, alterando o status da corrida.
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
     * Etapa 4 do Fluxo: O motorista finaliza a viagem.
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

    private void notificarMotoristasDisponiveis(Corrida corrida) {
        List<Motorista> motoristasElegiveis = motoristaRepository.getMotoristas().stream()
                .filter(m -> m.getStatus() == MotoristaStatus.DISPONIVEL)
                .filter(m -> {
                    Veiculo v = veiculoRepository.buscarPorId(m.getIdVeiculo());
                    return v != null && v.getCategoria() == corrida.getCategoriaVeiculo();
                })
                .collect(Collectors.toList());

        if (motoristasElegiveis.isEmpty()) {
            System.out.println("\n[INFO] Nenhum motorista da categoria selecionada está disponível no momento.");
            return;
        }

        System.out.println("\nProcurando motorista...");
        for (Motorista motorista : motoristasElegiveis) {
            motorista.adicionarCorridaNotificada(corrida);
            motoristaRepository.atualizar(motorista);
        }
    }

    private void limparNotificacoesGeral(Corrida corridaAceita) {
        List<Motorista> todosMotoristas = motoristaRepository.getMotoristas();
        for (Motorista motorista : todosMotoristas) {
            boolean foiRemovido = motorista.getCorridasNotificadas().removeIf(c -> c.getId() == corridaAceita.getId());
            if (foiRemovido) {
                motoristaRepository.atualizar(motorista);
            }
        }
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
        double tempoEstimadoMinutos = (distanciaKm / 40.0) * 60; // Assumindo velocidade média de 40 km/h

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
            System.out.println("[INFO] Aplicando tarifa de horário de pico (25% de acréscimo).");
            return 1.25;
        }
        if (horaAtual >= 0 && horaAtual < 5) {
            System.out.println("[INFO] Aplicando tarifa de horário noturno (35% de acréscimo).");
            return 1.35;
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
}