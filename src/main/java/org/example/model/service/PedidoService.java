package org.example.model.service;

import org.example.model.entity.*;
import org.example.model.repository.*;

import javax.xml.validation.TypeInfoProvider;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Service central para a lógica de negócio de Pedidos, com busca sequencial de
 * entregadores.
 */
public class PedidoService {

    private final EntregadorRepository entregadorRepository = new EntregadorRepository();
    private static final PedidoRepository pedidoRepository = new PedidoRepository();
    private static final PassageiroRepository clienteRepository = new PassageiroRepository();
    private final RestauranteRepository restauranteRepository = new RestauranteRepository();

    /**
     * Etapa 1 do Fluxo: Cria o pedido, adiciona à lista de pendentes do cliente
     * e inicia a busca pelo primeiro Entregador mais próximo.
     */
    public Pedido realizarPedido(int passageiroId, int restauranteId, Localizacao origem, Localizacao destino,
            FormaPagamento formaPagamento, ArrayList<MenuItem> itensPedidos, LocalDateTime agendamento) {
        double valorEstimado = calcularPrecoEstimado(origem, destino, itensPedidos);
        Pedido novoPedido;
        if (agendamento != null) {
            novoPedido = new Pedido(passageiroId, restauranteId, itensPedidos, agendamento);
        } else {
            novoPedido = new Pedido(passageiroId, restauranteId, itensPedidos);
        }
        novoPedido.setValor(valorEstimado);
        novoPedido.setFormaPagamento(formaPagamento);
        novoPedido.setStatusPedido(StatusCorrida.SOLICITADA);
        novoPedido.setDestino(destino);

        // Para pedidos imediatos, registra o horário de criação. Para agendados, o
        // construtor já recebe 'horaInicio' com o horário.
        if (novoPedido.getHoraInicio() == null) {
            novoPedido.setHoraInicio(LocalDateTime.now());
        }

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
     * 
     * @param pedido         O objeto Corrida a ser atualizado.
     * @param formaPagamento A FormaPagamento escolhida pelo usuário.
     */
    public void atualizarFormaPagamento(Pedido pedido, FormaPagamento formaPagamento) {
        pedido.setFormaPagamento(formaPagamento);
        pedidoRepository.atualizar(pedido);
    }

    /**
     * Cancela um pedido, atualizando o status e removendo-a da lista de pendentes
     * do passageiro.
     * 
     * @param pedido    O pedido a ser cancelada.
     * @param clienteId O ID do passageiro para encontrar e atualizar o objeto
     *                  correto.
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
     * Remover um pedido, atualizando o status e removendo-a da lista de pendentes
     * do passageiro.
     *
     * @param pedido    O pedido a ser cancelada.
     */
    public static void rejeitarPedido(Pedido pedido) {
        pedido.setStatusPedido(StatusCorrida.REJEITADA);
        pedidoRepository.atualizar(pedido);

        Passageiro cliente = clienteRepository.buscarPorId(pedido.getIdCliente());
        if (cliente != null) {
            cliente.getPedidosPendentes().removeIf(p -> p.getIdPedido() == pedido.getIdPedido());
            clienteRepository.atualizar(cliente);
        }
    }

    /**
     * Lógica principal de matchmaking: encontra o entregador elegível mais próximo.
     */
    public void encontrarProximoEntregadorDisponivel(Pedido pedido) {
        if (pedido == null || pedido.getStatusPedido() != StatusCorrida.SOLICITADA)
            return;

        List<Entregador> entregadoresElegiveis = entregadorRepository.getEntregadores().stream()
                .filter(e -> e.getStatus() == EntregadorStatus.DISPONIVEL)
                .filter(e -> !pedido.getEntregadoresQueRejeitaram().contains(e.getId()))
                .sorted(Comparator.comparingDouble(
                        e -> Localizacao.calcularDistancia(e.getLocalizacaoAtual(), pedido.getOrigem())))
                .toList();

        if (entregadoresElegiveis.isEmpty()) {
            System.out
                    .println("\n[INFO] Não há mais entregadores disponíveis para esta solicitação. Corrida cancelada.");
            pedido.setStatusPedido(StatusCorrida.CANCELADA);
            atualizarPedidosCancelados(pedido);
            return;
        }

        Entregador entregadorMaisProximo = entregadoresElegiveis.getFirst();

        System.out.println("\nNotificando o Entregador mais próximo: " + entregadorMaisProximo.getNome());
        pedido.setIdEntregador(entregadorMaisProximo.getId());
        entregadorMaisProximo.adicionarPedidoNotificada(pedido);
        Restaurante restaurante = restauranteRepository.buscarPorId(pedido.getIdRestaurante());
        restaurante.adicionarPedidoNotificado(pedido);
        restauranteRepository.atualizar(restaurante);
        entregadorRepository.atualizar(entregadorMaisProximo);
        pedidoRepository.atualizar(pedido);
    }

    /**
     * Um entregador aceita uma corrida. A corrida é imediatamente iniciada.
     */
    public boolean aceitarPedido(Entregador entregador, Pedido pedido) {
        if (pedido.getStatusPedido() != StatusCorrida.EM_PREPARO) {
            System.out.println("\n[AVISO] O restaurante ainda não aceitou esse pedido, aguarde antes de confirmar a entrega.");
            return false;
        }

        pedido.setAceiteEntregador(true);
        pedido.setHoraAceite(LocalDateTime.now());
        pedido.setStatusPedido(StatusCorrida.EM_CURSO);

        if (pedido.getHoraInicio() == null) {
            pedido.setHoraInicio(LocalDateTime.now());
        }

        PedidoService pedidoService = new PedidoService();

        pedidoService.corridaAceita(pedido);

        System.out.println("A Entrega será realizada em breve! Aguarde.");
        return true;
    }

    private void corridaAceita(Pedido pedido) {
        Restaurante restaurante = restauranteRepository.buscarPorId(pedido.getIdRestaurante());
        Entregador entregador = entregadorRepository.buscarPorId(pedido.getIdEntregador());
        Passageiro cliente = clienteRepository.buscarPorId(pedido.getIdCliente());

        cliente.getPedidosPendentes().removeIf(p -> p.getIdPedido() == pedido.getIdPedido());
        entregador.getEntregasNotificadas().removeIf(p -> p.getIdPedido() == pedido.getIdPedido());
        restaurante.getPedidosAceitos().removeIf(p -> p.getIdPedido() == pedido.getIdPedido());

        entregador.getEntregasAceitas().add(pedido);
        restaurante.getPedidosAceitos().add(pedido);
        cliente.getPedidosPendentes().add(pedido);

        restauranteRepository.atualizar(restaurante);
        entregadorRepository.atualizar(entregador);
        pedidoRepository.atualizar(pedido);
        clienteRepository.atualizar(cliente);
    }

    public void atualizarPedidosCancelados(Pedido pedido) {
        Restaurante restaurante = restauranteRepository.buscarPorId(pedido.getIdRestaurante());
        Entregador entregador = entregadorRepository.buscarPorId(pedido.getIdEntregador());
        Passageiro cliente = clienteRepository.buscarPorId(pedido.getIdCliente());
        entregador.getEntregasNotificadas().remove(pedido);
        restaurante.getPedidosNotificados().remove(pedido);
        cliente.getPedidosPendentes().remove(pedido);

        clienteRepository.atualizar(cliente);
        restauranteRepository.atualizar(restaurante);
        entregadorRepository.atualizar(entregador);
        pedidoRepository.atualizar(pedido);
    }

    /**
     * O entregador finaliza a entrega, atualizando o histórico do passageiro.
     */
    public void finalizarCorrida(Pedido pedido) {
        if (pedido.getStatusPedido() != StatusCorrida.EM_CURSO) {
            System.out.println("O pedido ainda não foi iniciado.");
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

    public void rejeitarPedidoEntregador(Entregador entregador, Pedido pedido) {
        Pedido pedidoAtual = pedidoRepository.buscarPorId(pedido.getIdPedido());
        if (pedidoAtual != null) {
            pedidoAtual.getEntregadoresQueRejeitaram().add(entregador.getId());
            pedidoRepository.atualizar(pedidoAtual);

            limparNotificacaoUnica(entregador, pedidoAtual);

            encontrarProximoEntregadorDisponivel(pedidoAtual);
        }
    }

    public double calcularPrecoEstimado(Localizacao origem, Localizacao destino, ArrayList<MenuItem> itensPedido) {
        double valorDosItens = 0;
        for (MenuItem itens : itensPedido) {
            valorDosItens += itens.getPreco();
        }

        double distanciaKm = Localizacao.calcularDistancia(origem, destino);
        double taxaPorKm = 1.5;
        double taxaFixa = 4.0;

        return (distanciaKm * taxaPorKm) + taxaFixa + valorDosItens;
    }

    public int calcularTempoDeEntrega(Localizacao origem, Localizacao destino, ArrayList<MenuItem> itensPedido) {
        int tempoPreparo = 0;
        for (MenuItem itens : itensPedido) {
            tempoPreparo += itens.getTempoPreparo();
        }
        double distanciaKm = Localizacao.calcularDistancia(origem, destino);
        int velocidadeMediaKmH = 30;
        int tempoEntregaMin = (int) ((distanciaKm / velocidadeMediaKmH) * 60);

        return tempoPreparo + tempoEntregaMin;
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

    /**
     * Simula o aceite automático do pedido (já que o Menu Entregador/Restaurante
     * ainda está "Em breve").
     * Se existir um entregador DISPONÍVEL, o sistema seleciona o mais próximo e
     * aceita o pedido.
     *
     * Esse método é útil para o CLI do Cliente (acompanhamento), permitindo que o
     * status evolua sem
     * depender de um operador no menu do entregador.
     */
    public boolean tentarAceiteAutomatico(Pedido pedido) {
        Pedido pedidoAtual = pedidoRepository.buscarPorId(pedido.getIdPedido());
        if (pedidoAtual == null)
            return false;

        // Só faz sentido se ainda está solicitado e já passou do horário agendado
        // (quando houver)
        if (pedidoAtual.getStatusPedido() != StatusCorrida.SOLICITADA)
            return false;

        if (pedidoAtual.getHoraInicio() != null && LocalDateTime.now().isBefore(pedidoAtual.getHoraInicio())) {
            return false; // ainda não chegou o horário do agendamento
        }

        List<Entregador> entregadoresElegiveis = entregadorRepository.getEntregadores().stream()
                .filter(e -> e.getStatus() == EntregadorStatus.DISPONIVEL)
                .filter(e -> !pedidoAtual.getEntregadoresQueRejeitaram().contains(e.getId()))
                .sorted(Comparator.comparingDouble(
                        e -> Localizacao.calcularDistancia(e.getLocalizacaoAtual(), pedidoAtual.getOrigem())))
                .toList();

        if (entregadoresElegiveis.isEmpty()) {
            return false;
        }

        // Simula o aceite do mais próximo
        return aceitarPedido(entregadoresElegiveis.getFirst(), pedidoAtual);
    }

    /**
     * Evolui automaticamente o status de um pedido após o ACEITE.
     *
     * Regras de tempo (simulação simples para CLI):
     * - ACEITA -> EM_PREPARO (após 5s)
     * - EM_PREPARO -> SAIU_PARA_ENTREGA (após 15s)
     * - SAIU_PARA_ENTREGA -> EM_CURSO (após 20s)
     *
     * Retorna o pedido atualizado do repositório.
     */
    public Pedido avancarStatusAutomatico(Pedido pedido) {
        Pedido pedidoAtual = pedidoRepository.buscarPorId(pedido.getIdPedido());
        if (pedidoAtual == null)
            return null;

        // só avança se já aceito e ainda não está em curso/finalizado/cancelado
        if (pedidoAtual.getStatusPedido() == StatusCorrida.CANCELADA ||
                pedidoAtual.getStatusPedido() == StatusCorrida.FINALIZADA ||
                pedidoAtual.getStatusPedido() == StatusCorrida.EM_CURSO) {
            return pedidoAtual;
        }

        if (pedidoAtual.getHoraAceite() == null) {
            return pedidoAtual;
        }

        long segundos = Duration.between(pedidoAtual.getHoraAceite(), LocalDateTime.now()).getSeconds();

        if (pedidoAtual.getStatusPedido() == StatusCorrida.ACEITA && segundos >= 5) {
            pedidoAtual.setStatusPedido(StatusCorrida.EM_PREPARO);
            pedidoRepository.atualizar(pedidoAtual);
            return pedidoAtual;
        }

        if (pedidoAtual.getStatusPedido() == StatusCorrida.EM_PREPARO && segundos >= 15) {
            pedidoAtual.setStatusPedido(StatusCorrida.SAIU_PARA_ENTREGA);
            pedidoRepository.atualizar(pedidoAtual);
            return pedidoAtual;
        }

        if (pedidoAtual.getStatusPedido() == StatusCorrida.SAIU_PARA_ENTREGA && segundos >= 20) {
            pedidoAtual.setStatusPedido(StatusCorrida.EM_CURSO);
            pedidoRepository.atualizar(pedidoAtual);
            return pedidoAtual;
        }

        return pedidoAtual;
    }

    /**
     * Retorna uma mensagem amigável de status para exibição no CLI do Cliente.
     */
    public String formatarStatusParaCliente(Pedido pedido) {
        if (pedido == null || pedido.getStatusPedido() == null)
            return "Status desconhecido";

        return switch (pedido.getStatusPedido()) {
            case SOLICITADA -> "Aguardando um entregador aceitar...";
            case ACEITA -> "Entregador aceitou! Restaurante recebeu seu pedido.";
            case EM_PREPARO -> "Restaurante está preparando seu pedido...";
            case SAIU_PARA_ENTREGA -> "Pedido saiu para entrega!";
            case EM_CURSO -> "Entregador a caminho do seu endereço!";
            case FINALIZADA -> "Pedido entregue!";
            case CANCELADA -> "Pedido cancelado.";
            case REJEITADA -> "Pedido foi rejeitado pelo Restaurante";
        };
    }

    public ArrayList<Pedido> pedidosPorEntregador(Entregador entregador){
        ArrayList<Pedido> pedidos = new ArrayList<>();
        for (Pedido p : pedidoRepository.carregar()){
            if (p.getIdEntregador() == entregador.getId()){
                pedidos.add(p);
            }
        }

        return pedidos;
    }

    public void atualizarPedido(Pedido pedido) {
        pedidoRepository.atualizar(pedido);
    }
}