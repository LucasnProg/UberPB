package org.example.view;



public class MenuPrincipalView {

    public static void exibirMenu() {
        int opcao = -1;

        while (opcao != 0) {
            ViewUtils.limparConsole();
            System.out.println("=== UberPB CLI - Menu Principal ===");
            System.out.println("1 - Cadastro");
            System.out.println("2 - Login");
            System.out.println("0 - Sair");
            System.out.print("\nEscolha uma opção: ");

            try {
                opcao = Integer.parseInt(ViewUtils.sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("\n[ERRO] Opção inválida! Pressione ENTER para tentar novamente.");
                ViewUtils.sc.nextLine();
                continue;
            }

            switch (opcao) {
                case 1:
                    CadastroView.cadastrarUsuario();
                    break;
                case 2:
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