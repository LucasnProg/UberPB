package org.example.model.entity;

/**
 * Enum que define as formas de pagamento aceitas no sistema.
 */
public enum FormaPagamento {
    CARTAO_CREDITO("Cartão de Crédito"),
    PIX("PIX"),
    PAYPAL("PayPal"),
    DINHEIRO("Dinheiro");

    private final String descricao;

    FormaPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}