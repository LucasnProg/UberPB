package org.example.model.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Entidade Motorista")
class MotoristaTest {

    private Motorista motorista;

    @BeforeEach
    void setUp() {
        motorista = new Motorista("Ana", "ana@gmail.com", "1234", "111.222.333-44", "83922222222");
    }

    @Test
    @DisplayName("Deve criar o motorista como DISPONIVEL por padrão")
    void testeConstrutor() {
        assertEquals(MotoristaStatus.DISPONIVEL, motorista.getStatus());
        assertNotNull(motorista.getCorridasNotificadas());
        assertNotNull(motorista.getCorridasAceitas());
    }

    @Test
    @DisplayName("Deve adicionar corrida notificada à lista corretamente")
    void testeAdicionarCorridaNotificada() {
        Corrida corridaMock = new Corrida();
        corridaMock.setId(99);

        motorista.adicionarCorridaNotificada(corridaMock);

        assertEquals(1, motorista.getCorridasNotificadas().size());
        assertEquals(99, motorista.getCorridasNotificadas().getFirst().getId());
    }

    @Test
    @DisplayName("Deve validar Getters e Setters de Motorista")
    void testeGettersESetters() {
        Localizacao loc = new Localizacao(10.0, 20.0);
        List<Corrida> aceitas = new ArrayList<>();
        aceitas.add(new Corrida());

        motorista.setChavePix("ana_pix");
        motorista.setLocalizacao(loc);
        motorista.setIdVeiculo(5);
        motorista.setStatus(MotoristaStatus.EM_CORRIDA);
        motorista.setCorridasAceitas(aceitas);

        assertAll("Motorista Getters e Setters",
                () -> assertEquals("ana_pix", motorista.getChavePix()),
                () -> assertEquals(loc, motorista.getLocalizacao()),
                () -> assertEquals(5, motorista.getIdVeiculo()),
                () -> assertEquals(MotoristaStatus.EM_CORRIDA, motorista.getStatus()),
                () -> assertEquals(1, motorista.getCorridasAceitas().size())
        );
    }
}