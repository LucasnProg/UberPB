package org.example.view;

import org.example.model.entity.Motorista;
import org.example.model.service.MotoristaService;
import java.util.regex.Pattern;

/**
 * View para a primeira etapa do cadastro de Motorista (dados pessoais).
 */
public class CadastroMotoristaView {

    private static final MotoristaService motoristaService = new MotoristaService();
    private static final Pattern CPF_PATTERN = Pattern.compile("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    /**
     * Executa o fluxo de cadastro dos dados pessoais do motorista.
     */
    public static void executar() {
        ViewUtils.limparConsole();
        System.out.println("--- Cadastro de Motorista (Etapa 1/2: Dados Pessoais) ---");
        System.out.println("(Digite 'voltar' a qualquer momento para cancelar)");
        String nome, cpf, email, senha, telefone, chavePix;

        while (true) {
            System.out.print("Nome completo: ");
            nome = ViewUtils.sc.nextLine();
            if (nome.equalsIgnoreCase("voltar")) return;
            if (!nome.trim().isEmpty()) break;
            System.out.println("\n[ERRO] O nome não pode estar em branco.");
        }

        while (true) {
            System.out.print("CPF (Formato: 000.000.000-00): ");
            cpf = ViewUtils.sc.nextLine();
            if (cpf.equalsIgnoreCase("voltar")) return;
            if (CPF_PATTERN.matcher(cpf).matches()) break;
            System.out.println("\n[ERRO] Formato de CPF inválido.");
        }

        while (true) {
            System.out.print("E-mail: ");
            email = ViewUtils.sc.nextLine();
            if (email.equalsIgnoreCase("voltar")) return;
            if (EMAIL_PATTERN.matcher(email).matches()) break;
            System.out.println("\n[ERRO] Formato de e-mail inválido.");
        }

        while (true) {
            System.out.print("Senha (mínimo de 6 caracteres): ");
            senha = ViewUtils.sc.nextLine();
            if (senha.equalsIgnoreCase("voltar")) return;
            if (senha.length() >= 6) break;
            System.out.println("\n[ERRO] A senha deve ter no mínimo 6 caracteres.");
        }

        while (true) {
            System.out.print("Telefone (apenas números): ");
            telefone = ViewUtils.sc.nextLine();
            if (telefone.equalsIgnoreCase("voltar")) return;
            if (telefone.matches("\\d+")) break;
            System.out.println("\n[ERRO] O telefone deve conter apenas números.");
        }

        System.out.print("Sua Chave PIX (CPF, E-mail ou Telefone): ");
        chavePix = ViewUtils.sc.nextLine();
        if (chavePix.equalsIgnoreCase("voltar")) return;

        Motorista novoMotorista = motoristaService.criar(nome, email, senha, cpf, telefone);

        if (novoMotorista != null) {
            novoMotorista.setChavePix(chavePix);
            motoristaService.atualizar(novoMotorista);
            CadastroVeiculoView.executar(novoMotorista);
        } else {
            System.out.println("\nCadastro não pôde ser concluído. Pressione ENTER para voltar...");
            ViewUtils.sc.nextLine();
        }
    }
}