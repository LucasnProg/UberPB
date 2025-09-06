package org.example.model.repository;

import org.example.model.entity.Gerente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GerenteRepositoryTest {

    private GerenteRepository repo;

    @BeforeEach
    void setUp() {
        GerenteRepository.limparTodos();
        repo = new GerenteRepository();
    }

    @Test
    void testSalvarEGerentePorCpf() {
        Gerente g = new Gerente("Rafael", "rafael@email.com", "senha123", "12345678900", "11988888888");
        repo.salvarGerente(g);

        Gerente encontrado = repo.buscarPorCpf("12345678900");
        assertNotNull(encontrado);
        assertEquals("Rafael", encontrado.getNome());
        assertEquals(1, encontrado.getId());
    }

    @Test
    void testBuscarPorEmail() {
        Gerente g = new Gerente("Bianca", "bianca@email.com", "senha123", "98765432100", "11999999999");
        repo.salvarGerente(g);

        Gerente encontrado = repo.buscarPorEmail("bianca@email.com");
        assertNotNull(encontrado);
        assertEquals("Bianca", encontrado.getNome());
    }

    @Test
    void testRemoverGerente() {
        Gerente g = new Gerente("Lucas", "lucas@email.com", "senha123", "11122233344", "11977777777");
        repo.salvarGerente(g);

        repo.remover("11122233344");
        Gerente removido = repo.buscarPorCpf("11122233344");
        assertNull(removido);
    }

    @Test
    void testExisteCpf() {
        Gerente g = new Gerente("Ana", "ana@email.com", "senha123", "55566677788", "11966666666");
        repo.salvarGerente(g);

        assertTrue(repo.existeCpf("55566677788"));
        assertFalse(repo.existeCpf("00000000000"));
    }

    @Test
    void testRealizarLogin() {
        Gerente g = new Gerente("Pedro", "pedro@email.com", "senha123", "22233344455", "11955555555");
        repo.salvarGerente(g);

        assertTrue(repo.realizarLogin("pedro@email.com", "senha123"));
        assertFalse(repo.realizarLogin("pedro@email.com", "senhaErrada"));
        assertFalse(repo.realizarLogin("outro@email.com", "senha123"));
    }

    @Test
    void testGetIdByCpf() {
        Gerente g = new Gerente("Joana", "joana@email.com", "senha123", "66677788899", "11944444444");
        repo.salvarGerente(g);

        int id = GerenteRepository.getIdByCpf("66677788899");
        assertEquals(1, id);

        int naoExiste = GerenteRepository.getIdByCpf("00000000000");
        assertEquals(0, naoExiste);
    }

    @Test
    void testGetGerentes() {
        Gerente g1 = new Gerente("Marcos", "marcos@email.com", "senha123", "10101010101", "11910101010");
        Gerente g2 = new Gerente("Fernanda", "fernanda@email.com", "senha123", "20202020202", "11920202020");

        repo.salvarGerente(g1);
        repo.salvarGerente(g2);

        List<Gerente> gerentes = repo.getGerentes();
        assertEquals(2, gerentes.size());
        assertEquals("Marcos", gerentes.get(0).getNome());
        assertEquals("Fernanda", gerentes.get(1).getNome());
    }
}
