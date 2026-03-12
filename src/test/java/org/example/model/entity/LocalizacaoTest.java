package org.example.model.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Entidade Localizacao")
class LocalizacaoTest {

    private Localizacao localizacao;

    @BeforeEach
    void setUp() {
        localizacao = new Localizacao(-7.2222, -35.8888);
    }

    @Test
    @DisplayName("Deve inicializar corretamente e validar getters/setters")
    void testeGettersESetters() {
        localizacao.setDescricao("Centro");

        assertAll("Validação dos campos",
                () -> assertEquals("Centro", localizacao.getDescricao()),
                () -> assertEquals(-7.2222, localizacao.getLatitude()),
                () -> assertEquals(-35.8888, localizacao.getLongitude())
        );
    }

    @Test
    @DisplayName("Deve calcular a distância corretamente entre dois pontos geográficos")
    void testeCalcularDistancia() {
        Localizacao pontoA = new Localizacao(-23.550520, -46.633308);
        Localizacao pontoB = new Localizacao(-22.906847, -43.172896);

        double distancia = Localizacao.calcularDistancia(pontoA, pontoB);

        assertTrue(distancia > 350 && distancia < 370, "A distância deve estar coerente com a fórmula de Haversine");

        assertEquals(0, Localizacao.calcularDistancia(pontoA, pontoA));
    }

    @Test
    @DisplayName("Deve validar os métodos Equals, HashCode e ToString")
    void testeOverridesBase() {
        Localizacao loc1 = new Localizacao(-7.11, -34.88);
        Localizacao loc2 = new Localizacao(-7.11, -34.88);
        Localizacao loc3 = new Localizacao(-8.00, -35.00);

        assertAll("Overrides",
                () -> assertEquals(loc1, loc2, "Objetos com mesmas coordenadas devem ser iguais"),
                () -> assertNotEquals(loc1, loc3, "Objetos com coordenadas diferentes devem ser diferentes"),
                () -> assertEquals(loc1.hashCode(), loc2.hashCode(), "HashCodes devem bater"),
                () -> assertTrue(loc1.toString().contains("Lat: -7.11"))
        );
    }
}