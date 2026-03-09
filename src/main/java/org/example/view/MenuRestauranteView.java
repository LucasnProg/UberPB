package org.example.view;

import org.example.model.entity.*;
import org.example.model.service.EntregadorService;
import org.example.model.service.RestauranteService;

import java.util.List;

public class MenuRestauranteView {

    private static RestauranteService restauranteService = new RestauranteService();
    private static EntregadorService entregadorService = new EntregadorService();

    public static void exibir(Restaurante restaurante) {
        List<Pedido> pedidosNotificados = restaurante.getPedidosNotificados();

        int opcao = -1;
        while (opcao != 0) {
            ViewUtils.limparConsole();
            System.out.println("=== Menu Restaurante ===");
            System.out.println("Bem-vindo, " + restaurante.getNome());
            System.out.println("Categoria: " + restaurante.getCategoria());
            System.out.printf("Você tem %d novo(s) pedidos(s).\n", pedidosNotificados.size());
            System.out.println("-----------------------");
            System.out.println("1 - Gerenciar Cardápio");
            System.out.println("2 - Ver Pedidos Recebidos");
            System.out.println("3 - Avaliar Experiência");
            System.out.println("4 - Ver Pedidos Finalizados");
            System.out.println("0 - Logout");
            System.out.print("\nEscolha uma opção: ");

            try {
                opcao = Integer.parseInt(ViewUtils.sc.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    GerenciarCarpadioView.executar(restaurante);
                    break;
                case 2:
                    GerenciarPedidosRestauranteView.executar(restaurante);
                    break;
                case 3:
                    AvaliarExperienciaRestauranteView.executar(restaurante);
                    break;
                case 4:
                    exibirPedidosFinalizados(restaurante);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("\n[ERRO] Opção inválida!");
                    ViewUtils.sc.nextLine();
            }
        }
    }
    private static void exibirPedidosFinalizados(Restaurante restaurante){
        List<Pedido> pedidos = restaurante.getPedidosAceitos().stream().filter(p -> p.getStatusPedido().equals(StatusCorrida.FINALIZADA)).toList();

        if (pedidos.isEmpty()) {
            System.out.println("Você ainda não tem nenhum pedido finalizado.");
            System.out.println("\nPressione ENTER para voltar.");
            ViewUtils.sc.nextLine();
            return;
        }

        for (Pedido p : pedidos) {
            System.out.println("ID Pedido: " + p.getIdPedido());
            System.out.println("Entregador : " + entregadorService.buscarPorId(p.getIdEntregador()).getNome());
            System.out.println("Valor: R$ " + String.format("%.2f", p.getValor()));
            System.out.println("Itens:");
            if (p.getItensPedidos() != null) {
                for (MenuItem item : p.getItensPedidos()) {
                    System.out.println(
                            "  - " + item.getNome() + " (R$ " + String.format("%.2f", item.getPreco()) + ")");
                }
            }
            System.out.println("-----------------------------------");
        }

        System.out.println("\nPressione ENTER para voltar.");
        ViewUtils.sc.nextLine();
    }
}