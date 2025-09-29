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
     * @return true se o pagamento foi confirmado, false se foi cancelado.
     */
    public static boolean executar(Corrida corrida) {
        while (true) {
            ViewUtils.limparConsole();
            System.out.println("--- Pagamento da Corrida ---");
            System.out.printf("Valor a pagar: R$ %.2f\n", corrida.getValor());
            System.out.println("\nSelecione a Forma de Pagamento:");

            int i = 1;
            for (FormaPagamento forma : FormaPagamento.values()) {
                System.out.println(i++ + " - " + forma.getDescricao());
            }
            System.out.println("0 - Cancelar Corrida");
            System.out.print("\nEscolha uma opção: ");

            try {
                int escolha = Integer.parseInt(ViewUtils.sc.nextLine());
                if (escolha == 0) return false;

                if (escolha > 0 && escolha <= FormaPagamento.values().length) {
                    FormaPagamento formaEscolhida = FormaPagamento.values()[escolha - 1];
                    corrida.setFormaPagamento(formaEscolhida);
                    cs.atualizarCorrida(corrida); // Salva a forma de pagamento na corrida

                    return processarPagamento(corrida);
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
        Motorista motorista = cs.getMotoristaById(corrida.getMotoristaId());
        if (motorista == null || motorista.getChavePix() == null || motorista.getChavePix().trim().isEmpty()) {
            System.out.println("\n[ERRO] O motorista não possui uma chave PIX cadastrada. Tente outra forma de pagamento.");
            System.out.println("Pressione ENTER para voltar.");
            ViewUtils.sc.nextLine();
            return false;
        }

        ViewUtils.limparConsole();
        System.out.println("--- Pagamento via PIX ---");
        System.out.println("Por favor, realize a transferência para o motorista:");
        System.out.printf("Valor: R$ %.2f\n", corrida.getValor());
        System.out.println("Chave PIX: " + motorista.getChavePix());
        System.out.println("\nApós realizar o pagamento no seu aplicativo de banco,");
        System.out.print("Pressione ENTER para confirmar e iniciar a corrida.");
        ViewUtils.sc.nextLine();
        return true;
    }

    private static boolean exibirInstrucoesDinheiro(Corrida corrida) {
        ViewUtils.limparConsole();
        System.out.println("--- Pagamento em Dinheiro ---");
        System.out.println("Você selecionou pagamento em dinheiro.");
        System.out.printf("Por favor, pague o valor de R$ %.2f diretamente ao motorista no final da viagem.\n", corrida.getValor());
        System.out.print("\nPressione ENTER para confirmar e iniciar a corrida.");
        ViewUtils.sc.nextLine();
        return true;
    }
}