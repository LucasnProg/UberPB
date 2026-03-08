package org.example.view;

import org.example.model.entity.*;
import org.example.model.repository.AvaliacaoRepository;
import org.example.model.repository.EntregadorRepository;
import org.example.model.repository.PassageiroRepository;
import org.example.model.repository.PedidoRepository;

import java.util.List;

/**
 * CLI para o Restaurante avaliar a experiência após pedidos FINALIZADOS.
 *
 * Salva avaliações (contexto PEDIDO):
 * - RESTAURANTE -> ENTREGADOR
 * - RESTAURANTE -> PASSAGEIRO
 */
public class AvaliarExperienciaRestauranteView {

    private static final PedidoRepository pedidoRepository = new PedidoRepository();
    private static final EntregadorRepository entregadorRepository = new EntregadorRepository();
    private static final PassageiroRepository passageiroRepository = new PassageiroRepository();
    private static final AvaliacaoRepository avaliacaoRepository = new AvaliacaoRepository();

    public static void executar(Restaurante restaurante) {
        ViewUtils.limparConsole();
        System.out.println("=== Avaliar Experiência (Restaurante) ===");

        List<Pedido> finalizados = pedidoRepository.getPedidos().stream()
                .filter(p -> p.getIdRestaurante() == restaurante.getId())
                .filter(p -> p.getStatusPedido() == StatusCorrida.FINALIZADA)
                .toList();

        if (finalizados.isEmpty()) {
            System.out.println("\nVocê ainda não possui pedidos FINALIZADOS para avaliar.");
            System.out.println("Pressione ENTER para voltar.");
            ViewUtils.sc.nextLine();
            return;
        }

        while (true) {
            ViewUtils.limparConsole();
            System.out.println("=== Selecione um Pedido Finalizado ===");
            for (int i = 0; i < finalizados.size(); i++) {
                Pedido p = finalizados.get(i);

                String entregadorNome = (entregadorRepository.buscarPorId(p.getIdEntregador()) != null)
                        ? entregadorRepository.buscarPorId(p.getIdEntregador()).getNome()
                        : ("Entregador #" + p.getIdEntregador());

                String clienteNome = (passageiroRepository.buscarPorId(p.getIdCliente()) != null)
                        ? passageiroRepository.buscarPorId(p.getIdCliente()).getNome()
                        : ("Cliente #" + p.getIdCliente());

                System.out.printf("%d - Pedido #%d | Entregador: %s | Cliente: %s%n",
                        (i + 1), p.getIdPedido(), entregadorNome, clienteNome);
            }
            System.out.println("0 - Voltar");
            System.out.print("\nEscolha uma opção: ");

            int op;
            try {
                op = Integer.parseInt(ViewUtils.sc.nextLine());
            } catch (NumberFormatException e) {
                op = -1;
            }

            if (op == 0) return;
            if (op < 1 || op > finalizados.size()) {
                System.out.println("\n[ERRO] Opção inválida. ENTER para tentar novamente.");
                ViewUtils.sc.nextLine();
                continue;
            }

            Pedido pedido = finalizados.get(op - 1);
            avaliarPedido(restaurante, pedido);
        }
    }

    private static void avaliarPedido(Restaurante restaurante, Pedido pedido) {
        ViewUtils.limparConsole();
        System.out.println("=== Avaliação do Pedido #" + pedido.getIdPedido() + " ===\n");

        Entregador entregador = entregadorRepository.buscarPorId(pedido.getIdEntregador());
        Passageiro cliente = passageiroRepository.buscarPorId(pedido.getIdCliente());

        String entregadorNome = (entregador != null) ? entregador.getNome() : ("Entregador #" + pedido.getIdEntregador());
        String clienteNome = (cliente != null) ? cliente.getNome() : ("Cliente #" + pedido.getIdCliente());

        // 1) Avaliar Entregador
        if (pedido.getIdEntregador() > 0 && !avaliacaoRepository.existeAvaliacaoNoPedido(
                pedido.getIdPedido(),
                TipoParteAvaliacao.RESTAURANTE, restaurante.getId(),
                TipoParteAvaliacao.ENTREGADOR, pedido.getIdEntregador()
        )) {
            System.out.println("--- Avaliar Entregador ---");
            System.out.println("Entregador: " + entregadorNome);
            int nota = lerNota();
            String comentario = lerComentarioOpcional();

            Avaliacao a = new Avaliacao(
                    TipoParteAvaliacao.RESTAURANTE,
                    restaurante.getId(),
                    TipoParteAvaliacao.ENTREGADOR,
                    pedido.getIdEntregador(),
                    TipoAvaliacaoContexto.PEDIDO,
                    null,
                    pedido.getIdPedido(),
                    nota,
                    comentario
            );
            avaliacaoRepository.salvar(a);
            System.out.println("✅ Avaliação do entregador registrada.\n");
        } else {
            System.out.println("[INFO] Você já avaliou o entregador neste pedido (ou não há entregador associado).\n");
        }

        // 2) Avaliar Cliente
        if (!avaliacaoRepository.existeAvaliacaoNoPedido(
                pedido.getIdPedido(),
                TipoParteAvaliacao.RESTAURANTE, restaurante.getId(),
                TipoParteAvaliacao.PASSAGEIRO, pedido.getIdCliente()
        )) {
            System.out.println("--- Avaliar Cliente ---");
            System.out.println("Cliente: " + clienteNome);
            int nota = lerNota();
            String comentario = lerComentarioOpcional();

            Avaliacao a = new Avaliacao(
                    TipoParteAvaliacao.RESTAURANTE,
                    restaurante.getId(),
                    TipoParteAvaliacao.PASSAGEIRO,
                    pedido.getIdCliente(),
                    TipoAvaliacaoContexto.PEDIDO,
                    null,
                    pedido.getIdPedido(),
                    nota,
                    comentario
            );
            avaliacaoRepository.salvar(a);
            System.out.println("✅ Avaliação do cliente registrada.\n");
        } else {
            System.out.println("[INFO] Você já avaliou o cliente neste pedido.\n");
        }

        System.out.println("Pressione ENTER para voltar.");
        ViewUtils.sc.nextLine();
    }

    private static int lerNota() {
        while (true) {
            System.out.print("Nota (1 a 5): ");
            String input = ViewUtils.sc.nextLine();
            try {
                int n = Integer.parseInt(input);
                if (n >= 1 && n <= 5) return n;
            } catch (NumberFormatException ignored) {}
            System.out.println("[ERRO] Nota inválida. Digite 1..5.");
        }
    }

    private static String lerComentarioOpcional() {
        System.out.print("Comentário (opcional, ENTER para pular): ");
        String c = ViewUtils.sc.nextLine();
        if (c == null || c.isBlank()) return null;
        return c;
    }
}