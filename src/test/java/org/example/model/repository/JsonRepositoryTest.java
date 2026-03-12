package org.example.model.repository;

import org.example.model.entity.Passageiro;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonRepositoryTest {

    private JsonRepository<Passageiro> jsonRepository;
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = File.createTempFile("teste_db_", ".json");
        jsonRepository = new JsonRepository<>(tempFile.getAbsolutePath(), Passageiro.class);
    }

    @AfterEach
    void tearDown() {
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    @Test
    void testSalvarECarregar() {
        List<Passageiro> passageiros = new ArrayList<>();
        Passageiro p1 = new Passageiro("João", "joao@email", "123", "111", "999");
        p1.setId(1);
        passageiros.add(p1);

        jsonRepository.salvar(passageiros);

        List<Passageiro> carregados = jsonRepository.carregar();
        assertEquals(1, carregados.size(), "Deve carregar exatamente 1 passageiro.");
        assertEquals("João", carregados.getFirst().getNome(), "O nome salvo deve ser recuperado corretamente.");
    }

    @Test
    void testCarregarArquivoVazio_DeveRetornarListaVazia() {
        List<Passageiro> carregados = jsonRepository.carregar();
        assertNotNull(carregados, "A lista não deve ser nula, mesmo se o arquivo estiver vazio.");
        assertTrue(carregados.isEmpty(), "A lista deve estar vazia.");
    }
}