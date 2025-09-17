package org.example.model.entity;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MotoristaTest {

    @Test
    void testCriacaoMotorista() {
        Motorista motorista = new Motorista(
                "João Silva",
                "joao@email.com",
                "senha123",
                "12345678900",
                "11999999999"
        );

        // Verifica atributos herdados
        assertEquals("João Silva", motorista.getNome());
        assertEquals("joao@email.com", motorista.getEmail());
        assertEquals("senha123", motorista.getSenha());
        assertEquals("12345678900", motorista.getCpf());
        assertEquals("11999999999", motorista.getTelefone());

        assertNotNull(motorista.getCorridasNotificadas());
        assertTrue(motorista.getCorridasNotificadas().isEmpty());
        assertNotNull(motorista.getCorridasAceitas());
        assertTrue(motorista.getCorridasAceitas().isEmpty());
    }

    @Test
    void testSettersAndGetters() {
        Motorista motorista = new Motorista("João", "email", "senha", "cpf", "tel");

        //motorista.setLocalizacao("Centro");
        motorista.setIdVeiculo(42);

        assertEquals("Centro", motorista.getLocalizacao());
        assertEquals(42, motorista.getIdVeiculo());

        ArrayList<Corrida> corridasAceitas = new ArrayList<>();
        motorista.setCorridasAceitas(corridasAceitas);
        assertEquals(corridasAceitas, motorista.getCorridasAceitas());
    }

    @Test
    void testAdicionarCorridaNotificada() {
        Motorista motorista = new Motorista("João", "email", "senha", "cpf", "tel");

        /*Corrida corrida = new Corrida(1, "Centro", "Aeroporto", CategoriaVeiculo.UBER_X);

        motorista.adicionarCorridaNotificada(corrida);

        assertEquals(1, motorista.getCorridasNotificadas().size());
        assertEquals(corrida, motorista.getCorridasNotificadas().get(0));*/
    }
}
