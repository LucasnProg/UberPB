package org.example.view;

import org.example.model.entity.Motorista;
import org.example.model.entity.Passageiro;
import org.example.model.service.MotoristaService;
import org.example.model.service.PassageiroService;

public class LoginView {
    // Supondo que você terá um método de login em cada Service
    // Ex: public Passageiro login(String email, String senha)
    private static final PassageiroService ps = new PassageiroService();
    private static final MotoristaService ms = new MotoristaService();

    public static void loginUsuario() {
        ViewUtils.limparConsole();
        System.out.println("--- Login ---");
        System.out.println("(Digite 'voltar' a qualquer momento para retornar ao menu)");

        int tipoLogin = 0;

        // 1. PERGUNTAR O TIPO DE PERFIL
        while (true) {
            System.out.println("\nComo você deseja logar?");
            System.out.println("1 - Como Passageiro");
            System.out.println("2 - Como Motorista");
            System.out.print("Escolha uma opção: ");

            String input = ViewUtils.sc.nextLine();
            if (input.equalsIgnoreCase("voltar")) return;

            try {
                tipoLogin = Integer.parseInt(input);
                if (tipoLogin == 1 || tipoLogin == 2) {
                    break; // Opção válida, sai do loop
                } else {
                    System.out.println("\n[ERRO] Opção inválida. Escolha 1 ou 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n[ERRO] Entrada inválida. Por favor, digite um número.");
            }
        }

        // 2. PEDIR CREDENCIAIS
        System.out.print("\nEmail: ");
        String email = ViewUtils.sc.nextLine();
        if (email.equalsIgnoreCase("voltar")) return;

        System.out.print("Senha: ");
        String senha = ViewUtils.sc.nextLine();
        if (senha.equalsIgnoreCase("voltar")) return;

        // 3. TENTAR LOGIN DE ACORDO COM O TIPO ESCOLHIDO
        if (tipoLogin == 1) {
            // Tenta logar como Passageiro
            ps.login(email, senha);
            Passageiro passageiroLogado = ps.getPassageiroLogado();

            if (passageiroLogado != null) {
                System.out.println("\nLogin como Passageiro bem-sucedido! Pressione ENTER para continuar.");
                ViewUtils.sc.nextLine();
                CorridaView.menuCorridaPassageiro(passageiroLogado);
            } else {
                System.out.println("\n[ERRO] E-mail ou senha inválidos para o perfil de Passageiro. Pressione ENTER para voltar.");
                ViewUtils.sc.nextLine();
            }
        } else { // tipoLogin == 2
            // Tenta logar como Motorista
            ms.login(email, senha); // Você precisará criar este método no seu Service
            Motorista motoristaLogado = ms.getMotoristaLogado();

            if (motoristaLogado != null) {
                System.out.println("\nLogin como Motorista bem-sucedido! Pressione ENTER para continuar.");
                ViewUtils.sc.nextLine();
                CorridaView.menuMotorista(motoristaLogado);
            } else {
                System.out.println("\n[ERRO] E-mail ou senha inválidos para o perfil de Motorista. Pressione ENTER para voltar.");
                ViewUtils.sc.nextLine();
            }
        }
    }
}