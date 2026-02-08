package org.example.view;

import org.example.model.service.EntregadorService;
import java.util.regex.Pattern;

/**
 * View dedicada ao fluxo de cadastro de um novo Entregador.
 */
public class CadastroEntregadorView {

    private static final EntregadorService entregadorService = new EntregadorService();
    private static final Pattern CPF_PATTERN = Pattern.compile("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    public static void executar() {
        ViewUtils.limparConsole();
        System.out.println("--- Cadastro de Entregador ---");
        System.out.println("(Digite 'voltar' a qualquer momento para cancelar)");
        String nome, cpf, email, senha, telefone;

        while (true) {
            System.out.print("Nome completo: ");
            nome = ViewUtils.sc.nextLine();
            if (nome.equalsIgnoreCase("voltar"))
                return;
            if (!nome.trim().isEmpty())
                break;
            System.out.println("\n[ERRO] O nome não pode estar em branco.");
        }

        while (true) {
            System.out.print("CPF (Formato: 000.000.000-00): ");
            cpf = ViewUtils.sc.nextLine();
            if (cpf.equalsIgnoreCase("voltar"))
                return;
            if (CPF_PATTERN.matcher(cpf).matches())
                break;
            System.out.println("\n[ERRO] Formato de CPF inválido.");
        }

        while (true) {
            System.out.print("E-mail: ");
            email = ViewUtils.sc.nextLine();
            if (email.equalsIgnoreCase("voltar"))
                return;
            if (EMAIL_PATTERN.matcher(email).matches())
                break;
            System.out.println("\n[ERRO] Formato de e-mail inválido.");
        }

        while (true) {
            System.out.print("Senha (mínimo de 6 caracteres): ");
            senha = ViewUtils.sc.nextLine();
            if (senha.equalsIgnoreCase("voltar"))
                return;
            if (senha.length() >= 6)
                break;
            System.out.println("\n[ERRO] A senha deve ter no mínimo 6 caracteres.");
        }

        while (true) {
            System.out.print("Telefone (apenas números): ");
            telefone = ViewUtils.sc.nextLine();
            if (telefone.equalsIgnoreCase("voltar"))
                return;
            if (telefone.matches("\\d+"))
                break;
            System.out.println("\n[ERRO] O telefone deve conter apenas números.");
        }

        entregadorService.criar(nome, email, senha, cpf, telefone);

        System.out.println("\nPressione ENTER para continuar...");
        ViewUtils.sc.nextLine();
    }
}
