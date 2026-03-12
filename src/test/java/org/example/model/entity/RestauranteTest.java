package org.example.model.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Entidade Restaurante")
class RestauranteTest {

    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante("Pizzaria do Bairro", "contato@gmail.com", "senhaForte", "12.345.678/0001-90", "83944444444", "Pizzaria", new Localizacao(-7.2222, -35.8888));
    }

    @Test
    @DisplayName("Deve salvar e retornar o CNPJ usando a estrutura base de CPF da superclasse")
    void testeWrapperCnpjCpf() {
        assertAll("Validação CNPJ/CPF",
                () -> assertEquals("12.345.678/0001-90", restaurante.getCnpj(), "Deve retornar o CNPJ corretamente"),
                () -> assertEquals("12.345.678/0001-90", restaurante.getCpf(), "Internamente, o valor do CPF também deve refletir o CNPJ")
        );

        restaurante.setCnpj("98.765.432/0001-10");

        assertAll("Atualização CNPJ/CPF",
                () -> assertEquals("98.765.432/0001-10", restaurante.getCnpj()),
                () -> assertEquals("98.765.432/0001-10", restaurante.getCpf())
        );
    }

    @Test
    @DisplayName("Deve validar os atributos próprios do Restaurante")
    void testeGettersESetters() {
        Localizacao local = new Localizacao(-7.00, -35.00);

        restaurante.setCategoria("Pizzaria");
        restaurante.setEndereco(local);

        assertAll("Atributos do Restaurante",
                () -> assertEquals("Pizzaria", restaurante.getCategoria()),
                () -> assertEquals(local, restaurante.getEndereco())
        );
    }
}