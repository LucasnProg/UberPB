package org.example.model.entity;

/**
 * Representa os tipos de "partes" que podem participar de uma avaliação.
 *
 * Observação: Mantemos isso genérico porque o projeto possui módulos de
 * corridas (Passageiro/Motorista) e pedidos (Restaurante/Entregador).
 */
public enum TipoParteAvaliacao {
    PASSAGEIRO,
    MOTORISTA,
    ENTREGADOR,
    RESTAURANTE
}