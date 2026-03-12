package org.example.model.repository;

import org.example.model.entity.MenuItem;
import org.example.model.entity.Pedido;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PedidoRepositoryTest {

    private PedidoRepository pedidoRepository;
    private File tempFile;

    @BeforeEach
    void setUp() throws Exception {
        tempFile = File.createTempFile("pedidos_test_", ".json");
        pedidoRepository = new PedidoRepository(tempFile.getAbsolutePath());
    }

    @AfterEach
    void tearDown() {
        if (tempFile.exists()) tempFile.delete();
    }

    @Test
    void testSalvarEBuscarPorId() {
        ArrayList<MenuItem> itens = new ArrayList<>();
        itens.add(new MenuItem("Hamburguer", "Pao e carne", 20.0, 15));

        Pedido pedido = new Pedido(5, 10, itens);
        pedidoRepository.salvar(pedido);

        Pedido encontrado = pedidoRepository.buscarPorId(pedido.getIdPedido());
        assertNotNull(encontrado, "O pedido deve ser encontrado pelo seu ID.");
        assertEquals(5, encontrado.getIdCliente());
        assertEquals(10, encontrado.getIdRestaurante());
    }

    @Test
    void testGetPedidos() {
        Pedido p1 = new Pedido(1, 1, new ArrayList<>());
        Pedido p2 = new Pedido(2, 2, new ArrayList<>());

        pedidoRepository.salvar(p1);
        pedidoRepository.salvar(p2);

        List<Pedido> pedidos = pedidoRepository.getPedidos();
        assertEquals(2, pedidos.size());
    }
}