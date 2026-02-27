package org.example.view;

import org.example.model.entity.Entregador;

public class MenuEntregadorView {

    public static void exibir(Entregador entregador) {
        int opcao = -1;
        while (opcao != 0) {
            ViewUtils.limparConsole();
            System.out.println("=== Menu Entregador ===");
            System.out.println("Bem-vindo, " + entregador.getNome());
            System.out.println("Status atual: " + entregador.getStatus());
            System.out.println("-----------------------");
            System.out.println("1 - Ver Pedidos Disponíveis (Em breve)");
            System.out.println("2 - Meus Pedidos Aceitos (Em breve)");
            System.out.println("3 - Alterar Status (Em breve)");
            System.out.println("4 - Avaliar Experiência");
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
                case 3:
                    System.out.println("\nFuncionalidade em desenvolvimento...");
                    ViewUtils.sc.nextLine();
                    break;
                case 4:
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
}