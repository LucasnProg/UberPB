package org.example.model.entity;

import org.example.controller.Sistema;
import org.example.model.service.CorridaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PassageiroTest {

    @BeforeEach
    void setUp() {
        // Limpa a lista de corridas antes de cada teste
        Sistema.corridas.clear();
    }

    @Test
    void testCriacaoPassageiro() {
        Passageiro passageiro = new Passageiro(
                "Maria Silva",
                "maria@email.com",
                "senha123",
                "98765432100",
                "11988888888"
        );

        // Verifica atributos herdados
        assertEquals("Maria Silva", passageiro.getNome());
        assertEquals("maria@email.com", passageiro.getEmail());
        assertEquals("senha123", passageiro.getSenha());
        assertEquals("98765432100", passageiro.getCpf());
        assertEquals("11988888888", passageiro.getTelefone());
    }

    @Test
    void testSolicitarCorrida() {
        Passageiro passageiro = new Passageiro(
                "Maria Silva",
                "maria@email.com",
                "senha123",
                "98765432100",
                "11988888888"
        );

        // Chama método que deve adicionar a corrida ao Sistema
        passageiro.solicitarCorrida("Centro", "Aeroporto", "Economico");

        // Verifica se a corrida foi adicionada
        assertEquals(1, Sistema.corridas.size());

        assertEquals(passageiro.getId(), Sistema.corridas.get(0).getPassageiroId());
        assertEquals("Centro", Sistema.corridas.get(0).getOrigem());
        assertEquals("Aeroporto", Sistema.corridas.get(0).getDestino());
        assertEquals("Economico", Sistema.corridas.get(0).getCategoriaVeiculo());
    }
}
