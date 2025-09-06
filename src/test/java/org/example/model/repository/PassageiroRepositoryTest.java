package org.example.model.repository;

import org.example.model.entity.Passageiro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PassageiroRepositoryTest {

    private PassageiroRepository repo;

    @BeforeEach
    void setUp() {
        // Limpa os passageiros antes de cada teste
        PassageiroRepository.limparTodos();
        repo = new PassageiroRepository();
    }

    @Test
    void testSalvarEPesquisarPorCpf() {
        Passageiro p = new Passageiro("Maria Silva", "maria@email.com", "senha123", "12345678900", "11988888888");
        repo.salvarPassageiro(p);

        Passageiro encontrado = repo.buscarPorCpf("12345678900");
        assertNotNull(encontrado);
        assertEquals("Maria Silva", encontrado.getNome());
        assertEquals(1, encontrado.getId());
    }

    @Test
    void testBuscarPorEmail() {
        Passageiro p = new Passageiro("João", "joao@email.com", "senha123", "98765432100", "11999999999");
        repo.salvarPassageiro(p);

        Passageiro encontrado = repo.buscarPorEmail("joao@email.com");
        assertNotNull(encontrado);
        assertEquals("João", encontrado.getNome());
    }

    @Test
    void testRemoverPassageiro() {
        Passageiro p = new Passageiro("Lucas", "lucas@email.com", "senha123", "11122233344", "11977777777");
        repo.salvarPassageiro(p);

        repo.remover("11122233344");
        Passageiro removido = repo.buscarPorCpf("11122233344");
        assertNull(removido);
    }

    @Test
    void testExisteCpf() {
        Passageiro p = new Passageiro("Ana", "ana@email.com", "senha123", "55566677788", "11966666666");
        repo.salvarPassageiro(p);

        assertTrue(repo.existeCpf("55566677788"));
        assertFalse(repo.existeCpf("00000000000"));
    }

    @Test
    void testRealizarLogin() {
        Passageiro p = new Passageiro("Carlos", "carlos@email.com", "senha123", "22233344455", "11955555555");
        repo.salvarPassageiro(p);

        assertTrue(repo.realizarLogin("carlos@email.com", "senha123"));
        assertFalse(repo.realizarLogin("carlos@email.com", "senhaErrada"));
        assertFalse(repo.realizarLogin("outro@email.com", "senha123"));
    }

    @Test
    void testGetIdByCpf() {
        Passageiro p = new Passageiro("Paula", "paula@email.com", "senha123", "66677788899", "11944444444");
        repo.salvarPassageiro(p);

        int id = PassageiroRepository.getIdByCpf("66677788899");
        assertEquals(1, id);

        int naoExiste = PassageiroRepository.getIdByCpf("00000000000");
        assertEquals(0, naoExiste);
    }

    @Test
    void testGetPassageiros() {
        Passageiro p1 = new Passageiro("Joana", "joana@email.com", "senha123", "10101010101", "11910101010");
        Passageiro p2 = new Passageiro("Pedro", "pedro@email.com", "senha123", "20202020202", "11920202020");

        repo.salvarPassageiro(p1);
        repo.salvarPassageiro(p2);

        List<Passageiro> passageiros = repo.getPassageiros();
        assertEquals(2, passageiros.size());
        assertEquals("Joana", passageiros.get(0).getNome());
        assertEquals("Pedro", passageiros.get(1).getNome());
    }
}
