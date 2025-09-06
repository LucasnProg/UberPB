package org.example.model.service;

import org.example.model.entity.Gerente;
import org.example.model.repository.GerenteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GerenteServiceTest {

    private GerenteService service;

    @BeforeEach
    void setUp() {
        GerenteRepository.limparTodos(); // limpa dados antes de cada teste
        service = new GerenteService();
    }

    @Test
    void testCriarGerente() {
        service.criar("Rafael", "rafael@email.com", "senha123", "12345678900", "11988888888");
        List<Gerente> gerentes = service.listar();

        assertEquals(1, gerentes.size());
        Gerente g = gerentes.get(0);
        assertEquals("Rafael", g.getNome());
        assertEquals("rafael@email.com", g.getEmail());
        assertEquals("senha123", g.getSenha());
        assertEquals("12345678900", g.getCpf());
        assertEquals("11988888888", g.getTelefone());
    }

    @Test
    void testGetGerente() {
        service.criar("Bianca", "bianca@email.com", "senha123", "98765432100", "11999999999");

        Gerente g = service.getGerente("98765432100");
        assertNotNull(g);
        assertEquals("Bianca", g.getNome());

        Gerente naoExiste = service.getGerente("00000000000");
        assertNull(naoExiste);
    }

    @Test
    void testDeletarGerente() {
        service.criar("Lucas", "lucas@email.com", "senha123", "11122233344", "11977777777");
        assertEquals(1, service.listar().size());

        service.deletar("11122233344");
        assertTrue(service.listar().isEmpty());
    }

    @Test
    void testLoginGerente() {
        service.criar("Ana", "ana@email.com", "senha123", "55566677788", "11966666666");

        // Login correto
        assertTrue(service.login("ana@email.com", "senha123"));

        // Login errado
        assertFalse(service.login("ana@email.com", "senhaErrada"));

        // Email não cadastrado
        assertFalse(service.login("outro@email.com", "senha123"));
    }
}
