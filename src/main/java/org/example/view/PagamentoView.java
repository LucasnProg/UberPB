package org.example.view;

import org.example.model.entity.Corrida;
import org.example.model.entity.FormaPagamento;
import org.example.model.entity.Motorista;
import org.example.model.service.CorridaService;

/**
 * View responsável por exibir as opções de pagamento e processar a escolha do usuário.
 */
public class PagamentoView {

    private static final CorridaService cs = new CorridaService();

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
        System.out.print("Número do Cartão: ");
        ViewUtils.sc.nextLine();
        System.out.print("Nome no Cartão: ");
        ViewUtils.sc.nextLine();
        System.out.print("Validade (MM/AA): ");
        ViewUtils.sc.nextLine();
        System.out.print("CVV: ");
        ViewUtils.sc.nextLine();
        System.out.println("\nProcessando pagamento... Pagamento Aprovado!");
        System.out.println("Pressione ENTER para continuar.");
        ViewUtils.sc.nextLine();
        return true;
    }

    private static boolean simularFormularioPayPal() {
        ViewUtils.limparConsole();
        System.out.println("--- Pagamento com PayPal ---");
        System.out.print("E-mail do PayPal: ");
        ViewUtils.sc.nextLine();
        System.out.print("Senha: ");
        ViewUtils.sc.nextLine();
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
        System.out.println("Você selecionou pagamento via PIX.");
        System.out.println("A chave PIX do motorista será exibida quando ele aceitar a corrida.");
        System.out.print("\nPressione ENTER para confirmar e procurar um motorista.");
        ViewUtils.sc.nextLine();
        return true;
    }

    private static boolean exibirInstrucoesDinheiro(Corrida corrida) {
        ViewUtils.limparConsole();
        System.out.println("--- Pagamento em Dinheiro ---");
        System.out.println("Você selecionou pagamento em dinheiro.");
        System.out.printf("Por favor, pague o valor de R$ %.2f diretamente ao motorista no final da viagem.\n", corrida.getValor());
        System.out.print("\nPressione ENTER para confirmar e procurar um motorista.");
        ViewUtils.sc.nextLine();
        return true;
    }
}