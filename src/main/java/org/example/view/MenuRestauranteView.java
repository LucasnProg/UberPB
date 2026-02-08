package org.example.view;

import org.example.model.entity.Restaurante;

public class MenuRestauranteView {

    public static void exibir(Restaurante restaurante) {
        int opcao = -1;
        while (opcao != 0) {
            ViewUtils.limparConsole();
            System.out.println("=== Menu Restaurante ===");
            System.out.println("Bem-vindo, " + restaurante.getNome());
            System.out.println("Categoria: " + restaurante.getCategoria());
            System.out.println("-----------------------");
            System.out.println("1 - Gerenciar Cardápio (Em breve)");
            System.out.println("2 - Ver Pedidos Recebidos (Em breve)");
            System.out.println("0 - Logout");
            System.out.print("\nEscolha uma opção: ");

            try {
                opcao = Integer.parseInt(ViewUtils.sc.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                case 2:
                    System.out.println("\nFuncionalidade em desenvolvimento...");
                    ViewUtils.sc.nextLine();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("\n[ERRO] Opção inválida!");
                    ViewUtils.sc.nextLine();
            }
        }
    }
}
