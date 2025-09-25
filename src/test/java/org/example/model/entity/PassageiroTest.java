package org.example.model.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PassageiroTest {

    private Passageiro passageiro;

    @BeforeEach
    void setUp() {
        passageiro = new Passageiro("Erick", "erick@email.com", "1234", "00011122233", "999888777");
    }

    @Test
    void testarSolicitarCorridaComLocalizacao() {
        // Criando objetos Localizacao em vez de strings
        Localizacao origem = new Localizacao(-7.1333, -34.8450);
        origem.setDescricao("Casa");
        Localizacao destino = new Localizacao(-7.1338, -34.8410);
        destino.setDescricao("UEPB");

        // Criando corrida manualmente
        Corrida corrida = new Corrida(1, origem, destino, CategoriaVeiculo.UBER_X);

        // Adiciona corrida às pendentes do passageiro
        passageiro.getCorridasPendentes().add(corrida);

        assertEquals(1, passageiro.getCorridasPendentes().size());
        assertEquals(origem, passageiro.getCorridasPendentes().get(0).getOrigem());
        assertEquals(destino, passageiro.getCorridasPendentes().get(0).getDestino());
        assertEquals(CategoriaVeiculo.UBER_X, passageiro.getCorridasPendentes().get(0).getCategoriaVeiculo());
    }
}
