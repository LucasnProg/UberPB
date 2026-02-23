package org.example.model.service;

import org.example.model.entity.Localizacao;
import org.example.model.entity.MenuItem;
import org.example.model.entity.Restaurante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class RestauranteServiceTest {

    private RestauranteService service;

    @BeforeEach
    void setUp() {
        service = new RestauranteService();
        File arquivo = new File("src/main/resources/data/restaurantes.json");
        if (arquivo.exists()) arquivo.delete();
    }

    @Test
    void testeCriarERestauranteLogin() {
        Localizacao loc = new Localizacao(0.0, 0.0);
        Restaurante r = service.criar("Rest1", "rest1@email.com", "senha123", "1112223330001", "11977777777", "Italiana", loc);
        assertNotNull(r);

        Restaurante login = service.login("rest1@email.com", "senha123");
        assertNotNull(login);
        assertEquals(r.getCnpj(), login.getCnpj());
    }

    @Test
    void testeAdicionarERemoverItemCardapio() {
        Localizacao loc = new Localizacao(1.0, 1.0);
        Restaurante r = service.criar("Rest2", "rest2@email.com", "senha123", "2223334440001", "11988888888", "Mexicana", loc);

        MenuItem item = new MenuItem("Taco", "Carne", 12.5, 15);
        service.adicionarItemAoCardapio(r, item);

        Restaurante salvo = service.buscarPorId(r.getId());
        assertNotNull(salvo.getMenu());
        assertEquals(1, salvo.getMenu().size());
        assertEquals("Taco", salvo.getMenu().get(0).getNome());

        service.removerItemPorId(salvo, 1);
        Restaurante depois = service.buscarPorId(r.getId());
        assertTrue(depois.getMenu().isEmpty());
    }
}
