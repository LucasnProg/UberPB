package org.example.view;

import org.example.model.service.MotoristaService;
import org.example.model.service.PassageiroService;

import java.util.Scanner;
import java.util.regex.Pattern;

public class CadastroView {
    private static final PassageiroService ps = new PassageiroService();
    private static final MotoristaService ms = new MotoristaService();

    private static final Pattern CPF_PATTERN = Pattern.compile("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    public static void cadastrarUsuario() {
        int tipo;
        String nome, cpf, email, senha, telefone;

        System.out.println("\n--- Cadastro de Usuário ---");
        // NOVA INSTRUÇÃO PARA O USUÁRIO
        System.out.println("(Digite 'voltar' a qualquer momento para cancelar e retornar ao menu)");

        while (true) {
            System.out.println("\n1 - Passageiro");
            System.out.println("2 - Motorista");
            System.out.print("Escolha o tipo de cadastro: ");

            String input = ViewUtils.sc.nextLine();
            // NOVA VERIFICAÇÃO DE CANCELAMENTO
            if (input.equalsIgnoreCase("voltar")) {
                System.out.println("\nCadastro cancelado. Retornando ao menu principal.");
                return; // Sai do método e volta ao menu
            }

            try {
                tipo = Integer.parseInt(input);
                if (tipo == 1 || tipo == 2) {
                    break;
                } else {
                    System.out.println("\n[ERRO] Opção inválida. Por favor, digite 1 para Passageiro ou 2 para Motorista.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\n[ERRO] Entrada inválida. Por favor, digite um número.");
            }
        }

        // --- Validação do Nome ---
        while (true) {
            System.out.print("Nome: ");
            nome = ViewUtils.sc.nextLine();
            if (nome.equalsIgnoreCase("voltar")) {
                System.out.println("\nCadastro cancelado. Retornando ao menu principal.");
                return;
            }
            if (!nome.trim().isEmpty()) {
                break;
            } else {
                System.out.println("\n[ERRO] O nome não pode estar em branco. Por favor, insira novamente.");
            }
        }

        // --- LÓGICA DE CPF MODIFICADA ---
        while (true) {
            System.out.print("CPF (Formato: 000.000.000-00): ");
            cpf = ViewUtils.sc.nextLine();
            if (cpf.equalsIgnoreCase("voltar")) {
                System.out.println("\nCadastro cancelado. Retornando ao menu principal.");
                return;
            }

            if (CPF_PATTERN.matcher(cpf).matches()) {
                boolean cpfJaExiste = false;
                // Verifica o CPF apenas para o tipo de usuário que está sendo cadastrado
                if (tipo == 1) {
                    if (ps.verificarCpf(cpf)) {
                        System.out.println("\n[ERRO] Este CPF já possui um cadastro como Passageiro.");
                        cpfJaExiste = true;
                    }
                } else { // tipo == 2
                    if (ms.verificarCpf(cpf)) {
                        System.out.println("\n[ERRO] Este CPF já possui um cadastro como Motorista.");
                        cpfJaExiste = true;
                    }
                }

                // Se o CPF não foi encontrado na verificação específica, sai do loop
                if (!cpfJaExiste) {
                    break;
                }
            } else {
                System.out.println("\n[ERRO] Formato de CPF inválido. Por favor, siga o formato 000.000.000-00.");
            }
        }

        // --- Demais validações com opção de 'voltar' ---
        while (true) {
            System.out.print("E-mail: ");
            email = ViewUtils.sc.nextLine();
            if (email.equalsIgnoreCase("voltar")) {
                System.out.println("\nCadastro cancelado. Retornando ao menu principal.");
                return;
            }
            if (EMAIL_PATTERN.matcher(email).matches()) {
                break;
            } else {
                System.out.println("\n[ERRO] Formato de e-mail inválido. Por favor, insira um e-mail válido.");
            }
        }

        while (true) {
            System.out.print("Senha (mínimo de 6 caracteres): ");
            senha = ViewUtils.sc.nextLine();
            if (senha.equalsIgnoreCase("voltar")) {
                System.out.println("\nCadastro cancelado. Retornando ao menu principal.");
                return;
            }
            if (senha.length() >= 6) {
                break;
            } else {
                System.out.println("\n[ERRO] A senha deve ter no mínimo 6 caracteres. Por favor, insira novamente.");
            }
        }

        while (true) {
            System.out.print("Telefone (apenas números): ");
            telefone = ViewUtils.sc.nextLine();
            if (telefone.equalsIgnoreCase("voltar")) {
                System.out.println("\nCadastro cancelado. Retornando ao menu principal.");
                return;
            }
            if (telefone.matches("\\d+")) {
                break;
            } else {
                System.out.println("\n[ERRO] O telefone deve conter apenas números. Por favor, insira novamente.");
            }
        }

        // --- Criação do Usuário ---
        if (tipo == 1) {
            ps.criar(nome, email, senha, cpf, telefone);
            System.out.println("\nPassageiro cadastrado com sucesso!");
        } else {
            ms.criar(nome, email, senha, cpf, telefone);
            System.out.println("\nMotorista cadastrado com sucesso!");
        }
    }
}