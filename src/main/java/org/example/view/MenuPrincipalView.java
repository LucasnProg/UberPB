package org.example.view;

/**
 * View que exibe o menu principal da aplicação e gerencia a navegação inicial.
 */
public class MenuPrincipalView {

    /**
     * Exibe o menu principal em um loop até que o usuário escolha sair.
     */
    public static void exibirMenu() {
        int opcao = -1;

        while (opcao != 0) {
            ViewUtils.limparConsole();
            System.out.println("=== UberPB CLI - Menu Principal ===");
            System.out.println("1 - Cadastrar como Passageiro/Cliente");
            System.out.println("2 - Cadastrar como Motorista");
            System.out.println("3 - Cadastrar como Restaurante");
            System.out.println("4 - Cadastrar como Entregador");
            System.out.println("5 - Login");
            System.out.println("0 - Sair");
            System.out.print("\nEscolha uma opção: ");

            try {
                opcao = Integer.parseInt(ViewUtils.sc.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    CadastroPassageiroView.executar();
                    break;
                case 2:
                    CadastroMotoristaView.executar();
                    break;
                case 3:
                    CadastroRestauranteView.executar();
                    break;
                case 4:
                    CadastroEntregadorView.executar();
                    break;
                case 5:
                    LoginView.loginUsuario();
                    break;
                case 0:
                    ViewUtils.limparConsole();
                    System.out.println("Obrigado por usar o UberPB CLI. Até logo!");
                    break;
                default:
                    System.out.println("\n[ERRO] Opção inválida! Pressione ENTER para tentar novamente.");
                    ViewUtils.sc.nextLine();
            }
        }
    }
}