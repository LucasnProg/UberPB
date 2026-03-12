package org.example.model.repository;

import org.example.model.entity.CategoriaVeiculo;
import org.example.model.entity.Corrida;
import org.example.model.entity.Localizacao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CorridaRepositoryTest {

    private CorridaRepository corridaRepository;
    private File tempFile;

    @BeforeEach
    void setUp() throws Exception {
        tempFile = File.createTempFile("corridas_test_", ".json");

        corridaRepository = new CorridaRepository(tempFile.getAbsolutePath());
    }

    @AfterEach
    void tearDown() {
        if (tempFile.exists()) tempFile.delete();
    }

    @Test
    void testSalvarEBuscarPorId() {
        Localizacao origem = new Localizacao(0, 0);
        Localizacao destino = new Localizacao(1, 1);
        Corrida corrida = new Corrida(10, origem, destino, CategoriaVeiculo.UBER_X);

        corridaRepository.salvar(corrida);

        Corrida encontrada = corridaRepository.buscarPorId(corrida.getId());
        assertNotNull(encontrada, "A corrida salva deve ser encontrada pelo ID.");
        assertEquals(10, encontrada.getPassageiroId());
    }

    @Test
    void testGetCorridasListaCompleta() {
        Localizacao loc = new Localizacao(0, 0);
        Corrida c1 = new Corrida(1, loc, loc, CategoriaVeiculo.UBER_X);
        Corrida c2 = new Corrida(2, loc, loc, CategoriaVeiculo.UBER_BLACK);

        corridaRepository.salvar(c1);
        corridaRepository.salvar(c2);

        List<Corrida> todas = corridaRepository.getCorridas();
        assertEquals(2, todas.size(), "Deve retornar exatamente as 2 corridas salvas.");
    }
}