package org.example.model.repository;

import org.example.model.entity.Motorista;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MotoristaRepositoryTest {

    private MotoristaRepository repo;

    @BeforeEach
    void setUp() {
        repo = new MotoristaRepository();
        repo.limpar(); // limpa todos os motoristas antes de cada teste
    }

    @Test
    void testSalvarEBuscarPorEmail() {
        Motorista m = new Motorista("Carlos", "carlos@email.com", "senha123", "12345678900", "11988888888");
        repo.salvar(m);

        Motorista encontrado = repo.buscarPorEmail("carlos@email.com");
        assertNotNull(encontrado);
        assertEquals("Carlos", encontrado.getNome());
        assertEquals(1, encontrado.getId());
    }

    @Test
    void testAtualizarMotorista() {
        Motorista m = new Motorista("Lucas", "lucas@email.com", "senha123", "98765432100", "11999999999");
        repo.salvar(m);

        m.setNome("Lucas Silva");
        repo.atualizar(m);

        Motorista atualizado = repo.buscarPorEmail("lucas@email.com");
        assertEquals("Lucas Silva", atualizado.getNome());
    }

    @Test
    void testGetMotoristas() {
        Motorista m1 = new Motorista("Marcos", "marcos@email.com", "senha123", "10101010101", "11910101010");
        Motorista m2 = new Motorista("Fernanda", "fernanda@email.com", "senha123", "20202020202", "11920202020");

        repo.salvar(m1);
        repo.salvar(m2);

        List<Motorista> motoristas = repo.getMotoristas();
        assertTrue(motoristas.stream().anyMatch(m -> m.getNome().equals("Marcos")));
        assertTrue(motoristas.stream().anyMatch(m -> m.getNome().equals("Fernanda")));
    }
}
