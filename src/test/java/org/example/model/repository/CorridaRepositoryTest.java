package org.example.model.repository;

import org.example.model.entity.Corrida;
import org.example.model.entity.CategoriaVeiculo;
import org.example.model.entity.Localizacao;
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
        Localizacao origem = new Localizacao(-7.1333, -34.8450);
        Localizacao destino = new Localizacao(-7.1338, -34.8410);
        Corrida corrida = new Corrida(1, origem, destino, CategoriaVeiculo.UBER_X);
        corrida.setHoraInicio(LocalDateTime.now());
        corrida.setHoraFim(LocalDateTime.now().plusMinutes(30));

        repository.salvar(corrida);

        List<Corrida> corridas = repository.getCorridas();
        assertFalse(corridas.isEmpty());
        assertEquals(1, corridas.get(0).getId());
    }

    @Test
    void deveBuscarPorId() {
        Localizacao origem = new Localizacao(-7.1330, -34.8400);
        Localizacao destino = new Localizacao(-7.1340, -34.8420);
        Corrida corrida = new Corrida(2, origem, destino, CategoriaVeiculo.UBER_COMFORT);
        repository.salvar(corrida);

        Corrida encontrada = repository.buscarPorId(1);
        assertNotNull(encontrada);
        assertEquals(origem, encontrada.getOrigem());
        assertEquals(destino, encontrada.getDestino());
        assertEquals(CategoriaVeiculo.UBER_COMFORT, encontrada.getCategoriaVeiculo());
    }

    @Test
    void deveRemoverCorrida() {
        Localizacao origem = new Localizacao(-7.1350, -34.8430);
        Localizacao destino = new Localizacao(-7.1360, -34.8440);
        Corrida corrida = new Corrida(3, origem, destino, CategoriaVeiculo.UBER_XL);
        repository.salvar(corrida);

        assertNotNull(repository.buscarPorId(1));

        repository.remover(1);

        assertNull(repository.buscarPorId(1));
        assertTrue(repository.getCorridas().isEmpty());
    }

    @Test
    void deveRetornarListaDeCorridas() {
        Localizacao o1 = new Localizacao(-7.1400, -34.8500);
        Localizacao d1 = new Localizacao(-7.1410, -34.8510);
        Localizacao o2 = new Localizacao(-7.1420, -34.8520);
        Localizacao d2 = new Localizacao(-7.1430, -34.8530);

        Corrida c1 = new Corrida(4, o1, d1, CategoriaVeiculo.UBER_X);
        Corrida c2 = new Corrida(5, o2, d2, CategoriaVeiculo.UBER_XL);

        repository.salvar(c1);
        repository.salvar(c2);

        List<Corrida> corridas = repository.getCorridas();
        assertEquals(2, corridas.size());
        assertEquals(o1, corridas.get(0).getOrigem());
        assertEquals(o2, corridas.get(1).getOrigem());
    }
}
