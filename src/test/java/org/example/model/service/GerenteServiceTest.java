package org.example.model.service;

import org.example.model.entity.Gerente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class GerenteServiceTest {

    private GerenteService service;

    @BeforeEach
    void setUp() {
        service = new GerenteService();
        File arquivo = new File("src/main/resources/data/gerentes.json");
        if (arquivo.exists()) arquivo.delete();
    }

    @Test
    void testeCriarGerenteELogin() {
        Gerente g = service.criar("Rafael", "rafael@email.com", "senha123", "12345678900", "11988888888");
        assertNotNull(g);

        Gerente login = service.login("rafael@email.com", "senha123");
        assertNotNull(login);
        assertEquals(g.getCpf(), login.getCpf());
    }

    @Test
    void testeCriarGerenteEmailDuplicado() {
        service.criar("Bianca", "bianca@email.com", "senha123", "98765432100", "11999999999");
        Gerente duplicado = service.criar("Bianca II", "bianca@email.com", "senha456", "55566677788", "11911111111");
        assertNull(duplicado);
    }
}
