package org.example.model.repository;

import org.example.model.entity.Corrida;
import org.example.model.entity.StatusCorrida;
import org.junit.jupiter.api.*;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CorridaRepositoryTest {

    private static final String TEST_FILE = "src/test/resources-test/corridas-test.json";
    private CorridaRepository repository;

    @BeforeEach
    void setup() throws Exception {
        // Garante que o arquivo de teste está limpo
        File file = new File(TEST_FILE);
        file.getParentFile().mkdirs();
        Files.writeString(file.toPath(), "[]");

        // Criamos um repository apontando pro JSON de teste
        repository = new CorridaRepository() {
            {
                this.corridasDB = new JsonRepository<>(TEST_FILE, Corrida.class);
                this.corridasCarregadas = corridasDB.carregar();
            }
        };
    }

    @Test
    void testSalvarCorridaEAtribuirId() {
        Corrida corrida = new Corrida(1, "Origem A", "Destino B", "Econômico");
        repository.salvarCorrida(corrida);

        List<Corrida> corridas = repository.getCorridas();
        assertEquals(1, corridas.size());
        assertEquals(1, corridas.get(0).getId());
        assertEquals("Origem A", corridas.get(0).getOrigem());
    }

    @Test
    void testBuscarPorId() {
        Corrida corrida = new Corrida(2, "Ponto X", "Ponto Y", "Luxo");
        repository.salvarCorrida(corrida);

        Corrida encontrada = repository.buscarPorId(1);
        assertNotNull(encontrada);
        assertEquals("Ponto X", encontrada.getOrigem());
    }

    @Test
    void testExisteCorrida() {
        Corrida corrida = new Corrida(3, "A", "B", "Econômico");
        repository.salvarCorrida(corrida);

        assertTrue(repository.existeCorrida(1));
        assertFalse(repository.existeCorrida(99));
    }

    @Test
    void testRemoverCorrida() {
        Corrida corrida1 = new Corrida(1, "O1", "D1", "Econômico");
        Corrida corrida2 = new Corrida(2, "O2", "D2", "Luxo");
        repository.salvarCorrida(corrida1);
        repository.salvarCorrida(corrida2);

        assertEquals(2, repository.getCorridas().size());

        repository.remover(1);
        assertEquals(1, repository.getCorridas().size());
        assertFalse(repository.existeCorrida(1));
    }

    @AfterEach
    void cleanup() {
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}
