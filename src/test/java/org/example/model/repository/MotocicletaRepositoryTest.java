package org.example.model.repository;

import org.example.model.entity.Motocicleta;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class MotocicletaRepositoryTest {

    private MotocicletaRepository motocicletaRepository;
    private File tempFile;

    @BeforeEach
    void setUp() throws Exception {
        motocicletaRepository = new MotocicletaRepository();
        tempFile = File.createTempFile("motos_test_", ".json");

        Field field = MotocicletaRepository.class.getDeclaredField("motosDB");
        field.setAccessible(true);
        field.set(motocicletaRepository, new JsonRepository<>(tempFile.getAbsolutePath(), Motocicleta.class));
    }

    @AfterEach
    void tearDown() {
        if (tempFile.exists()) tempFile.delete();
    }

    @Test
    void testSalvarEBuscarPorPlacaEMarca() {
        Motocicleta moto = new Motocicleta("Yamaha", "Fazer 250", "YAM9876");
        motocicletaRepository.salvar(moto);

        Motocicleta encontrada = motocicletaRepository.buscarPorPlaca("YAM9876");
        assertNotNull(encontrada);
        assertEquals("Yamaha", encontrada.getFabricante());
        assertEquals("Fazer 250", encontrada.getModelo());
        assertEquals(1, encontrada.getId(), "O primeiro ID deve ser 1");
    }
}