package org.example.view;

import org.example.model.entity.Entregador;
import org.example.model.entity.MenuItem;
import org.example.model.entity.Pedido;
import org.example.model.entity.Restaurante;
import org.example.model.service.PedidoService;
import org.example.model.service.RestauranteService;
import org.example.model.service.SimuladorViagem;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class GerenciarPedidosEntregadorView {

    private static final PedidoService pedidoService = new PedidoService();
    private static final RestauranteService restauranteService = new RestauranteService();

    public static void executar(Entregador entregador) {
        while (true) {
            ViewUtils.limparConsole();
            System.out.println("=== Ver Pedidos Disponíveis ===");
            System.out.println("Entregador: " + entregador.getNome());
            System.out.println("-----------------------------------");

            List<Pedido> pedidos = entregador.getEntregasNotificadas();

            if (pedidos == null || pedidos.isEmpty()) {
                System.out.println("Nenhum pedido disponível no momento.");
                System.out.println("\nPressione ENTER para voltar.");
                ViewUtils.sc.nextLine();
                return;
            }

            for (Pedido p : pedidos) {
                if(p.isAceiteRestaurante()) {
                    System.out.println("ID Pedido: " + p.getIdPedido());
                    System.out.println("Restaurante : " + restauranteService.buscarPorId(p.getIdRestaurante()).getNome());
                    System.out.println("Origem: " + p.getOrigem().getDescricao());
                    System.out.println("Destino: " + p.getDestino().getDescricao());
                    System.out.println("Valor da Entrega: R$ " + String.format("%.2f", p.getValor()));
                    if (p.isAgendamento()) {
                        System.out.println("Pedido agendado para: " + p.getHoraInicio().format(DateTimeFormatter.ofPattern("HH:mm")));
                    }
                    System.out.println("-----------------------------------");
                }
            }

            System.out.println("Digite o ID do pedido que deseja gerenciar (ou 0 para voltar):");
            int idSelecionado = -1;
            try {
                idSelecionado = Integer.parseInt(ViewUtils.sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\n[ERRO] Entrada inválida.");
                ViewUtils.sc.nextLine();
                continue;
            }

            if (idSelecionado == 0) {
                return;
            }

            Pedido pedidoSelecionado = null;
            for (Pedido p : pedidos) {
                if (p.getIdPedido() == idSelecionado) {
                    pedidoSelecionado = p;
                    break;
                }
            }

            if (pedidoSelecionado == null) {
                System.out.println("\n[ERRO] Pedido não encontrado ou não notificado a você. Pressione ENTER.");
                ViewUtils.sc.nextLine();
                continue;
            }

            System.out.println("\nPedido #" + pedidoSelecionado.getIdPedido() + " selecionado!");
            System.out.println("1 - Aceitar Pedido");
            System.out.println("2 - Rejeitar Pedido");
            System.out.println("0 - Voltar");
            System.out.print("\nEscolha uma opção: ");

            int acao = -1;
            try {
                acao = Integer.parseInt(ViewUtils.sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\n[ERRO] Entrada inválida.");
                ViewUtils.sc.nextLine();
                continue;
            }

            switch (acao) {
                case 1:
                    boolean aceito = pedidoService.aceitarPedido(entregador, pedidoSelecionado);
                    if (aceito) {
                        Restaurante restaurante = restauranteService.buscarPorId(pedidoSelecionado.getIdRestaurante());
                        SimuladorViagem.prepararSimulacaoEntrega(pedidoSelecionado,restaurante, entregador);
                        MapaView.abrirMapa();
                        SimuladorViagem.simularEntrega(pedidoSelecionado);
                    }
                    System.out.println("\nPressione ENTER para continuar.");
                    ViewUtils.sc.nextLine();
                    return;
                case 2:
                    pedidoService.rejeitarPedidoEntregador(entregador, pedidoSelecionado);
                    System.out.println("\nPedido rejeitado.");
                    System.out.println("\nPressione ENTER para continuar.");
                    ViewUtils.sc.nextLine();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("\n[ERRO] Opção inválida!");
                    ViewUtils.sc.nextLine();
                    break;
            }
        }
    }
}
