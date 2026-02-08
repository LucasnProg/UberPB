package org.example.view;

import org.example.model.service.RestauranteService;
import java.util.regex.Pattern;

/**
 * View dedicada ao fluxo de cadastro de um novo Restaurante.
 */
public class CadastroRestauranteView {

    private static final RestauranteService restauranteService = new RestauranteService();
    private static final Pattern CNPJ_PATTERN = Pattern.compile("^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    public static void executar() {
        ViewUtils.limparConsole();
        System.out.println("--- Cadastro de Restaurante ---");
        System.out.println("(Digite 'voltar' a qualquer momento para cancelar)");
        String nome, cnpj, email, senha, telefone;

        while (true) {
            System.out.print("Nome do Restaurante: ");
            nome = ViewUtils.sc.nextLine();
            if (nome.equalsIgnoreCase("voltar"))
                return;
            if (!nome.trim().isEmpty())
                break;
            System.out.println("\n[ERRO] O nome não pode estar em branco.");
        }

        while (true) {
            System.out.print("CNPJ (Formato: 00.000.000/0000-00): ");
            cnpj = ViewUtils.sc.nextLine();
            if (cnpj.equalsIgnoreCase("voltar"))
                return;
            if (CNPJ_PATTERN.matcher(cnpj).matches())
                break;
            System.out.println("\n[ERRO] Formato de CNPJ inválido.");
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

        restauranteService.criar(nome, email, senha, cnpj, telefone);

        System.out.println("\nPressione ENTER para continuar...");
        ViewUtils.sc.nextLine();
    }
}
