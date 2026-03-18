package org.example.view;

import org.example.model.entity.MenuItem;
import org.example.model.entity.Passageiro;
import org.example.model.entity.Pedido;
import org.example.model.entity.StatusCorrida;
import org.example.model.entity.Entregador;
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
                    return;
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
            Entregador entregador = entregadorService.buscarPorId(p.getIdEntregador());
            System.out.println("Entregador : " + (entregador != null ? entregador.getNome() : "Não atribuído"));
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

                Pedido pedidoSelecionada = cliente.getHistoricoPedidos().stream()
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
                System.out.print("\nDigite uma nota de 0 a 5 para o Restaurante: ");
                String notaInput = ViewUtils.sc.nextLine().replace(",", ".");
                double nota = Double.parseDouble(notaInput);

                if (nota < 0 || nota > 5) {
                    System.out.println("\n[ERRO] A nota deve estar entre 0 e 5.");
                } else {
                    RestauranteService.receberAvaliacao(pedido.getIdRestaurante(), nota);
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
            Entregador entregador = entregadorService.buscarPorId(p.getIdEntregador());
            System.out.println("Entregador : " + (entregador != null ? entregador.getNome() : "Aguardando Entregador"));
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
                MapaView.abrirMapa();
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
