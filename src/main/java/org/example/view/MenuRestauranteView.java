package org.example.view;

import org.example.model.entity.*;
import org.example.model.service.EntregadorService;
import org.example.model.service.MotoristaService;
import org.example.model.service.PassageiroService;
import org.example.model.service.RestauranteService;

import java.time.format.DateTimeFormatter;
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
            if (restaurante.getAvaliacao() != null){
                System.out.printf("Avaliação: %.1f\n", restaurante.getAvaliacao());
            }else {
                System.out.println("Avaliação: (Ainda não avaliado)");
            }
            System.out.printf("Você tem %d novo(s) pedidos(s).\n", pedidosNotificados.size());
            System.out.println("-----------------------");
            System.out.println("1 - Gerenciar Cardápio");
            System.out.println("2 - Ver Pedidos Recebidos");
            System.out.println("3 - Ver Pedidos Finalizados");
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
            System.out.println("Entregador : " + EntregadorService.buscarPorId(p.getIdEntregador()).getNome());
            System.out.println("Cliente : " + PassageiroService.buscarPorId(p.getIdCliente()).getNome());
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

        while (true) {
            System.out.print("\nDigite o ID do pedido para Avaliar (ou 0 para voltar): ");
            String inputId = ViewUtils.sc.nextLine();

            try {
                int idPedido = Integer.parseInt(inputId);

                if (idPedido == 0) return;

                Pedido pedidoSelecionada = restaurante.getPedidosAceitos().stream()
                        .filter(c -> c.getIdPedido() == idPedido)
                        .findFirst()
                        .orElse(null);

                if (pedidoSelecionada != null) {
                    processarAvaliacao(pedidoSelecionada);
                    return;
                } else {
                    System.out.println("\n[ERRO] ID não encontrado no seu histórico.");
                }

            } catch (NumberFormatException e) {
                System.out.println("\n[ERRO] Digite um número de ID válido.");
            }
        }
    }

    private static void processarAvaliacao(Pedido pedido) {
        while (true) {
            try {
                System.out.print("\nDigite uma nota de 0 a 5 para o Entregador: ");
                String notaInput = ViewUtils.sc.nextLine().replace(",", ".");
                double nota = Double.parseDouble(notaInput);

                if (nota < 0 || nota > 5) {
                    System.out.println("\n[ERRO] A nota deve estar entre 0 e 5.");
                } else {
                    EntregadorService.receberAvaliacao(pedido.getIdEntregador(), nota);
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("\n[ERRO] Formato de nota inválido. Use apenas números (ex: 4.5).");
            }
        }

        while (true) {
            try {
                System.out.print("\nDigite uma nota de 0 a 5 para o Cliente: ");
                String notaInput = ViewUtils.sc.nextLine().replace(",", ".");
                double nota = Double.parseDouble(notaInput);

                if (nota < 0 || nota > 5) {
                    System.out.println("\n[ERRO] A nota deve estar entre 0 e 5.");
                } else {
                    PassageiroService.receberAvaliacao(pedido.getIdCliente(), nota);
                    System.out.println("\nAvaliação enviada com sucesso!");
                    System.out.println("Pressione ENTER para continuar...");
                    ViewUtils.sc.nextLine();
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("\n[ERRO] Formato de nota inválido. Use apenas números (ex: 4.5).");
            }
        }

    }
}