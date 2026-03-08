package org.example.view;

import org.example.model.entity.Avaliacao;
import org.example.model.entity.Entregador;
import org.example.model.entity.Pedido;
import org.example.model.entity.Restaurante;
import org.example.model.entity.TipoAvaliacaoContexto;
import org.example.model.entity.TipoParteAvaliacao;
import org.example.model.repository.AvaliacaoRepository;
import org.example.model.service.PedidoService;
import org.example.model.service.RestauranteService;

/**
 * CLI para o Cliente avaliar Restaurante e Entregador após a entrega.
 *
 * Fluxo:
 * - Só deve ser chamado quando o pedido estiver FINALIZADO (ou imediatamente após finalizar).
 * - Salva 2 avaliações no repositório:
 *   (PASSAGEIRO -> RESTAURANTE) e (PASSAGEIRO -> ENTREGADOR), ambas com contexto PEDIDO.
 */
public class AvaliarEntregaView {

    private static final RestauranteService restauranteService = new RestauranteService();
    private static final PedidoService pedidoService = new PedidoService();
    private static final AvaliacaoRepository avaliacaoRepository = new AvaliacaoRepository();

    public static void executar(Pedido pedido) {
        if (pedido == null) return;

        ViewUtils.limparConsole();
        System.out.println("=== Avaliação da Entrega ===");
        System.out.println("Pedido #" + pedido.getIdPedido());
        System.out.println();

        Restaurante restaurante = restauranteService.buscarPorId(pedido.getIdRestaurante());
        Entregador entregador = pedidoService.getEntregadorById(pedido.getIdEntregador());

        String nomeRestaurante = (restaurante != null) ? restaurante.getNome() : ("Restaurante ID " + pedido.getIdRestaurante());
        String nomeEntregador = (entregador != null) ? entregador.getNome() : ("Entregador ID " + pedido.getIdEntregador());

        // 1) Avaliar Restaurante
        avaliarParte(
                pedido,
                "Restaurante",
                nomeRestaurante,
                TipoParteAvaliacao.RESTAURANTE,
                pedido.getIdRestaurante()
        );

        // 2) Avaliar Entregador
        avaliarParte(
                pedido,
                "Entregador",
                nomeEntregador,
                TipoParteAvaliacao.ENTREGADOR,
                pedido.getIdEntregador()
        );

        System.out.println("\n✅ Obrigado! Avaliações registradas.");
        System.out.println("Pressione ENTER para continuar.");
        ViewUtils.sc.nextLine();
    }

    private static void avaliarParte(Pedido pedido,
                                     String label,
                                     String nome,
                                     TipoParteAvaliacao tipoAvaliado,
                                     int avaliadoId) {

        System.out.println("---- Avaliar " + label + " ----");
        System.out.println(label + ": " + nome);

        // bloqueia duplicidade simples (se o usuário voltar e tentar avaliar de novo)
        if (avaliacaoRepository.existeAvaliacaoNoPedido(
                pedido.getIdPedido(),
                TipoParteAvaliacao.PASSAGEIRO, pedido.getIdCliente(),
                tipoAvaliado, avaliadoId
        )) {
            System.out.println("[INFO] Você já avaliou este(a) " + label.toLowerCase() + " neste pedido. Pulando...");
            System.out.println();
            return;
        }

        int nota = lerNota();
        System.out.print("Comentário (opcional, ENTER para pular): ");
        String comentario = ViewUtils.sc.nextLine();
        if (comentario != null && comentario.isBlank()) {
            comentario = null;
        }

        Avaliacao avaliacao = new Avaliacao(
                TipoParteAvaliacao.PASSAGEIRO,
                pedido.getIdCliente(),
                tipoAvaliado,
                avaliadoId,
                TipoAvaliacaoContexto.PEDIDO,
                null,
                pedido.getIdPedido(),
                nota,
                comentario
        );

        avaliacaoRepository.salvar(avaliacao);
        System.out.println("✅ Avaliação registrada para " + label + ".");
        System.out.println();
    }

    private static int lerNota() {
        while (true) {
            System.out.print("Nota (1 a 5): ");
            String input = ViewUtils.sc.nextLine();
            try {
                int nota = Integer.parseInt(input);
                if (nota >= 1 && nota <= 5) {
                    return nota;
                }
            } catch (NumberFormatException ignored) {}

            System.out.println("[ERRO] Nota inválida. Digite um número entre 1 e 5.");
        }
    }
}