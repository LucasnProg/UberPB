package org.example.model.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MenuItemTest {

    @Test
    void testMenuItemGettersESetters() {
        MenuItem item = new MenuItem("Hambúrguer", "Pão, carne, queijo", 25.50, 15);

        item.setId(1);
        assertEquals(1, item.getId());
        assertEquals("Hambúrguer", item.getNome());
        assertEquals("Pão, carne, queijo", item.getIngredientes());
        assertEquals(25.50, item.getPreco());
        assertEquals(15, item.getTempoPreparo());
    }
}