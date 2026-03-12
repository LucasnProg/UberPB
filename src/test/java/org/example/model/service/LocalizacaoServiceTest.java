package org.example.model.service;

import org.example.model.entity.Localizacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de Integração - LocalizacaoService")
class LocalizacaoServiceTest {

    private LocalizacaoService localizacaoService;

    @BeforeEach
    void setUp() {
        localizacaoService = new LocalizacaoService();
    }

    @Test
    @DisplayName("Deve adicionar um novo local válido e recuperá-lo na lista")
    void testeAdicionarECarregarLocais() {
        String descricaoUnica = "Ponto Turístico " + System.currentTimeMillis();
        Localizacao novoLocal = new Localizacao(-7.22, -35.88);
        novoLocal.setDescricao(descricaoUnica);

        localizacaoService.adicionarNovoLocal(novoLocal);

        List<Localizacao> locais = localizacaoService.carregarLocais();
        assertNotNull(locais, "A lista de locais não deve ser nula");

        boolean encontrou = locais.stream().anyMatch(l -> descricaoUnica.equals(l.getDescricao()));
        assertTrue(encontrou, "O local recém-adicionado deve estar presente na lista carregada");
    }

    @Test
    @DisplayName("Não deve adicionar local com descrição vazia ou nula")
    void testeAdicionarLocalInvalido() {
        int totalAntes = localizacaoService.carregarLocais().size();

        Localizacao localInvalido = new Localizacao(0.0, 0.0);
        localInvalido.setDescricao("");
        localizacaoService.adicionarNovoLocal(localInvalido);

        int totalDepois = localizacaoService.carregarLocais().size();
        assertEquals(totalAntes, totalDepois, "O tamanho da lista não deve mudar se o local for inválido");
    }
}