package org.example.model.service;

import org.example.model.entity.Passageiro;
import org.example.model.repository.PassageiroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PassageiroServiceTest {
/*
    private PassageiroService service;

    @BeforeEach
    void setUp() {
        PassageiroRepository.limparTodos();
        service = new PassageiroService();
    }

    @Test
    void testCriarPassageiro() {
        service.criar("Maria Silva", "maria@email.com", "senha123", "12345678900", "11988888888");
        List<Passageiro> passageiros = service.listar();

        assertEquals(1, passageiros.size());
        Passageiro p = passageiros.get(0);
        assertEquals("Maria Silva", p.getNome());
        assertEquals("maria@email.com", p.getEmail());
        assertEquals("senha123", p.getSenha());
        assertEquals("12345678900", p.getCpf());
        assertEquals("11988888888", p.getTelefone());
    }

    @Test
    void testGetPassageiro() {
        service.criar("João", "joao@email.com", "senha123", "98765432100", "11999999999");

        Passageiro p = service.getPassageiro("98765432100");
        assertNotNull(p);
        assertEquals("João", p.getNome());

        Passageiro naoExiste = service.getPassageiro("00000000000");
        assertNull(naoExiste);
    }

    @Test
    void testDeletarPassageiro() {
        service.criar("Lucas", "lucas@email.com", "senha123", "11122233344", "11977777777");
        assertEquals(1, service.listar().size());

        service.deletar("11122233344");
        assertTrue(service.listar().isEmpty());
    }

    @Test
    void testLoginPassageiro() {
        service.criar("Ana", "ana@email.com", "senha123", "55566677788", "11966666666");

        // Login correto
        assertTrue(service.login("ana@email.com", "senha123"));
        assertNotNull(service.getPassageiroLogado());

        // Login errado
        assertFalse(service.login("ana@email.com", "senhaErrada"));

        // Email não cadastrado
        assertFalse(service.login("outro@email.com", "senha123"));
    }*/
}
