package org.example.view;

import org.example.model.entity.*;
import org.example.model.service.LocalizacaoService;
import org.example.model.service.PedidoService;
import org.example.model.service.RestauranteService;
import org.example.model.service.SimuladorViagem;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;


public class RealizarPedidoView {
    private static final RestauranteService rs = new RestauranteService();
    private static final PedidoService ps = new PedidoService();
    private static final LocalizacaoService ls = new LocalizacaoService();

    public static void executar(Restaurante restaurante, Passageiro passageiro) {
        ArrayList<MenuItem> itensPedido = new ArrayList<>();
        ViewUtils.limparConsole();
        ArrayList<MenuItem> cardapio = null;
        Localizacao destino = null;
        System.out.printf("\n--- Cardapio de %s ---\n", restaurante.getNome());
        try{
            cardapio = rs.getMenu(restaurante.getId());
        } catch (NullPointerException e){
            System.out.println("\nRestaurante Ainda não possui cardápio cadastrado.");
            return;
        }
        if (cardapio!= null){
            for (MenuItem menuItem : cardapio) {
                System.out.println("Id: "+menuItem.getId());
                System.out.println("Nome: "+menuItem.getNome());
                System.out.println("Ingredientes: "+menuItem.getIngredientes());
                System.out.println("Preço: "+menuItem.getPreco());
                System.out.println("Tempo de Preparo: "+menuItem.getTempoPreparo());
            }
        } else {
            System.out.println("\nCardápio vazio.");
        }

        int idItem = -1;
        while (true) {
            System.out.println("\n"+itensPedido.size()+" Itens adicionados ao Pedido.");
            System.out.print("\nEscolha uma opção (Digite: 0, para concluir o pedido):  ");
            String idItemPedido = ViewUtils.sc.nextLine();


            try {
                idItem = Integer.parseInt(idItemPedido);
            } catch (NumberFormatException e) {
                System.out.println("\n[ERRO] Entrada inválida. Pressione ENTER para tentar novamente.");
                ViewUtils.sc.nextLine();
            }

            if (idItem>0 && idItem<=rs.getMenu(restaurante.getId()).size()){
                itensPedido.add(rs.getItemPorId(restaurante,idItem));
            } else if (idItem == 0) {
                while (true) {
                    int locId;
                    System.out.print("\nSelecione o Local de Entrega: \n");
                    int i = 1;
                    List<Localizacao> bairros = ls.carregarBairros();
                    for (Localizacao local : bairros) {
                        System.out.println(i++ + " - " + local.getDescricao());
                    }

                    System.out.print("\nEscolha uma opção: ");
                    String input = ViewUtils.sc.nextLine();

                    try {
                        locId = Integer.parseInt(input);
                        if (locId > 0 && locId <= bairros.size()) {
                            destino = bairros.get(locId - 1);
                        } else {
                            System.out.println("\n[ERRO] Opção inválida. Pressione ENTER para tentar novamente.");
                            ViewUtils.sc.nextLine();
                        }
                    } catch (NumberFormatException nfe) {
                        System.out.println("\n[ERRO] Formato de entrada inválido.");
                    }
                    break;
                }

                if(!ps.verificarEntregadoresDisponiveis()){
                    System.out.println("\n[INFO] Desculpe, não há motoristas para esta categoria na sua região no momento.");
                    System.out.println("Pressione ENTER para voltar.");
                    ViewUtils.sc.nextLine();
                    return;
                }

                System.out.println("\nCalculando Preço e verificando disponibilidade de motoristas...");

                if(!ps.verificarEntregadoresDisponiveis()){
                    System.out.println("\n[INFO] Desculpe, não há motoristas para esta categoria na sua região no momento.");
                    System.out.println("Pressione ENTER para voltar.");
                    ViewUtils.sc.nextLine();
                    return;
                }

                System.out.println("\nInformações do Pedido:");
                double estimativaValor = ps.calcularPrecoEstimado(restaurante.getEndereco(), destino, itensPedido);
                System.out.printf("Preço Estimado: R$ %.2f\n", estimativaValor );
                System.out.printf("Tempo de Entrega Estimado:  %d Minutos\n", ps.calcularTempoDeEntrega(restaurante.getEndereco(),destino, itensPedido));

                System.out.print("\nDeseja prosseguir para o pagamento e solicitar a corrida? (S/N): ");

                if (ViewUtils.sc.nextLine().equalsIgnoreCase("S")) {
                    Pedido pedidoTemporario = new Pedido();
                    pedidoTemporario.setValor(estimativaValor);

                    FormaPagamento formaPagamento = PagamentoPedidoView.executar(pedidoTemporario);

                    if (formaPagamento != null) {
                        LocalDateTime dataAgendamento = null;
                        System.out.println("\nDeseja agendar seu pedido? (S/N):");
                        if (ViewUtils.sc.nextLine().equalsIgnoreCase("S")) {
                            while (true) {
                                try {
                                    System.out.println("\n--- Formulário de Agendamento de horario---");

                                    System.out.print("Digite o horário (formato: HH:MM): ");
                                    String horaInput = ViewUtils.sc.nextLine();

                                    LocalTime horaAgendada = LocalTime.parse(horaInput, DateTimeFormatter.ofPattern("HH:mm"));
                                    dataAgendamento = LocalDateTime.of(LocalDate.now(), horaAgendada);

                                    if (dataAgendamento.isBefore(LocalDateTime.now())) {
                                        System.out.println("[ERRO] A data de agendamento não pode ser no passado.");
                                        continue;
                                    }

                                    System.out.println("O pedido sairá para entrega próximo ao horario agendado: " + horaAgendada);
                                    break;

                                } catch (DateTimeParseException e) {
                                    System.out.println("[ERRO] Formato de data ou hora inválido. Tente novamente.");
                                }
                            }
                        }

                        System.out.println("\nBuscando motoristas na sua região...");
                        Pedido pedidoSolicitado = ps.realizarPedido(passageiro.getId(), restaurante.getId(), restaurante.getEndereco(), destino, formaPagamento, itensPedido, dataAgendamento);
                        if (pedidoSolicitado != null) {
                            acompanharPedidoPassageiro(pedidoSolicitado);
                        }
                    } else {
                        System.out.println("\nSolicitação cancelada.");
                    }
                } else {
                    System.out.println("\nSolicitação cancelada.");
                }

                System.out.println("\nPressione ENTER para voltar ao menu.");
                ViewUtils.sc.nextLine();
            }
        }

    }

