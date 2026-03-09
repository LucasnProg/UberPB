package org.example.view;

import org.example.model.entity.MenuItem;
import org.example.model.entity.Passageiro;
import org.example.model.entity.Pedido;
import org.example.model.entity.StatusCorrida;
import org.example.model.service.EntregadorService;
import org.example.model.service.PassageiroService;
import org.example.model.service.RestauranteService;

import java.time.format.DateTimeFormatter;
import java.util.List;


public class AcompanharPedidosClienteView {

    private static PassageiroService passageiroService = new PassageiroService();
    private static RestauranteService restauranteService = new RestauranteService();
    private static EntregadorService entregadorService = new EntregadorService();

    public static void executar(Passageiro cliente) throws InterruptedException {
        while (true) {
            ViewUtils.limparConsole();
            System.out.println("=== Selecione quais pedidos deseja visualizar  ===");

            System.out.println("1 - Visualizar Histórico de Pedidos");
            System.out.println("2 - Visualizar Pedidos Pendentes");
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
                    exibirPedidosRealizados(cliente);
                    return;
                case 2:
                    exibirPedidosPendentes(cliente);
                    return;
                case 0:
                    break;
                default:
                    System.out.println("\n[ERRO] Opção inválida!");
                    ViewUtils.sc.nextLine();
                    break;
            }

        }
    }

    private static void exibirPedidosRealizados(Passageiro cliente){
        List<Pedido> pedidos = cliente.getHistoricoPedidos();

        if (pedidos == null || pedidos.isEmpty()) {
            System.out.println("Você ainda não tem nenhum pedido finalizado.");
            System.out.println("\nPressione ENTER para voltar.");
            ViewUtils.sc.nextLine();
            return;
        }

        for (Pedido p : pedidos) {
            System.out.println("ID Pedido: " + p.getIdPedido());
            System.out.println("Restaurante : " + restauranteService.buscarPorId(p.getIdRestaurante()).getNome());
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


    private static void exibirPedidosPendentes(Passageiro cliente) throws InterruptedException {
        List<Pedido> pedidos = cliente.getPedidosPendentes();
        StatusCorrida statusPedido = StatusCorrida.SOLICITADA;
        if (pedidos == null || pedidos.isEmpty()) {
            System.out.println("Você não tem nenhum pedido pendente.");
            System.out.println("\nPressione ENTER para voltar.");
            ViewUtils.sc.nextLine();
            return;
        }

        for (Pedido p : pedidos) {
            System.out.println("ID Pedido: " + p.getIdPedido());
            System.out.println("Restaurante : " + restauranteService.buscarPorId(p.getIdRestaurante()).getNome());
            System.out.println("Entregador : " + entregadorService.buscarPorId(p.getIdEntregador()).getNome());
            System.out.println("Valor: R$ " + String.format("%.2f", p.getValor()));
            System.out.println("Itens:");
            if (p.getItensPedidos() != null) {
                for (MenuItem item : p.getItensPedidos()) {
                    System.out.println(
                            "  - " + item.getNome() + " (R$ " + String.format("%.2f", item.getPreco()) + ")");
                }
            }
            statusPedido = p.getStatusPedido();
            System.out.println("Status do Pedido:" + statusPedido);
            System.out.println("-----------------------------------");
        }

        if (statusPedido.equals(StatusCorrida.EM_CURSO)){
            System.out.println("\nDeseja acompanhar o trajeto do entregador? (Digite: sim):");
            String escolha = ViewUtils.sc.nextLine();

            if (escolha.equalsIgnoreCase("Sim")){
                System.out.println("ABRE O TRAJETO");
            } else {
                System.out.println("Voltando...");
                Thread.sleep(2000);
                return;
            }
        } else{
            System.out.println("Seu pedido chegará em breve, ele ainda está: " + statusPedido);
        }

        
    }
}
