package org.example.model.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MotocicletaTest {

    @Test
    void testMotocicletaGettersESetters() {
        Motocicleta moto = new Motocicleta("Honda", "CG 160", "XYZ9876");

        moto.setId(2);
        assertEquals(2, moto.getId());
        assertEquals("Honda", moto.getFabricante());
        assertEquals("CG 160", moto.getModelo());
        assertEquals("XYZ9876", moto.getPlaca());
    }
}