    public static void exibirDetalhesPagamentoAoAceite(Pedido pedido, Entregador entregador) {
        ViewUtils.limparConsole();
        System.out.println("--- Pagamento (Entregador Encontrado) ---");

        if (pedido.getFormaPagamento() == FormaPagamento.PIX) {

            System.out.println("Pagamento: PIX (Ao Motorista no ato da entrega)");
            System.out.printf("Valor: R$ %.2f\n", pedido.getValor());
            System.out.println("----------------------------------------");

        } else if (pedido.getFormaPagamento() == FormaPagamento.DINHEIRO) {
            String trocoInfo = pedido.isPrecisaTroco() ? "PASSAGEIRO PRECISA DE TROCO!" : "Não precisa de troco.";

            System.out.println("Pagamento: DINHEIRO (Ao Motorista no ato da entrega)");
            System.out.printf("Valor: R$ %.2f\n", pedido.getValor());
            System.out.println("----------------------------------------");
            System.out.println("OBSERVAÇÃO:");
            System.out.println(trocoInfo);
            System.out.println("----------------------------------------");

        } else {
            System.out.println("Pagamento: " + pedido.getFormaPagamento().getDescricao() + " (Via Aplicativo)");
            System.out.printf("Valor: R$ %.2f\n", pedido.getValor());
            System.out.println("\nO pagamento já foi processado/será debitado automaticamente. O motorista não precisa cobrar.");
        }

        System.out.println("\nPressione ENTER para iniciar a simulação da viagem.");
        ViewUtils.sc.nextLine();
    }

