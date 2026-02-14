package org.example.model.service;

import org.example.model.entity.*;
import org.example.model.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Service central para a lógica de negócio de Pedidos, com busca sequencial de entregadores.
 */
public class PedidoService {

    private final EntregadorRepository entregadorRepository = new EntregadorRepository();
    private final PedidoRepository pedidoRepository = new PedidoRepository();
    private final PassageiroRepository clienteRepository = new PassageiroRepository();
    private final RestauranteRepository restauranteRepository = new RestauranteRepository();

    /**
     * Etapa 1 do Fluxo: Cria o pedido, adiciona à lista de pendentes do cliente
     * e inicia a busca pelo primeiro Entregador mais próximo.
     */
    public Pedido realizarPedido(int passageiroId, int restauranteId, Localizacao origem, Localizacao destino, FormaPagamento formaPagamento, ArrayList<MenuItem> itensPedidos, LocalDateTime agendamento) {
        double valorEstimado = calcularPrecoEstimado(origem, destino, itensPedidos);
        Pedido novoPedido;
        if (agendamento != null){
            novoPedido = new Pedido(passageiroId,restauranteId, itensPedidos, agendamento);
        } else {
            novoPedido = new Pedido(passageiroId,restauranteId, itensPedidos);
        }
        novoPedido.setValor(valorEstimado);
        novoPedido.setFormaPagamento(formaPagamento);
        novoPedido.setStatusPedido(StatusCorrida.SOLICITADA);
        pedidoRepository.salvar(novoPedido);

        Passageiro clienteAtualizado = clienteRepository.buscarPorId(passageiroId);
        if (clienteAtualizado != null) {
            clienteAtualizado.getPedidosPendentes().add(novoPedido);
            clienteRepository.atualizar(clienteAtualizado);
        }

        encontrarProximoEntregadorDisponivel(novoPedido);
        return novoPedido;
    }

    /**
     * Atualiza a Forma de Pagamento do Pedido e a persiste no repositório.
     * Este método é chamado pela PagamentoView após a escolha do usuário.
     * @param pedido O objeto Corrida a ser atualizado.
     * @param formaPagamento A FormaPagamento escolhida pelo usuário.
     */
    public void atualizarFormaPagamento(Pedido pedido, FormaPagamento formaPagamento) {
        pedido.setFormaPagamento(formaPagamento);
        pedidoRepository.atualizar(pedido);}


    /**
     * Cancela um pedido, atualizando o status e removendo-a da lista de pendentes do passageiro.
     * @param pedido O pedido a ser cancelada.
     * @param clienteId O ID do passageiro para encontrar e atualizar o objeto correto.
     */
    public void cancelarPedido(Pedido pedido, int clienteId) {
        pedido.setStatusPedido(StatusCorrida.CANCELADA);
        pedidoRepository.atualizar(pedido);

        Passageiro cliente = clienteRepository.buscarPorId(clienteId);
        if (cliente != null) {
            cliente.getPedidosPendentes().removeIf(p -> p.getIdPedido() == pedido.getIdPedido());
            clienteRepository.atualizar(cliente);
        }
    }


    /**
     * Lógica principal de matchmaking: encontra o entregador elegível mais próximo.
     */
    public void encontrarProximoEntregadorDisponivel(Pedido pedido) {
        if (pedido == null || pedido.getStatusPedido() != StatusCorrida.SOLICITADA) return;

        List<Entregador> entregadoresElegiveis = entregadorRepository.getEntregadores().stream()
                .filter(e -> e.getStatus() == EntregadorStatus.DISPONIVEL)
                .filter(e -> !pedido.getEntregadoresQueRejeitaram().contains(e.getId()))
                .sorted(Comparator.comparingDouble(e ->
                        Localizacao.calcularDistancia(e.getLocalizacaoAtual(), pedido.getOrigem())
                ))
                .toList();

        if (entregadoresElegiveis.isEmpty()) {
            System.out.println("\n[INFO] Não há mais entregadores disponíveis para esta solicitação. Corrida cancelada.");
            pedido.setStatusPedido(StatusCorrida.CANCELADA);
            pedidoRepository.atualizar(pedido);
            return;
        }

        Entregador entregadorMaisProximo = entregadoresElegiveis.getFirst();

        System.out.println("\nNotificando o Entregador mais próximo: " + entregadorMaisProximo.getNome());
        entregadorMaisProximo.adicionarPedidoNotificada(pedido);
        Restaurante restaurante = restauranteRepository.buscarPorId(pedido.getIdRestaurante());
        restaurante.adicionarPedidoNotificado(pedido);
        restauranteRepository.atualizar(restaurante);
        entregadorRepository.atualizar(entregadorMaisProximo);
    }

