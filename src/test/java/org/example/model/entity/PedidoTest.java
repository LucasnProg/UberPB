package org.example.model.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    @Test
    void testPedidoGettersESetters() {
        Pedido pedido = new Pedido();

        pedido.setIdPedido(100);
        assertEquals(100, pedido.getIdPedido());

        pedido.setIdCliente(10);
        assertEquals(10, pedido.getIdCliente());

        pedido.setIdRestaurante(5);
        assertEquals(5, pedido.getIdRestaurante());

        pedido.setIdEntregador(2);
        assertEquals(2, pedido.getIdEntregador());

        ArrayList<MenuItem> itens = new ArrayList<>();
        pedido.setItensPedidos(itens);
        assertEquals(itens, pedido.getItensPedidos());

        pedido.setValor(55.90);
        assertEquals(55.90, pedido.getValor());

        pedido.setPrecisaTroco(true);
        assertTrue(pedido.isPrecisaTroco());

        pedido.setAceiteRestaurante(true);
        assertTrue(pedido.isAceiteRestaurante());

        pedido.setAceiteEntregador(false);
        assertFalse(pedido.isAceiteEntregador());

        pedido.setAgendamento(true);
        assertTrue(pedido.isAgendamento());

        LocalDateTime agora = LocalDateTime.now();
        pedido.setHoraInicio(agora);
        assertEquals(agora, pedido.getHoraInicio());
    }
}

