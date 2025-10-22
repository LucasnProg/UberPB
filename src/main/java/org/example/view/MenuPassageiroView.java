package org.example.view;

import org.example.model.entity.Passageiro;

public class MenuPassageiroView {

    public static void exibir(Passageiro passageiro) {
        int opcao = -1;
        while (opcao != 0) {
            ViewUtils.limparConsole();
            System.out.println("--- Menu do Passageiro ---");
            System.out.printf("Olá, %s! ⭐ Avaliação Média: %.2f\n",
                    passageiro.getNome(), passageiro.getMediaAvaliacao());
            System.out.println("\n1 - Solicitar uma Corrida");
            System.out.println("2 - Acompanhar Corridas Solicitadas");
            System.out.println("3 - Ver Histórico de Corridas");
            System.out.println("4 - Avaliar Corridas Pendentes");
            System.out.println("0 - Logout");
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
                    AcompanharCorridasView.executar(passageiro);
                    break;
                case 3:
                    HistoricoCorridasView.executar(passageiro);
                    break;
                case 4:
                    AvaliacaoPendentesView.avaliarComoPassageiro(passageiro);
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