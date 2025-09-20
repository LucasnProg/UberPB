package org.example.model.repository;

import org.example.model.entity.Motorista;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MotoristaRepositoryTest {
/*
    private MotoristaRepository repo;

    @BeforeEach
    void setUp() {
        MotoristaRepository.limparTodos();
        repo = new MotoristaRepository();
    }

    @Test
    void testSalvarEMotoristaPorCpf() {
        Motorista m = new Motorista("Carlos", "carlos@email.com", "senha123", "12345678900", "11988888888");
        repo.salvarMotorista(m);

        Motorista encontrado = repo.buscarPorCpf("12345678900");
        assertNotNull(encontrado);
        assertEquals("Carlos", encontrado.getNome());
        assertEquals(1, encontrado.getId());
    }

    @Test
    void testBuscarPorEmail() {
        Motorista m = new Motorista("Lucas", "lucas@email.com", "senha123", "98765432100", "11999999999");
        repo.salvarMotorista(m);

        Motorista encontrado = repo.buscarPorEmail("lucas@email.com");
        assertNotNull(encontrado);
        assertEquals("Lucas", encontrado.getNome());
    }

    @Test
    void testRemoverMotorista() {
        Motorista m = new Motorista("Paula", "paula@email.com", "senha123", "11122233344", "11977777777");
        repo.salvarMotorista(m);

        repo.remover("11122233344");
        Motorista removido = repo.buscarPorCpf("11122233344");
        assertNull(removido);
    }

    @Test
    void testExisteCpf() {
        Motorista m = new Motorista("Ana", "ana@email.com", "senha123", "55566677788", "11966666666");
        repo.salvarMotorista(m);

        assertTrue(repo.existeCpf("55566677788"));
        assertFalse(repo.existeCpf("00000000000"));
    }

    @Test
    void testRealizarLogin() {
        Motorista m = new Motorista("Pedro", "pedro@email.com", "senha123", "22233344455", "11955555555");
        repo.salvarMotorista(m);

        assertTrue(repo.realizarLogin("pedro@email.com", "senha123"));
        assertFalse(repo.realizarLogin("pedro@email.com", "senhaErrada"));
        assertFalse(repo.realizarLogin("outro@email.com", "senha123"));
    }

    @Test
    void testGetIdByCpf() {
        Motorista m = new Motorista("Joana", "joana@email.com", "senha123", "66677788899", "11944444444");
        repo.salvarMotorista(m);

        int id = MotoristaRepository.getIdByCpf("66677788899");
        assertEquals(1, id);

        int naoExiste = MotoristaRepository.getIdByCpf("00000000000");
        assertEquals(0, naoExiste);
    }

    @Test
    void testGetMotoristas() {
        Motorista m1 = new Motorista("Marcos", "marcos@email.com", "senha123", "10101010101", "11910101010");
        Motorista m2 = new Motorista("Fernanda", "fernanda@email.com", "senha123", "20202020202", "11920202020");

        repo.salvarMotorista(m1);
        repo.salvarMotorista(m2);

        List<Motorista> motoristas = repo.getMotoristas();
        assertEquals(2, motoristas.size());
        assertEquals("Marcos", motoristas.get(0).getNome());
        assertEquals("Fernanda", motoristas.get(1).getNome());
    }*/
}
