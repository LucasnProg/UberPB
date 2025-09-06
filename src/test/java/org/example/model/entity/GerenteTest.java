package org.example.model.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GerenteTest {

    @Test
    void testCriacaoGerente() {
        // Criar gerente
        Gerente gerente = new Gerente(
                "Carlos Silva",
                "carlos@email.com",
                "senha123",
                "12345678900",
                "11999999999"
        );

        // Verificar se os valores foram atribuídos corretamente
        assertEquals("Carlos Silva", gerente.getNome());
        assertEquals("carlos@email.com", gerente.getEmail());
        assertEquals("senha123", gerente.getSenha());
        assertEquals("12345678900", gerente.getCpf());
        assertEquals("11999999999", gerente.getTelefone());
    }
}
