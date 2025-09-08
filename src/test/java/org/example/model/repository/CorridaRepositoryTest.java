package org.example.model.repository;

import org.example.model.entity.Corrida;
import org.example.model.entity.CategoriaVeiculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CorridaRepositoryTest {

    private CorridaRepository repository;

    @BeforeEach
    void setup() {
        File file = new File("src/test/resources/data/corridas-test.json");
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) parentDir.mkdirs();
        if (file.exists()) file.delete();

        repository = new CorridaRepository("src/test/resources/data/corridas-test.json");
    }

    @Test
    void deveSalvarENaoEstarVazio() {
        Corrida corrida = new Corrida(1, "Centro", "Aeroporto", CategoriaVeiculo.UBER_X);
        corrida.setHoraInicio(LocalDateTime.now());
        corrida.setHoraFim(LocalDateTime.now().plusMinutes(30));

        repository.salvarCorrida(corrida);

        List<Corrida> corridas = repository.getCorridas();
        assertFalse(corridas.isEmpty());
        assertEquals(1, corridas.get(0).getId());
    }

    @Test
    void deveBuscarPorId() {
        Corrida corrida = new Corrida(2, "Praça", "Estação", CategoriaVeiculo.UBER_COMFORT);
        repository.salvarCorrida(corrida);

        Corrida encontrada = repository.buscarPorId(1);
        assertNotNull(encontrada);
        assertEquals("Praça", encontrada.getOrigem());
        assertEquals("Estação", encontrada.getDestino());
        assertEquals(CategoriaVeiculo.UBER_COMFORT, encontrada.getCategoriaVeiculo());
    }

    @Test
    void deveVerificarExistenciaDeCorrida() {
        Corrida corrida = new Corrida(3, "Shopping", "Universidade", CategoriaVeiculo.UBER_BLACK);
        repository.salvarCorrida(corrida);

        assertTrue(repository.existeCorrida(1));
        assertFalse(repository.existeCorrida(999));
    }

    @Test
    void deveRemoverCorrida() {
        Corrida corrida = new Corrida(4, "Hospital", "Rodoviária", CategoriaVeiculo.UBER_BAG);
        repository.salvarCorrida(corrida);

        assertTrue(repository.existeCorrida(1));

        repository.remover(1);

        assertFalse(repository.existeCorrida(1));
        assertTrue(repository.getCorridas().isEmpty());
    }

    @Test
    void deveRetornarListaDeCorridas() {
        Corrida c1 = new Corrida(5, "Rua A", "Rua B", CategoriaVeiculo.UBER_X);
        Corrida c2 = new Corrida(6, "Rua C", "Rua D", CategoriaVeiculo.UBER_XL);

        repository.salvarCorrida(c1);
        repository.salvarCorrida(c2);

        List<Corrida> corridas = repository.getCorridas();
        assertEquals(2, corridas.size());
        assertEquals("Rua A", corridas.get(0).getOrigem());
        assertEquals("Rua C", corridas.get(1).getOrigem());
    }
}
