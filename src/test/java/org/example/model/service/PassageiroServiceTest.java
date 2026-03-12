package org.example.model.service;

import org.example.model.entity.Passageiro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PassageiroServiceTest {

    private PassageiroService passageiroService;

    @BeforeEach
    void setUp() {
        passageiroService = new PassageiroService();
    }

    @Test
    void testLogicaDeCalculoDeAvaliacao() {
        Passageiro passageiro = new Passageiro("Teste", "teste@gmail.com", "123", "000", "999");

        if (passageiro.getAvaliacao() == null) {
            passageiro.setAvaliacao(5.0);
        }
        assertEquals(5.0, passageiro.getAvaliacao());

        Double notaRecebida = 3.0;
        if (passageiro.getAvaliacao() != null) {
            Double novaAvaliacao = (passageiro.getAvaliacao() + notaRecebida) / 2;
            passageiro.setAvaliacao(novaAvaliacao);
        }
        assertEquals(4.0, passageiro.getAvaliacao(), "A média de avaliação calculada está incorreta.");
    }
}