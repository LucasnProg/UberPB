package org.example.view;

import org.example.model.entity.*;
import org.example.model.repository.AvaliacaoRepository;
import org.example.model.repository.PassageiroRepository;
import org.example.model.repository.PedidoRepository;
import org.example.model.repository.RestauranteRepository;

import java.util.List;

/**
 * CLI para o Entregador avaliar a experiência após entregas FINALIZADAS.
 *
 * Salva avaliações (contexto PEDIDO):
 * - ENTREGADOR -> PASSAGEIRO
 * - ENTREGADOR -> RESTAURANTE
 */
public class AvaliarExperienciaEntregadorView {

    private static final PedidoRepository pedidoRepository = new PedidoRepository();
    private static final PassageiroRepository passageiroRepository = new PassageiroRepository();
    private static final RestauranteRepository restauranteRepository = new RestauranteRepository();
    private static final AvaliacaoRepository avaliacaoRepository = new AvaliacaoRepository();

    public static void executar(Entregador entregador) {
        ViewUtils.limparConsole();
        System.out.println("=== Avaliar Experiência (Entregador) ===");

        List<Pedido> finalizados = pedidoRepository.getPedidos().stream()
                .filter(p -> p.getIdEntregador() == entregador.getId())
                .filter(p -> p.getStatusPedido() == StatusCorrida.FINALIZADA)
                .toList();

        if (finalizados.isEmpty()) {
            System.out.println("\nVocê ainda não possui entregas FINALIZADAS para avaliar.");
            System.out.println("Pressione ENTER para voltar.");
            ViewUtils.sc.nextLine();
            return;
        }

        while (true) {
            ViewUtils.limparConsole();
            System.out.println("=== Selecione um Pedido Finalizado ===");
            for (int i = 0; i < finalizados.size(); i++) {
                Pedido p = finalizados.get(i);

                String restauranteNome = restauranteRepository.buscarPorId(p.getIdRestaurante()) != null
                        ? restauranteRepository.buscarPorId(p.getIdRestaurante()).getNome()
                        : ("Restaurante #" + p.getIdRestaurante());

                String clienteNome = passageiroRepository.buscarPorId(p.getIdCliente()) != null
                        ? passageiroRepository.buscarPorId(p.getIdCliente()).getNome()
                        : ("Cliente #" + p.getIdCliente());

                System.out.printf("%d - Pedido #%d | %s -> %s%n", (i + 1), p.getIdPedido(), restauranteNome, clienteNome);
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
            avaliarPedido(entregador, pedido);
        }
    }

    private static void avaliarPedido(Entregador entregador, Pedido pedido) {
        ViewUtils.limparConsole();
        System.out.println("=== Avaliação do Pedido #" + pedido.getIdPedido() + " ===\n");

        Passageiro cliente = passageiroRepository.buscarPorId(pedido.getIdCliente());
        Restaurante restaurante = restauranteRepository.buscarPorId(pedido.getIdRestaurante());

        String clienteNome = (cliente != null) ? cliente.getNome() : ("Cliente #" + pedido.getIdCliente());
        String restauranteNome = (restaurante != null) ? restaurante.getNome() : ("Restaurante #" + pedido.getIdRestaurante());

        // 1) Avaliar Cliente
        if (!avaliacaoRepository.existeAvaliacaoNoPedido(
                pedido.getIdPedido(),
                TipoParteAvaliacao.ENTREGADOR, entregador.getId(),
                TipoParteAvaliacao.PASSAGEIRO, pedido.getIdCliente()
        )) {
            System.out.println("--- Avaliar Cliente ---");
            System.out.println("Cliente: " + clienteNome);
            int nota = lerNota();
            String comentario = lerComentarioOpcional();

            Avaliacao a = new Avaliacao(
                    TipoParteAvaliacao.ENTREGADOR,
                    entregador.getId(),
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

        // 2) Avaliar Restaurante
        if (!avaliacaoRepository.existeAvaliacaoNoPedido(
                pedido.getIdPedido(),
                TipoParteAvaliacao.ENTREGADOR, entregador.getId(),
                TipoParteAvaliacao.RESTAURANTE, pedido.getIdRestaurante()
        )) {
            System.out.println("--- Avaliar Restaurante ---");
            System.out.println("Restaurante: " + restauranteNome);
            int nota = lerNota();
            String comentario = lerComentarioOpcional();

            Avaliacao a = new Avaliacao(
                    TipoParteAvaliacao.ENTREGADOR,
                    entregador.getId(),
                    TipoParteAvaliacao.RESTAURANTE,
                    pedido.getIdRestaurante(),
                    TipoAvaliacaoContexto.PEDIDO,
                    null,
                    pedido.getIdPedido(),
                    nota,
                    comentario
            );
            avaliacaoRepository.salvar(a);
            System.out.println("✅ Avaliação do restaurante registrada.\n");
        } else {
            System.out.println("[INFO] Você já avaliou o restaurante neste pedido.\n");
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