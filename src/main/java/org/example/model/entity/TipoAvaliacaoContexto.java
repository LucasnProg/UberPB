package org.example.model.entity;

/**
 * Define o contexto ao qual uma avaliação está vinculada.
 *
 * - CORRIDA: avaliação típica do Uber (passageiro <-> motorista) após uma corrida.
 * - PEDIDO: avaliação típica do Uber Eats (cliente <-> entregador/restaurante) após um pedido.
 */
public enum TipoAvaliacaoContexto {
    CORRIDA,
    PEDIDO
}