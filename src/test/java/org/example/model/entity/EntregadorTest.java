package org.example.model.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Entidade Entregador")
class EntregadorTest {

    private Entregador entregador;

    @BeforeEach
    void setUp() {
        entregador = new Entregador("Lucas Entregador", "lucas@gmail.com", "senha123", "123.456.789-10", "83911111111");
    }

    @Test
    @DisplayName("Deve inicializar com status DISPONIVEL e atributos da superclasse corretos")
    void testeConstrutor() {
        assertAll("Validação do construtor",
                () -> assertEquals("Lucas Entregador", entregador.getNome()),
                () -> assertEquals("lucas@gmail.com", entregador.getEmail()),
                () -> assertEquals("123.456.789-10", entregador.getCpf()),
                () -> assertEquals(EntregadorStatus.DISPONIVEL, entregador.getStatus(), "O status inicial deve ser DISPONIVEL")
        );
    }

    @Test
    @DisplayName("Deve validar todos os Getters e Setters específicos do Entregador")
    void testeGettersESetters() {
        Localizacao novaLocalizacao = new Localizacao(-7.22, -35.88);

        entregador.setLocalizacaoAtual(novaLocalizacao);
        entregador.setStatus(EntregadorStatus.OCUPADO);

        assertAll("Validação de Getters e Setters",
                () -> assertEquals(novaLocalizacao, entregador.getLocalizacaoAtual()),
                () -> assertEquals(EntregadorStatus.OCUPADO, entregador.getStatus())
        );
    }
}