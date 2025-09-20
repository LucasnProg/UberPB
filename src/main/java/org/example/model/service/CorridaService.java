package org.example.model.service;

import org.example.model.entity.*;
import org.example.model.repository.CorridaRepository;
import org.example.model.repository.MotoristaRepository;
import org.example.model.repository.PassageiroRepository;
import org.example.model.repository.VeiculoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service central para toda a lógica de negócio envolvendo Corridas.
 * Gerencia o ciclo de vida de uma corrida, desde a solicitação e notificação
 * até a finalização.
 */
public class CorridaService {

    private final MotoristaRepository motoristaRepository = new MotoristaRepository();
    private final VeiculoRepository veiculoRepository = new VeiculoRepository();
    private final CorridaRepository corridaRepository = new CorridaRepository();
    private final PassageiroRepository passageiroRepository = new PassageiroRepository();

    /**
     * Etapa 1 do Fluxo: Cria a solicitação de corrida, salva no sistema
     * e notifica todos os motoristas elegíveis.
     * @param passageiro O passageiro que está solicitando a corrida.
     * @param origem O local de partida.
     * @param destino O local de chegada.
     * @param categoria A categoria de veículo desejada.
     * @return O objeto Corrida recém-criado com o status SOLICITADA.
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
     * @param motorista O motorista que está tentando aceitar.
     * @param corrida A corrida notificada.
     * @return true se o motorista conseguiu aceitar a corrida, false caso contrário.
     */
    public boolean aceitarCorrida(Motorista motorista, Corrida corrida) {
        // CRÍTICO: Recarrega a corrida do repositório para verificar seu status atual
        Corrida corridaAtual = corridaRepository.buscarPorId(corrida.getId());

        if (corridaAtual.getStatus() != StatusCorrida.SOLICITADA) {
            System.out.println("\n[AVISO] Outro motorista já aceitou esta corrida.");
            limparNotificacaoUnica(motorista, corrida);
            return false;
        }

        // Se a corrida ainda está disponível, o motorista a "ganha"
        corridaAtual.setMotoristaId(motorista.getId());
        corridaAtual.setStatus(StatusCorrida.ACEITA);
        corridaRepository.atualizar(corridaAtual);

        Motorista motoristaAtualizado = motoristaRepository.buscarPorId(motorista.getId());
        motoristaAtualizado.setStatus(MotoristaStatus.EM_CORRIDA);
        motoristaAtualizado.getCorridasAceitas().add(corridaAtual);
        motoristaAtualizado.getCorridasNotificadas().removeIf(c -> c.getId() == corrida.getId());
        motoristaRepository.atualizar(motoristaAtualizado);

        // Limpa a notificação de todos os outros motoristas
        limparNotificacoesGeral(corrida);
        return true;
    }

    /**
     * Etapa 3 do Fluxo: O motorista inicia a viagem, alterando o status da corrida.
     * @param corrida A corrida a ser iniciada.
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
     * Etapa 4 do Fluxo: O motorista finaliza a viagem, registrando a hora de término
     * e voltando ao status DISPONIVEL.
     * @param corrida A corrida a ser finalizada.
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
        System.out.println("Corrida finalizada!");
    }

    // --- Métodos de Lógica Interna e Auxiliares ---

    /**
     * Lógica central de notificação: encontra todos os motoristas elegíveis e os notifica.
     */
    private void notificarMotoristasDisponiveis(Corrida corrida) {
        List<Motorista> motoristasDisponiveis = motoristaRepository.getMotoristas().stream()
                .filter(m -> m.getStatus() == MotoristaStatus.DISPONIVEL)
                .filter(m -> {
                    Veiculo v = veiculoRepository.buscarPorId(m.getIdVeiculo());
                    return v != null && v.getCategoria() == corrida.getCategoriaVeiculo();
                })
                .collect(Collectors.toList());

        if (motoristasDisponiveis.isEmpty()) {
            System.out.println("\n[INFO] Nenhum motorista da categoria selecionada está disponível.");
            return;
        }

        System.out.println("\nNotificando " + motoristasDisponiveis.size() + " motorista(s)...");
        for (Motorista motorista : motoristasDisponiveis) {
            motorista.adicionarCorridaNotificada(corrida);
            motoristaRepository.atualizar(motorista);
        }
    }

    /**
     * Após um motorista aceitar, remove a notificação da corrida para todos os outros.
     */
    private void limparNotificacoesGeral(Corrida corridaAceita) {
        List<Motorista> todosMotoristas = motoristaRepository.getMotoristas();
        for (Motorista motorista : todosMotoristas) {
            boolean foiRemovido = motorista.getCorridasNotificadas().removeIf(c -> c.getId() == corridaAceita.getId());
            if (foiRemovido) {
                motoristaRepository.atualizar(motorista);
            }
        }
    }

    /**
     * Remove a notificação de um motorista específico (caso a corrida tenha sido aceita por outro).
     */
    private void limparNotificacaoUnica(Motorista motorista, Corrida corrida) {
        Motorista m = motoristaRepository.buscarPorId(motorista.getId());
        if (m != null) {
            m.getCorridasNotificadas().removeIf(c -> c.getId() == corrida.getId());
            motoristaRepository.atualizar(m);
        }
    }

    /**
     * Calcula o preço estimado de uma corrida com base na distância real e na categoria.
     */
    public double calcularPrecoEstimado(Localizacao origem, Localizacao destino, CategoriaVeiculo categoria) {
        double distanciaKm = Localizacao.calcularDistancia(origem, destino);
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

    // --- Métodos de Busca para a Camada de Visão (View) ---

    /**
     * Busca uma corrida ativa (ACEITA ou EM_CURSO) para um motorista específico.
     * @return A corrida ativa encontrada, ou null.
     */
    public Corrida buscarCorridaAtivaPorMotorista(Motorista motorista) {
        return corridaRepository.getCorridas().stream()
                .filter(c -> c.getMotoristaId() == motorista.getId() &&
                        (c.getStatus() == StatusCorrida.ACEITA || c.getStatus() == StatusCorrida.EM_CURSO))
                .findFirst()
                .orElse(null);
    }

    /**
     * Busca uma corrida pelo seu ID.
     */
    public Corrida buscarCorridaPorId(int id) {
        return corridaRepository.buscarPorId(id);
    }

    /**
     * Busca um motorista pelo seu ID.
     */
    public Motorista getMotoristaById(int id) {
        return motoristaRepository.buscarPorId(id);
    }

    /**
     * Busca um passageiro pelo seu ID.
     */
    public Passageiro getPassageiroById(int id) {
        return passageiroRepository.buscarPorId(id);
    }
}