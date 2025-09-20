package org.example.view;

import org.example.model.entity.Motorista;
import org.example.model.entity.Passageiro;
import org.example.model.service.MotoristaService;
import org.example.model.service.PassageiroService;

/**
 * View responsável pelo fluxo de login do usuário.
 */
public class LoginView {
    private static final PassageiroService passageiroService = new PassageiroService();
    private static final MotoristaService motoristaService = new MotoristaService();

    /**
     * Exibe a interface de login, coleta as credenciais e direciona o usuário.
     */
    public static void loginUsuario() {
        ViewUtils.limparConsole();
        System.out.println("--- Login ---");
        System.out.println("(Digite 'voltar' a qualquer momento para retornar ao menu)");
        int tipoLogin = 0;

        while (true) {
            System.out.println("\nComo você deseja logar?\n1 - Como Passageiro\n2 - Como Motorista");
            System.out.print("Escolha uma opção: ");
            String input = ViewUtils.sc.nextLine();
            if (input.equalsIgnoreCase("voltar")) return;
            try {
                tipoLogin = Integer.parseInt(input);
                if (tipoLogin == 1 || tipoLogin == 2) break;
                else System.out.println("\n[ERRO] Opção inválida.");
            } catch (NumberFormatException e) {
                System.out.println("\n[ERRO] Entrada inválida.");
            }
        }

        System.out.print("\nEmail: ");
        String email = ViewUtils.sc.nextLine();
        if (email.equalsIgnoreCase("voltar")) return;

        System.out.print("Senha: ");
        String senha = ViewUtils.sc.nextLine();
        if (senha.equalsIgnoreCase("voltar")) return;

        if (tipoLogin == 1) {
            Passageiro passageiroLogado = passageiroService.login(email, senha);
            if (passageiroLogado != null) {
                System.out.println("\nLogin como Passageiro bem-sucedido! Pressione ENTER para continuar.");
                ViewUtils.sc.nextLine();
                MenuPassageiroView.exibir(passageiroLogado);
            }
        } else { // tipoLogin == 2
            Motorista motoristaLogado = motoristaService.login(email, senha);
            if (motoristaLogado != null) {
                System.out.println("\nLogin como Motorista bem-sucedido! Pressione ENTER para continuar.");
                ViewUtils.sc.nextLine();
                CorridaView.menuMotorista(motoristaLogado);
            }
        }
    }
}