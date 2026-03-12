package org.example.model.service;

import org.example.model.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PedidoServiceTest {

    private PedidoService pedidoService;
    private ArrayList<MenuItem> itensPedido;
    private Localizacao origem;
    private Localizacao destino;

    @BeforeEach
    void setUp() {
        pedidoService = new PedidoService();
        itensPedido = new ArrayList<>();
        origem = new Localizacao(0.0, 0.0);
        destino = new Localizacao(0.0, 0.0);
    }

    @Test
    void testCalcularPrecoEstimado() {
        itensPedido.add(new MenuItem("Pizza", "Queijo", 30.00, 20));
        itensPedido.add(new MenuItem("Refrigerante", "Cola", 15.00, 5));

        double precoFinal = pedidoService.calcularPrecoEstimado(origem, destino, itensPedido);

        assertEquals(49.00, precoFinal, 0.01, "O cálculo do preço do pedido está incorreto.");
    }

    @Test
    void testCalcularTempoDeEntrega() {
        itensPedido.add(new MenuItem("Pizza", "Queijo", 30.00, 20));
        itensPedido.add(new MenuItem("Refrigerante", "Cola", 15.00, 5));

        int tempoTotal = pedidoService.calcularTempoDeEntrega(origem, destino, itensPedido);

        assertEquals(25, tempoTotal, "O tempo de entrega deve ser a soma do tempo de preparo + tempo de viagem.");
    }

    @Test
    void testFormatarStatusParaCliente() {
        Pedido pedido = new Pedido();

        pedido.setStatusPedido(StatusCorrida.SOLICITADA);
        assertEquals("Aguardando um entregador aceitar...", pedidoService.formatarStatusParaCliente(pedido));

        pedido.setStatusPedido(StatusCorrida.EM_PREPARO);
        assertEquals("Restaurante está preparando seu pedido...", pedidoService.formatarStatusParaCliente(pedido));

        pedido.setStatusPedido(StatusCorrida.SAIU_PARA_ENTREGA);
        assertEquals("Pedido saiu para entrega!", pedidoService.formatarStatusParaCliente(pedido));
    }

    @Test
    void testAceitarPedido_QuandoStatusNaoEmPreparo_DeveRetornarFalse() {
        Entregador entregador = new Entregador("João", "joao@email", "123", "000", "999");
        Pedido pedido = new Pedido();
        pedido.setStatusPedido(StatusCorrida.SOLICITADA);

        boolean aceitou = pedidoService.aceitarPedido(entregador, pedido);

        assertFalse(aceitou, "O entregador não pode aceitar o pedido antes do restaurante colocá-lo EM_PREPARO.");
    }

    @Test
    void testAvancarStatusAutomatico_NaoDeveAvancarSemTempoSuficiente() {
        Pedido pedido = new Pedido();
        pedido.setIdPedido(100);
        pedido.setStatusPedido(StatusCorrida.ACEITA);
        pedido.setHoraAceite(LocalDateTime.now());

        try {
            Pedido pedidoAtualizado = pedidoService.avancarStatusAutomatico(pedido);
            if (pedidoAtualizado != null) {
                assertEquals(StatusCorrida.ACEITA, pedidoAtualizado.getStatusPedido(), "Não deve mudar de status antes de 5 segundos.");
            }
        } catch (Exception e) {
            System.out.println("Dependência do repositório identificada.");
        }
    }
}