package org.example.view;

import org.example.model.entity.Passageiro;

/**
 * View que exibe o menu principal para um passageiro logado.
 */
public class MenuPassageiroView {

    /**
     * Exibe o menu de opções para o passageiro logado.
     * @param passageiro O passageiro que está com a sessão ativa.
     */
    public static void exibir(Passageiro passageiro) {
        int opcao = -1;
        while (opcao != 0) {
            ViewUtils.limparConsole();
            System.out.println("--- Menu do Passageiro ---\nOlá, " + passageiro.getNome() + "!");
            System.out.println("\n1 - Solicitar uma Corrida\n2 - Ver Histórico de Corridas\n0 - Logout");
            System.out.print("\nEscolha uma opção: ");
            try {
                opcao = Integer.parseInt(ViewUtils.sc.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    SolicitarCorridaView.executar(passageiro);
                    break;
                case 2:
                    System.out.println("\nFuncionalidade em desenvolvimento. Pressione ENTER para voltar.");
                    ViewUtils.sc.nextLine();
                    break;
                case 0:
                    System.out.println("\nFazendo logout...");
                    break;
                default:
                    System.out.println("\n[ERRO] Opção inválida! Pressione ENTER.");
                    ViewUtils.sc.nextLine();
            }
        }
    }
}