    /**
     * Um entregador aceita uma corrida. A corrida é imediatamente iniciada.
     */
    public boolean aceitarPedido(Entregador entregador, Pedido pedido) {
        Pedido pedidoAtual = pedidoRepository.buscarPorId(pedido.getIdPedido());
        if (pedidoAtual.getStatusPedido() != StatusCorrida.SOLICITADA) {
            System.out.println("\n[AVISO] Outro motorista já aceitou esta corrida.");
            limparNotificacaoUnica(entregador, pedido);
            return false;
        }

        // Atualiza a corrida: define entregador, status e hora de início
        pedidoAtual.setIdEntregador(entregador.getId());
        pedidoAtual.setStatusPedido(StatusCorrida.EM_CURSO); // Status muda direto para EM_CURSO
        if (pedidoAtual.getHoraInicio() !=null){
            pedidoAtual.setHoraInicio(LocalDateTime.now()); // Hora de início é registrada no aceite, caso não seja definido
        }
        pedidoRepository.atualizar(pedidoAtual);

        // Atualiza o entregador
        Entregador entregadorAtualizado = entregadorRepository.buscarPorId(entregador.getId());
        entregadorAtualizado.setStatus(EntregadorStatus.OCUPADO);
        entregadorAtualizado.getEntregasAceitas().add(pedidoAtual);
        entregadorAtualizado.getEntregasNotificadas().removeIf(p -> p.getIdPedido() == pedido.getIdPedido());
        entregadorRepository.atualizar(entregadorAtualizado);

        System.out.println("A Entrega será realizada em breve! Aguarde.");
        return true;
    }

    /**
     * O entregador finaliza a entrega, atualizando o histórico do passageiro.
     */
    public void finalizarCorrida(Pedido pedido) {
        if (pedido.getStatusPedido() != StatusCorrida.EM_CURSO) {
            System.out.println("A corrida ainda não foi iniciada.");
            return;
        }

        pedido.setHoraFim(LocalDateTime.now());
        pedido.setStatusPedido(StatusCorrida.FINALIZADA);
        pedidoRepository.atualizar(pedido);

        Entregador entregador = entregadorRepository.buscarPorId(pedido.getIdEntregador());
        if (entregador != null) {
            entregador.setStatus(EntregadorStatus.DISPONIVEL);
            entregadorRepository.atualizar(entregador);
        }

        Passageiro cliente = clienteRepository.buscarPorId(pedido.getIdCliente());
        if (cliente != null) {
            cliente.getPedidosPendentes().removeIf(p -> p.getIdPedido() == pedido.getIdPedido());
            cliente.getHistoricoPedidos().add(pedido);
            clienteRepository.atualizar(cliente);
        }

        System.out.println("Entrega realizada!");
    }

    private void limparNotificacaoUnica(Entregador entregadorPedido, Pedido pedido) {
        Entregador entregador = entregadorRepository.buscarPorId(entregadorPedido.getId());
        if (entregador != null) {
            entregador.getEntregasNotificadas().removeIf(p -> p.getIdPedido() == pedido.getIdPedido());
            entregadorRepository.atualizar(entregador);
        }
    }

    public double calcularPrecoEstimado(Localizacao origem, Localizacao destino, ArrayList<MenuItem> itensPedido) {
        double valorDosItens = 0;
        for (MenuItem itens : itensPedido){
            valorDosItens += itens.getPreco();
        }

        double distanciaKm = Localizacao.calcularDistancia(origem, destino);
        double tempoEstimadoMinutos = (distanciaKm / 40.0) * 60;
        double tarifaBase = 1.00;
        double precoPorKm = 0.80;
        double precoPorMinuto = 0.15;
        double precoEntrega = tarifaBase + (distanciaKm * precoPorKm) + (tempoEstimadoMinutos * precoPorMinuto);
        double fatorDemanda = obterFatorDeDemanda();
        double precoFinal = (precoEntrega * fatorDemanda) + valorDosItens;
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

    public int calcularTempoDeEntrega(Localizacao origem, Localizacao destino, ArrayList<MenuItem> itensPedido){
        int maiorTempo = 0;
        for (MenuItem item : itensPedido){
            if (item.getTempoPreparo()>maiorTempo){
                maiorTempo = item.getTempoPreparo();
            }
        }
        double distanciaKm = Localizacao.calcularDistancia(origem, destino);
        return (int) ((distanciaKm / 40.0) * 60) + maiorTempo;
    }

    public Pedido buscarPedidoAtivoPorEntregador(Entregador entregador) {
        return pedidoRepository.getPedidos().stream()
                .filter(p -> p.getIdEntregador() == entregador.getId() &&
                        (p.getStatusPedido() == StatusCorrida.EM_CURSO)) // Apenas EM_CURSO é considerada ativa
                .findFirst()
                .orElse(null);
    }

    public Pedido buscarPedidoPorId(int id) {
        return pedidoRepository.buscarPorId(id);
    }

    public Entregador getEntregadorById(int id) {
        return entregadorRepository.buscarPorId(id);
    }

    public Passageiro getClienteById(int id) {
        return clienteRepository.buscarPorId(id);
    }

    public boolean verificarEntregadoresDisponiveis() {
        long countEntregadores = entregadorRepository.getEntregadores().stream()
                .filter(e -> e.getStatus() == EntregadorStatus.DISPONIVEL)
                .count();

        return countEntregadores > 0;
    }

    public void atualizarPedido(Pedido pedido) {
        pedidoRepository.atualizar(pedido);
    }
}