package org.example.model.repository;

import org.example.model.entity.CategoriaVeiculo;
import org.example.model.entity.Veiculo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class VeiculoRepositoryTest {

    private VeiculoRepository veiculoRepository;
    private File tempFile;

    @BeforeEach
    void setUp() throws Exception {
        veiculoRepository = new VeiculoRepository();
        tempFile = File.createTempFile("veiculos_test_", ".json");

        Field field = VeiculoRepository.class.getDeclaredField("veiculosDB");
        field.setAccessible(true);
        field.set(veiculoRepository, new JsonRepository<>(tempFile.getAbsolutePath(), Veiculo.class));
    }

    @AfterEach
    void tearDown() {
        if (tempFile.exists()) tempFile.delete();
    }

    @Test
    void testSalvarEBuscarPorPlaca() {
        Veiculo v = new Veiculo("Honda", "Civic", "ABC1234", "111222", 2020, "Preto", 400f, 5, false, CategoriaVeiculo.UBER_X);
        veiculoRepository.salvar(v);

        Veiculo encontrado = veiculoRepository.buscarPorPlaca("ABC1234");
        assertNotNull(encontrado);
        assertEquals("Honda", encontrado.getMarca());
        assertEquals("Civic", encontrado.getModelo());

        Veiculo encontradoIgnorandoCase = veiculoRepository.buscarPorPlaca("abc1234");
        assertNotNull(encontradoIgnorandoCase, "A busca pela placa deve ignorar maiúsculas e minúsculas.");
    }

    @Test
    void testAutoIncrementoDeId() {
        Veiculo v1 = new Veiculo("VW", "Gol", "AAA0000", "000", 2015, "Branco", 280f, 5, false, CategoriaVeiculo.UBER_X);
        Veiculo v2 = new Veiculo("Fiat", "Argo", "BBB1111", "111", 2019, "Vermelho", 300f, 5, false, CategoriaVeiculo.UBER_X);

        veiculoRepository.salvar(v1);
        veiculoRepository.salvar(v2);

        assertEquals(1, v1.getId());
        assertEquals(2, v2.getId());
    }
}