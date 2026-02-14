package org.example.view;

import org.example.model.entity.Passageiro;
import org.example.model.entity.Restaurante;
import org.example.model.service.RestauranteService;


public class EscolherRestaurantesView {
    private static final RestauranteService rs = new RestauranteService();

    public static Restaurante executar(Passageiro passageiro) {
        while (true) {
            ViewUtils.limparConsole();
            System.out.println("--- Escolha um restaurante ---");

            for (Restaurante restaurante : rs.listarRestaurantes()) {
                System.out.println("\n---------------------------------");
                System.out.println("Digite: " + restaurante.getId());
                System.out.println("Nome: " + restaurante.getNome());
                String categoria = (restaurante.getCategoria() != null) ? restaurante.getCategoria() : "Não informado";
                System.out.println("Categoria: " + categoria);
                String endereco = (restaurante.getEndereco() != null) ? restaurante.getEndereco().getDescricao() : "Não informado";
                System.out.println("Endereço: " + endereco);
                System.out.println("---------------------------------");
            }

            System.out.print("\nEscolha uma opção (0 - Voltar/Cancelar): ");

            String input = ViewUtils.sc.nextLine();
            try {
                int escolha = Integer.parseInt(input);
                if (escolha == 0){
                    return null;
                }
                if (escolha > 0 && escolha <= rs.listarRestaurantes().size()) {
                    RealizarPedidoView.executar(rs.buscarPorId(escolha), passageiro);
                } else {
                    System.out.println("\n[ERRO] Opção inválida. Pressione ENTER para tentar novamente.");
                    ViewUtils.sc.nextLine();
                }
            } catch (NumberFormatException e) {
                System.out.println("\n[ERRO] Entrada inválida. Pressione ENTER para tentar novamente.");
                ViewUtils.sc.nextLine();
            }
        }
    }
}
