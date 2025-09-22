package org.example.model.repository;

import org.example.model.entity.Gerente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GerenteRepositoryTest {

    private GerenteRepository repo;

    @BeforeEach
    void setUp() {
        repo = new GerenteRepository();
        repo.limpar(); // Limpa todos os gerentes antes de cada teste
    }

    @Test
    void testSalvarEBuscarPorCpf() {
        Gerente g = new Gerente("Rafael", "rafael@email.com", "senha123",
                "12345678900", "11988888888");

        repo.salvar(g);

        Gerente encontrado = repo.buscarPorCpf("12345678900");
        assertNotNull(encontrado, "Gerente não encontrado pelo CPF");
        assertEquals("Rafael", encontrado.getNome());
        assertEquals("12345678900", encontrado.getCpf());
    }

    @Test
    void testSalvarEBuscarPorEmail() {
        Gerente g = new Gerente("Bianca", "bianca@email.com", "senha123",
                "98765432100", "11999999999");

        repo.salvar(g);

        Gerente encontrado = repo.buscarPorEmail("bianca@email.com");
        assertNotNull(encontrado, "Gerente não encontrado pelo email");
        assertEquals("Bianca", encontrado.getNome());
        assertEquals("bianca@email.com", encontrado.getEmail());
    }
}
