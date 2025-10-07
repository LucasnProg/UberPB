package org.example.view;

import org.example.model.entity.Corrida;
import org.example.model.entity.FormaPagamento;
import org.example.model.entity.Motorista;
import org.example.model.service.CorridaService;
import java.util.regex.Pattern;

/**
 * View responsável por exibir as opções de pagamento e processar a escolha do usuário.
 */
public class PagamentoView {

    private static final CorridaService cs = new CorridaService();

    private static final Pattern CARTAO_NUMERO_PATTERN = Pattern.compile("^\\d{13,19}$");
    private static final Pattern CARTAO_CVV_PATTERN = Pattern.compile("^\\d{3,4}$");
    private static final Pattern CARTAO_VALIDADE_PATTERN = Pattern.compile("^(0[1-9]|1[0-2])/(\\d{2})$");

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");


    /**
     * Exibe o menu de seleção de forma de pagamento e processa a escolhida.
     * @param corrida A corrida que está sendo paga.
     * @return A FormaPagamento escolhida ou null se o pagamento for cancelado.
     */
    public static FormaPagamento executar(Corrida corrida) {
        while (true) {
            ViewUtils.limparConsole();
            System.out.println("--- Pagamento da Corrida ---");
            System.out.printf("Valor a pagar: R$ %.2f\n", corrida.getValor());
            System.out.println("\nSelecione a Forma de Pagamento:");

            int i = 1;
            for (FormaPagamento forma : FormaPagamento.values()) {
                System.out.println(i++ + " - " + forma.getDescricao());
            }
            System.out.println("0 - Cancelar Solicitação");
            System.out.print("\nEscolha uma opção: ");

            try {
                int escolha = Integer.parseInt(ViewUtils.sc.nextLine());
                if (escolha == 0) return null; // Retorna nulo se cancelado

                if (escolha > 0 && escolha <= FormaPagamento.values().length) {
                    FormaPagamento formaEscolhida = FormaPagamento.values()[escolha - 1];
                    corrida.setFormaPagamento(formaEscolhida);
                    // A atualização da corrida agora acontece no service após a confirmação

                    if (processarPagamento(corrida)) {
                        cs.atualizarFormaPagamento(corrida, formaEscolhida);
                        return formaEscolhida; // Retorna a forma de pagamento se o processo foi bem-sucedido
                    } else {
                        // Se o processamento falhar (ex: motorista sem PIX), volta ao menu de seleção
                        continue;
                    }
                } else {
                    System.out.println("\n[ERRO] Opção inválida. Pressione ENTER.");
                    ViewUtils.sc.nextLine();
                }
            } catch (NumberFormatException e) {
                System.out.println("\n[ERRO] Entrada inválida. Pressione ENTER.");
                ViewUtils.sc.nextLine();
            }
        }
    }

    private static boolean processarPagamento(Corrida corrida) {
        switch (corrida.getFormaPagamento()) {
            case CARTAO_CREDITO:
                return simularFormularioCartao();
            case PAYPAL:
                return simularFormularioPayPal();
            case PIX:
                return exibirInstrucoesPix(corrida);
            case DINHEIRO:
                return exibirInstrucoesDinheiro(corrida);
            default:
                return false;
        }
    }



    private static boolean simularFormularioCartao() {
        ViewUtils.limparConsole();
        System.out.println("--- Pagamento com Cartão de Crédito ---");
        String numero, validade, cvv;
        while (true) {
            System.out.print("Número do Cartão (13 a 19 dígitos): ");
            numero = ViewUtils.sc.nextLine().replaceAll("[^0-9]", "");
            if (CARTAO_NUMERO_PATTERN.matcher(numero).matches()) break;
            System.out.println("\n[ERRO] Número de cartão inválido ou fora do formato. Tente novamente.");
        }

        System.out.print("Nome no Cartão: ");
        ViewUtils.sc.nextLine();

        while (true) {
            System.out.print("Validade (MM/AA): ");
            validade = ViewUtils.sc.nextLine();
            if (CARTAO_VALIDADE_PATTERN.matcher(validade).matches()) break;
            System.out.println("\n[ERRO] Validade inválida. Use o formato MM/AA (ex: 12/28). Tente novamente.");
        }

        while (true) {
            System.out.print("CVV (3 ou 4 dígitos): ");
            cvv = ViewUtils.sc.nextLine();
            if (CARTAO_CVV_PATTERN.matcher(cvv).matches()) break;
            System.out.println("\n[ERRO] CVV inválido. Tente novamente.");
        }

        System.out.println("\nProcessando pagamento... Pagamento Aprovado!");
        System.out.println("Pressione ENTER para continuar.");
        ViewUtils.sc.nextLine();
        return true;
    }

    private static boolean simularFormularioPayPal() {
        ViewUtils.limparConsole();
        System.out.println("--- Pagamento com PayPal ---");
        String email, senha;

        while (true) {
            System.out.print("E-mail do PayPal: ");
            email = ViewUtils.sc.nextLine();
            if (EMAIL_PATTERN.matcher(email).matches()) break;
            System.out.println("\n[ERRO] Formato de e-mail inválido. Tente novamente.");
        }

        while (true) {
            System.out.print("Senha (mínimo de 6 caracteres): ");
            senha = ViewUtils.sc.nextLine();
            if (senha.length() >= 6) break;
            System.out.println("\n[ERRO] A senha deve ter no mínimo 6 caracteres.");
        }

        System.out.println("\nProcessando... Pagamento Aprovado!");
        System.out.println("Pressione ENTER para continuar.");
        ViewUtils.sc.nextLine();
        return true;
    }

    private static boolean exibirInstrucoesPix(Corrida corrida) {
        // A busca pelo motorista agora ocorre depois, então não podemos validar a chave PIX aqui.
        // A validação será feita no momento da notificação ao motorista.
        // Por enquanto, apenas confirmamos a intenção de pagar com PIX.
        ViewUtils.limparConsole();
        System.out.println("--- Pagamento via PIX ---");
        System.out.printf("O valor de R$ %.2f será pago diretamente ao motorista no final da viagem.\n", corrida.getValor());
        System.out.println("O motorista irá fornecer sua chave (ou QR Code) assim que a corrida for iniciada.");

        System.out.print("\nPressione ENTER para confirmar a intenção de pagar via PIX e procurar um motorista.");
        ViewUtils.sc.nextLine();
        return true;
    }

    private static boolean exibirInstrucoesDinheiro(Corrida corrida) {
        ViewUtils.limparConsole();
        System.out.println("--- Pagamento em Dinheiro ---");
        System.out.println("Você selecionou pagamento em dinheiro.");
        System.out.printf("Valor a ser pago: R$ %.2f\n", corrida.getValor());

        // 💡 Lógica de Troco:
        System.out.print("Você precisará de troco? (S/N): ");
        String resposta = ViewUtils.sc.nextLine().toUpperCase();

        if (resposta.equals("S")) {
            corrida.setPrecisaTroco(true);
            System.out.println("Motorista será notificado sobre a necessidade de troco.");
        } else {
            corrida.setPrecisaTroco(false);
        }

        System.out.print("\nPressione ENTER para confirmar e procurar um motorista.");
        ViewUtils.sc.nextLine();
        return true;
    }
}