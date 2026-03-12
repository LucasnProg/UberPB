package org.example.model.service;

import org.example.model.entity.Localizacao;
import org.example.model.entity.MenuItem;
import org.example.model.entity.Restaurante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class RestauranteServiceTest {

    private RestauranteService restauranteService;
    private Restaurante restauranteFake;

    @BeforeEach
    void setUp() {
        restauranteService = new RestauranteService();
        Localizacao loc = new Localizacao(0, 0);
        restauranteFake = new Restaurante("Pizza Boa", "pizza@email.com", "123", "000", "999", "Pizzaria", loc);
        restauranteFake.setId(1);
    }

    @Test
    void testAdicionarEBuscarItemAoCardapio() {
        MenuItem item = new MenuItem("Pizza Calabresa", "Queijo e calabresa", 45.0, 30);

        restauranteService.adicionarItemAoCardapio(restauranteFake, item);

        assertNotNull(restauranteFake.getMenu(), "O menu não pode ser nulo.");
        assertEquals(1, restauranteFake.getMenu().size(), "O menu deve ter 1 item.");

        assertEquals(1, item.getId());

        MenuItem itemBuscado = restauranteService.getItemPorId(restauranteFake, 1);
        assertNotNull(itemBuscado);
        assertEquals("Pizza Calabresa", itemBuscado.getNome());
    }

    @Test
    void testRemoverItemPorId() {
        MenuItem item1 = new MenuItem("Pizza", "Massa", 40.0, 20);
        MenuItem item2 = new MenuItem("Suco", "Laranja", 10.0, 5);

        restauranteService.adicionarItemAoCardapio(restauranteFake, item1);
        restauranteService.adicionarItemAoCardapio(restauranteFake, item2);

        restauranteService.removerItemPorId(restauranteFake, 2);

        assertEquals(1, restauranteFake.getMenu().size());
        assertNull(restauranteService.getItemPorId(restauranteFake, 2), "O item removido não deve mais ser encontrado.");
    }
}