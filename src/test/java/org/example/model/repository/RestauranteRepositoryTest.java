package org.example.model.repository;

import org.example.model.entity.Localizacao;
import org.example.model.entity.Restaurante;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class RestauranteRepositoryTest {

    private RestauranteRepository restauranteRepository;
    private File tempFile;

    @BeforeEach
    void setUp() throws Exception {
        restauranteRepository = new RestauranteRepository();
        tempFile = File.createTempFile("restaurantes_test_", ".json");

        Field field = RestauranteRepository.class.getDeclaredField("restauranteDB");
        field.setAccessible(true);
        field.set(restauranteRepository, new JsonRepository<>(tempFile.getAbsolutePath(), Restaurante.class));
    }

    @AfterEach
    void tearDown() {
        if (tempFile.exists()) tempFile.delete();
    }

    @Test
    void testSalvarEGetAll() {
        Localizacao loc = new Localizacao(0, 0);
        Restaurante r1 = new Restaurante("Burguer", "burguer@email", "123", "1111", "999", "Lanche", loc);
        Restaurante r2 = new Restaurante("Pizza", "pizza@email", "123", "2222", "888", "Pizzaria", loc);

        restauranteRepository.salvar(r1);
        restauranteRepository.salvar(r2);

        assertEquals(2, restauranteRepository.getAll().size(), "A lista deve conter os dois restaurantes salvos.");
        assertEquals(1, r1.getId());
        assertEquals(2, r2.getId());
    }

    @Test
    void testBuscarPorCnpj() {
        Localizacao loc = new Localizacao(0, 0);
        Restaurante restaurante = new Restaurante("Sushi", "sushi@email", "123", "55555555000199", "999", "Japonesa", loc);
        restauranteRepository.salvar(restaurante);

        Restaurante encontrado = restauranteRepository.buscarPorCnpj("55555555000199");
        assertNotNull(encontrado, "Deve encontrar o restaurante pelo CNPJ.");
        assertEquals("Sushi", encontrado.getNome());
    }
}