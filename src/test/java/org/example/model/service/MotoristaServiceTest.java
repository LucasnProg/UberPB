package org.example.model.service;

import org.example.model.entity.Motorista;
import org.example.model.repository.MotoristaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MotoristaServiceTest {

    private MotoristaService service;

    @BeforeEach
    void setUp() {
        MotoristaRepository.limparTodos(); // limpa dados antes de cada teste
        service = new MotoristaService();
    }

    @Test
    void testCriarMotorista() {
        service.criar("Carlos", "carlos@email.com", "senha123", "12345678900", "11988888888");
        List<Motorista> motoristas = service.listar();

        assertEquals(1, motoristas.size());
        Motorista m = motoristas.get(0);
        assertEquals("Carlos", m.getNome());
        assertEquals("carlos@email.com", m.getEmail());
        assertEquals("senha123", m.getSenha());
        assertEquals("12345678900", m.getCpf());
        assertEquals("11988888888", m.getTelefone());
    }

    @Test
    void testGetMotorista() {
        service.criar("Paula", "paula@email.com", "senha123", "98765432100", "11999999999");

        Motorista m = service.getMotorista("98765432100");
        assertNotNull(m);
        assertEquals("Paula", m.getNome());

        Motorista naoExiste = service.getMotorista("00000000000");
        assertNull(naoExiste);
    }

    @Test
    void testDeletarMotorista() {
        service.criar("Lucas", "lucas@email.com", "senha123", "11122233344", "11977777777");
        assertEquals(1, service.listar().size());

        service.deletar("11122233344");
        assertTrue(service.listar().isEmpty());
    }

    @Test
    void testLoginMotorista() {
        service.criar("Ana", "ana@email.com", "senha123", "55566677788", "11966666666");

        // Login correto
        assertTrue(service.login("ana@email.com", "senha123"));

        // Login errado
        assertFalse(service.login("ana@email.com", "senhaErrada"));

        // Email não cadastrado
        assertFalse(service.login("outro@email.com", "senha123"));
    }
}