    private static void acompanharPedidoPassageiro(Pedido pedido) {
        while (true) {
            ViewUtils.limparConsole();
            System.out.println("---- Acompanhando seu Pedido ----");

            Pedido pedidoAtualizado = ps.buscarPedidoPorId(pedido.getIdPedido());
            if (pedidoAtualizado == null) {
                System.out.println("[ERRO] Pedido não encontrado.");
                System.out.println("Pressione ENTER para voltar.");
                ViewUtils.sc.nextLine();
                return;
            }

            // Exibição amigável do status (para o Cliente)
            System.out.println("Pedido #" + pedidoAtualizado.getIdPedido());
            System.out.println("Status: " + ps.formatarStatusParaCliente(pedidoAtualizado));
            System.out.println();

            // Caso seja agendado e ainda não chegou a hora
            if (pedidoAtualizado.getStatusPedido() == StatusCorrida.SOLICITADA
                    && pedidoAtualizado.getHoraInicio() != null
                    && LocalDateTime.now().isBefore(pedidoAtualizado.getHoraInicio())) {

                System.out.println("⏰ Pedido agendado para: " + pedidoAtualizado.getHoraInicio().toLocalTime());
                System.out.println("\n(Atualizando em 5 segundos...)");
                try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
                continue;
            }

            // 1) Enquanto está SOLICITADA, o cliente pode cancelar.
            //    Também fazemos um "aceite automático" para a simulação evoluir sem o menu do entregador.
            if (pedidoAtualizado.getStatusPedido() == StatusCorrida.SOLICITADA) {
                System.out.println("Pressione 'C' e ENTER para CANCELAR a busca, ou apenas aguarde...");
                try {
                    if (System.in.available() > 0) {
                        String input = ViewUtils.sc.nextLine().toUpperCase();
                        if (input.equals("C")) {
                            ps.cancelarPedido(pedidoAtualizado, pedidoAtualizado.getIdCliente());
                            System.out.println("\nPedido cancelado. Pressione ENTER para voltar ao menu.");
                            ViewUtils.sc.nextLine();
                            return;
                        }
                    }

                    // Simula que um entregador aceita após um tempo (para o CLI não ficar travado em SOLICITADA)
                    ps.tentarAceiteAutomatico(pedidoAtualizado);

                    Thread.sleep(5000);
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
                continue;
            }

            // 2) Status intermediários (ACEITA / EM_PREPARO / SAIU_PARA_ENTREGA)
            if (pedidoAtualizado.getStatusPedido() == StatusCorrida.ACEITA
                    || pedidoAtualizado.getStatusPedido() == StatusCorrida.EM_PREPARO
                    || pedidoAtualizado.getStatusPedido() == StatusCorrida.SAIU_PARA_ENTREGA) {

                // Evolui automaticamente (simulação de preparo/saída)
                ps.avancarStatusAutomatico(pedidoAtualizado);

                System.out.println("\n(Atualizando em 5 segundos...)");
                try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
                continue;
            }

            // 3) Quando entra em EM_CURSO, abre o mapa e simula a rota até a entrega.
            if (pedidoAtualizado.getStatusPedido() == StatusCorrida.EM_CURSO) {
                Entregador entregador = ps.getEntregadorById(pedidoAtualizado.getIdEntregador());
                if (entregador != null) {
                    exibirDetalhesPagamentoAoAceite(pedidoAtualizado, entregador);

                    System.out.println("Entregador: " + entregador.getNome());
                    System.out.println("Status: " + ps.formatarStatusParaCliente(pedidoAtualizado));

                    SimuladorViagem.prepararSimulacaoEntrega(pedidoAtualizado, entregador.getLocalizacaoAtual());
                    MapaView.abrirMapa();
                    SimuladorViagem.simularEntrega(pedidoAtualizado);

                    // Finaliza e persiste
                    ps.finalizarCorrida(pedidoAtualizado);

                    // Se você já implementou a feature de avaliação pós-entrega, descomente:
                    // AvaliarEntregaView.executar(pedidoAtualizado);
                } else {
                    System.out.println("[ERRO] Entregador não encontrado. Não foi possível simular a entrega.");
                }

                System.out.println("\nPressione ENTER para voltar ao menu.");
                ViewUtils.sc.nextLine();
                return;
            }

            // 4) Estados finais
            if (pedidoAtualizado.getStatusPedido() == StatusCorrida.FINALIZADA
                    || pedidoAtualizado.getStatusPedido() == StatusCorrida.CANCELADA) {
                System.out.println("Status: " + ps.formatarStatusParaCliente(pedidoAtualizado));
                System.out.println("\nPressione ENTER para voltar ao menu.");
                ViewUtils.sc.nextLine();
                return;
            }

            System.out.println("\n(Atualizando em 5 segundos...)");
            try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
        }
    }
}