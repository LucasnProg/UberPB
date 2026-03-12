package org.example.model.repository;

import org.example.model.entity.Motorista;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class MotoristaRepositoryTest {

    private MotoristaRepository motoristaRepository;
    private File tempFile;

    @BeforeEach
    void setUp() throws Exception {
        motoristaRepository = new MotoristaRepository();

        tempFile = File.createTempFile("motoristas_test_", ".json");

        Field field = MotoristaRepository.class.getDeclaredField("motoristasDB");
        field.setAccessible(true);
        field.set(motoristaRepository, new JsonRepository<>(tempFile.getAbsolutePath(), Motorista.class));
    }

    @AfterEach
    void tearDown() {
        if (tempFile.exists()) tempFile.delete();
    }

    @Test
    void testSalvarEAutoIncrementoDeId() {
        Motorista m1 = new Motorista("M1", "m1@email", "123", "111", "999");
        Motorista m2 = new Motorista("M2", "m2@email", "123", "222", "888");

        motoristaRepository.salvar(m1);
        motoristaRepository.salvar(m2);

        assertEquals(1, m1.getId(), "O primeiro ID gerado deve ser 1.");
        assertEquals(2, m2.getId(), "O segundo ID gerado deve ser 2.");
        assertEquals(2, motoristaRepository.getMotoristas().size());
    }

    @Test
    void testAtualizarMotorista() {
        Motorista motorista = new Motorista("Carlos", "carlos@email.com", "123", "111", "999");
        motoristaRepository.salvar(motorista);

        motorista.setNome("Carlos Silva");
        motoristaRepository.atualizar(motorista);

        Motorista atualizado = motoristaRepository.buscarPorId(motorista.getId());
        assertEquals("Carlos Silva", atualizado.getNome(), "O nome do motorista deveria ter sido atualizado.");
    }

    @Test
    void testBuscarPorEmail() {
        Motorista motorista = new Motorista("Ana", "ana@email.com", "123", "111", "999");
        motoristaRepository.salvar(motorista);

        Motorista encontrado = motoristaRepository.buscarPorEmail("ana@email.com");
        assertNotNull(encontrado);
        assertEquals("Ana", encontrado.getNome());

        Motorista naoEncontrado = motoristaRepository.buscarPorEmail("inexistente@email.com");
        assertNull(naoEncontrado);
    }
}