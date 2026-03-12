package org.example.model.repository;

import org.example.model.entity.Localizacao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LocalizacaoRepositoryTest {

    private LocalizacaoRepository localizacaoRepository;
    private File tempLocais;
    private File tempBairros;

    @BeforeEach
    void setUp() throws Exception {
        localizacaoRepository = new LocalizacaoRepository();
        tempLocais = File.createTempFile("locais_test_", ".json");
        tempBairros = File.createTempFile("bairros_test_", ".json");

        Field fieldLoc = LocalizacaoRepository.class.getDeclaredField("localizacaoDB");
        fieldLoc.setAccessible(true);
        fieldLoc.set(localizacaoRepository, new JsonRepository<>(tempLocais.getAbsolutePath(), Localizacao.class));

        Field fieldBairros = LocalizacaoRepository.class.getDeclaredField("bairrosDB");
        fieldBairros.setAccessible(true);
        fieldBairros.set(localizacaoRepository, new JsonRepository<>(tempBairros.getAbsolutePath(), Localizacao.class));
    }

    @AfterEach
    void tearDown() {
        if (tempLocais.exists()) tempLocais.delete();
        if (tempBairros.exists()) tempBairros.delete();
    }

    @Test
    void testAdicionarLocalizacao_EvitaDuplicatas() {
        Localizacao loc1 = new Localizacao(-7.0, -35.0);
        loc1.setDescricao("Centro");

        Localizacao loc2 = new Localizacao(-7.1, -35.1);
        loc2.setDescricao("Centro");

        localizacaoRepository.adicionar(loc1);
        List<Localizacao> salvos = localizacaoRepository.carregar();
        assertEquals(1, salvos.size());

        localizacaoRepository.adicionar(loc2);
        salvos = localizacaoRepository.carregar();
        assertEquals(1, salvos.size(), "Não deve adicionar localizações com a mesma descrição.");
    }

    @Test
    void testCarregarBairros_VazioInicialmente() {
        List<Localizacao> bairros = localizacaoRepository.carregarBairros();
        assertNotNull(bairros);
        assertTrue(bairros.isEmpty(), "A lista de bairros do arquivo de teste deve iniciar vazia.");
    }
}