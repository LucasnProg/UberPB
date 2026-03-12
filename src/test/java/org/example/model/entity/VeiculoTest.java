package org.example.model.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VeiculoTest {

    @Test
    void testVeiculoGettersESetters() {
        Veiculo veiculo = new Veiculo("Toyota", "Corolla", "ABC1234", "111222333", 2022, "Prata", 470.5f, 5, true, CategoriaVeiculo.UBER_BLACK);

        veiculo.setId(1);
        assertEquals(1, veiculo.getId());
        assertEquals("Toyota", veiculo.getMarca());
        assertEquals("Corolla", veiculo.getModelo());
        assertEquals("ABC1234", veiculo.getPlaca());
        assertEquals("111222333", veiculo.getRenavam());
        assertEquals(2022, veiculo.getAnoFabricacao());
        assertEquals("Prata", veiculo.getCor());
        assertEquals(470.5f, veiculo.getCapacidadeMala());
        assertEquals(5, veiculo.getNumAssentos());
        assertTrue(veiculo.isPremium());
        assertEquals(CategoriaVeiculo.UBER_BLACK, veiculo.getCategoria());
    }
}

