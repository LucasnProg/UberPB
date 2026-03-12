package org.example.model.repository;

import org.example.model.entity.Passageiro;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class PassageiroRepositoryTest {

    private PassageiroRepository passageiroRepository;
    private File tempFile;

    @BeforeEach
    void setUp() throws Exception {
        passageiroRepository = new PassageiroRepository();
        tempFile = File.createTempFile("passageiros_test_", ".json");

        Field field = PassageiroRepository.class.getDeclaredField("passageiroDB");
        field.setAccessible(true);
        field.set(passageiroRepository, new JsonRepository<>(tempFile.getAbsolutePath(), Passageiro.class));
    }

    @AfterEach
    void tearDown() {
        if (tempFile.exists()) tempFile.delete();
    }

    @Test
    void testSalvarEBuscarPorCpf() {
        Passageiro passageiro = new Passageiro("Lucas", "lucas@email.com", "senha", "12345678900", "8888");
        passageiroRepository.salvar(passageiro);

        Passageiro encontrado = passageiroRepository.buscarPorCpf("12345678900");
        assertNotNull(encontrado, "Deve encontrar o passageiro pelo CPF.");
        assertEquals("Lucas", encontrado.getNome());
    }

    @Test
    void testAtualizarPassageiro() {
        Passageiro passageiro = new Passageiro("Marcos", "marcos@email.com", "123", "000", "999");
        passageiroRepository.salvar(passageiro);

        passageiro.setSenha("novaSenha");
        passageiroRepository.atualizar(passageiro);

        Passageiro atualizado = passageiroRepository.buscarPorId(passageiro.getId());
        assertEquals("novaSenha", atualizado.getSenha(), "A senha deve ter sido atualizada no banco JSON.");
    }
}