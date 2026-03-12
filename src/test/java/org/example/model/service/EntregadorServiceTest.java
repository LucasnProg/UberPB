package org.example.model.service;

import org.example.model.entity.Entregador;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de Integração - EntregadorService")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EntregadorServiceTest {

    private EntregadorService entregadorService;
    private static String emailTeste;
    private static String cpfTeste;

    @BeforeEach
    void setUp() {
        entregadorService = new EntregadorService();
    }

    @BeforeAll
    static void setUpAll() {
        long timestamp = System.currentTimeMillis();
        emailTeste = "entregador_" + timestamp + "@gmail.com";
        cpfTeste = "555.444." + (timestamp % 1000) + "-10";
    }

    @Test
    @Order(1)
    @DisplayName("Deve cadastrar um novo Entregador com dados válidos")
    void testeCadastrarEntregador() {
        Entregador entregador = entregadorService.criar("Marcos Motoboy", emailTeste, "velocidade10", cpfTeste, "83912345678");
        assertNotNull(entregador);
        assertEquals(emailTeste, entregador.getEmail());
    }

    @Test
    @Order(2)
    @DisplayName("Deve realizar autenticação correta do Entregador")
    void testeLoginEntregador() {
        Entregador sucesso = entregadorService.login(emailTeste, "velocidade10");
        assertNotNull(sucesso);

        Entregador falha = entregadorService.login(emailTeste, "senhaErrada");
        assertNull(falha);
    }
}