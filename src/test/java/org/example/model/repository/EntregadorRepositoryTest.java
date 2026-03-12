package org.example.model.repository;

import org.example.model.entity.Entregador;
import org.example.model.entity.EntregadorStatus;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de Integração - EntregadorRepository")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EntregadorRepositoryTest {

    private EntregadorRepository entregadorRepository;
    private static String emailTeste;
    private static String cpfTeste;
    private static Entregador entregadorSalvo;

    @BeforeEach
    void setUp() {
        entregadorRepository = new EntregadorRepository();
    }

    @BeforeAll
    static void setUpAll() {
        long timestamp = System.currentTimeMillis();
        emailTeste = "repo_entregador_" + timestamp + "@delivery.com";
        cpfTeste = "222.333." + (timestamp % 1000) + "-44";
    }

    @Test
    @Order(1)
    @DisplayName("Deve salvar um novo Entregador gerando ID sequencial")
    void testeSalvarEntregador() {
        Entregador entregador = new Entregador("Entregador Veloz", emailTeste, "senha123", cpfTeste, "83900000000");
        entregadorRepository.salvar(entregador);

        Entregador buscado = entregadorRepository.buscarPorEmail(emailTeste);
        assertNotNull(buscado, "O entregador deveria ter sido salvo no arquivo JSON");
        assertTrue(buscado.getId() > 0, "O ID deve ter sido gerado automaticamente");

        entregadorSalvo = buscado;
    }

    @Test
    @Order(2)
    @DisplayName("Deve buscar o Entregador por ID, CPF e E-mail")
    void testeMetodosDeBusca() {
        assertNotNull(entregadorSalvo);

        Entregador porId = entregadorRepository.buscarPorId(entregadorSalvo.getId());
        assertEquals(emailTeste, porId.getEmail());

        Entregador porCpf = entregadorRepository.buscarPorCpf(cpfTeste);
        assertNotNull(porCpf);
        assertEquals(entregadorSalvo.getId(), porCpf.getId());
    }

    @Test
    @Order(3)
    @DisplayName("Deve atualizar os dados do Entregador no arquivo JSON")
    void testeAtualizarEntregador() {
        entregadorSalvo.setStatus(EntregadorStatus.OCUPADO);

        entregadorRepository.atualizar(entregadorSalvo);

        Entregador atualizado = entregadorRepository.buscarPorId(entregadorSalvo.getId());
        assertEquals(EntregadorStatus.OCUPADO, atualizado.getStatus(), "O status atualizado deve persistir");
    }
}