package org.example.view;

import org.example.model.entity.MenuItem;
import org.example.model.entity.Pedido;
import org.example.model.entity.Restaurante;
import org.example.model.service.RestauranteService;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class GerenciarPedidosRestauranteView {

    private static final RestauranteService restauranteService = new RestauranteService();

    public static void executar(Restaurante restaurante) {
        while (true) {
            ViewUtils.limparConsole();
            System.out.println("=== Gerenciar Pedidos Recebidos ===");
            System.out.println("Restaurante: " + restaurante.getNome());
            System.out.println("-----------------------------------");

            List<Pedido> pedidos = restaurante.getPedidosNotificados();

            if (pedidos == null || pedidos.isEmpty()) {
                System.out.println("Nenhum pedido recebido no momento.");
                System.out.println("\nPressione ENTER para voltar.");
                ViewUtils.sc.nextLine();
                return;
            }

            for (Pedido p : pedidos) {
                System.out.println("ID Pedido: " + p.getIdPedido());
                System.out.println("Cliente ID: " + p.getIdCliente());
                System.out.println("Valor: R$ " + String.format("%.2f", p.getValor()));
                if (p.isAgendamento()){
                    System.out.println("Pedido agendado para: " + p.getHoraInicio().format(DateTimeFormatter.ofPattern("HH:mm")));
                }
                System.out.println("Itens:");
                if (p.getItensPedidos() != null) {
                    for (MenuItem item : p.getItensPedidos()) {
                        System.out.println(
                                "  - " + item.getNome() + " (R$ " + String.format("%.2f", item.getPreco()) + ")");
                    }
                }
                System.out.println("-----------------------------------");
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
            System.out.println("1 - Aceitar Pedido (Iniciar Preparo)");
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
                    restauranteService.aceitarPedido(restaurante, pedidoSelecionado);
                    System.out.println("\nPedido aceito e em preparo. Pressione ENTER para continuar.");
                    ViewUtils.sc.nextLine();
                    break;
                case 2:
                    restauranteService.rejeitarPedido(restaurante, pedidoSelecionado);
                    System.out.println("\nPedido rejeitado. Pressione ENTER para continuar.");
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
