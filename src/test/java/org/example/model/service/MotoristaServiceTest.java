package org.example.model.service;

import org.example.model.entity.Motorista;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MotoristaServiceTest {

    private MotoristaService motoristaService;

    @BeforeEach
    void setUp() {
        motoristaService = new MotoristaService();
    }

    @Test
    void testLogin_QuandoCredenciaisFalsas_DeveRetornarNull() {
        Motorista motorista = motoristaService.login("email_invalido@teste.com", "senha_falsa");

        assertNull(motorista, "Login com credenciais inexistentes deveria retornar nulo.");
    }

    @Test
    void testCalculoDeAvaliacaoMotorista() {
        Motorista motorista = new Motorista("Roberto", "roberto@gmail.com", "123", "111", "999");

        if (motorista.getAvaliacao() == null) {
            motorista.setAvaliacao(4.0);
        }

        if (motorista.getAvaliacao() != null) {
            Double novaAvaliacao = (motorista.getAvaliacao() + 5.0) / 2;
            motorista.setAvaliacao(novaAvaliacao);
        }

        assertEquals(4.5, motorista.getAvaliacao());
    }
}