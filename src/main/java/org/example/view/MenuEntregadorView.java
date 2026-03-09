package org.example.view;

import org.example.model.entity.*;
import org.example.model.service.EntregadorService;
import org.example.model.service.RestauranteService;

import java.util.List;

public class MenuEntregadorView {

    EntregadorService es = new EntregadorService();
    private static RestauranteService restauranteService = new RestauranteService();

    public static void exibir(Entregador entregador) {
        List<Pedido> pedidosNotificados = entregador.getEntregasNotificadas();
        int opcao = -1;
        while (opcao != 0) {
            ViewUtils.limparConsole();
            System.out.println("=== Menu Entregador ===");
            System.out.println("Bem-vindo, " + entregador.getNome());
            System.out.println("Status atual: " + entregador.getStatus());
            System.out.println("-----------------------");
            System.out.println("1 - Ver entregas Notificadas");
            System.out.println("2 - Meus Pedidos Finalizados");
            System.out.println("3 - Avaliar Experiência");
            System.out.println("0 - Logout");
            System.out.print("\nEscolha uma opção: ");

            try {
                opcao = Integer.parseInt(ViewUtils.sc.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    GerenciarPedidosEntregadorView.executar(entregador);
                    break;
                case 2:
                    exibirEntregasFinalizadas(entregador);
                    break;
                case 3:
                    AvaliarExperienciaEntregadorView.executar(entregador);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("\n[ERRO] Opção inválida!");
                    ViewUtils.sc.nextLine();
            }
        }
    }

    private static void exibirEntregasFinalizadas(Entregador entregador){
        List<Pedido> pedidos = entregador.getEntregasAceitas().stream().filter(p -> p.getStatusPedido().equals(StatusCorrida.FINALIZADA)).toList();

        if (pedidos.isEmpty()) {
            System.out.println("Você ainda não tem nenhum pedido finalizado.");
            System.out.println("\nPressione ENTER para voltar.");
            ViewUtils.sc.nextLine();
            return;
        }

        for (Pedido p : pedidos) {
            System.out.println("ID Pedido: " + p.getIdPedido());
            System.out.println("Restaurante : " + restauranteService.buscarPorId(p.getIdRestaurante()).getNome());
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