package org.example.model.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Entidade Passageiro")
class PassageiroTest {

    private Passageiro passageiro;

    @BeforeEach
    void setUp() {
        passageiro = new Passageiro("Carlos", "carlos@gmail.com", "1234", "123.456.789-00", "83977777777");
    }

    @Test
    @DisplayName("Deve inicializar as listas corretamente no construtor")
    void testeConstrutorEListasIniciais() {
        assertAll("Construtor do Passageiro",
                () -> assertEquals("Carlos", passageiro.getNome()),
                () -> assertNotNull(passageiro.getCorridasPendentes()),
                () -> assertTrue(passageiro.getCorridasPendentes().isEmpty()),
                () -> assertNotNull(passageiro.getHistoricoCorridas()),
                () -> assertTrue(passageiro.getHistoricoCorridas().isEmpty()),
                () -> assertNotNull(passageiro.getPedidosPendentes()),
                () -> assertTrue(passageiro.getPedidosPendentes().isEmpty()),
                () -> assertNotNull(passageiro.getHistoricoPedidos()),
                () -> assertTrue(passageiro.getHistoricoPedidos().isEmpty())
        );
    }

    @Test
    @DisplayName("Deve garantir que getters de lista nunca retornem null (Lazy Initialization)")
    void testeGettersDeListasNulas() {
        passageiro.setHistoricoPedidos(null);
        passageiro.setPedidosPendentes(null);

        assertAll("Prevenção de NullPointerException nas listas",
                () -> assertNotNull(passageiro.getHistoricoPedidos(), "Histórico de pedidos não pode ser null"),
                () -> assertNotNull(passageiro.getPedidosPendentes(), "Pedidos pendentes não pode ser null"),
                () -> assertNotNull(passageiro.getCorridasPendentes(), "Corridas pendentes não pode ser null"),
                () -> assertNotNull(passageiro.getHistoricoCorridas(), "Histórico de corridas não pode ser null")
        );
    }

    @Test
    @DisplayName("Deve validar os Getters e Setters exclusivos de Passageiro")
    void testeGettersESetters() {
        Localizacao casa = new Localizacao(-7.11, -34.88);
        passageiro.setLocalCasa(casa);

        List<Pedido> novosPedidosPendentes = new ArrayList<>();
        novosPedidosPendentes.add(new Pedido());
        passageiro.setPedidosPendentes(novosPedidosPendentes);

        List<Pedido> novoHistorico = new ArrayList<>();
        novoHistorico.add(new Pedido());
        passageiro.setHistoricoPedidos(novoHistorico);

        assertAll("Setters e Getters específicos",
                () -> assertEquals(casa, passageiro.getLocalCasa()),
                () -> assertEquals(1, passageiro.getPedidosPendentes().size()),
                () -> assertEquals(1, passageiro.getHistoricoPedidos().size())
        );
    }
}