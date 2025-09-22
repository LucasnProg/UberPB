package org.example.model.service;

import org.example.model.entity.Passageiro;
import org.example.model.repository.PassageiroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PassageiroServiceTest {

    private PassageiroService passageiroService;
    private PassageiroRepository passageiroRepository;

    @BeforeEach
    void setUp() {
        // Cria o service e o repositório real
        passageiroService = new PassageiroService();
        passageiroRepository = new PassageiroRepository();

        // Limpa os dados do repositório antes de cada teste
        File arquivo = new File("src/main/resources/data/passageiros.json");
        if (arquivo.exists()) arquivo.delete();
    }

    @Test
    void testeCriarPassageiroSucesso() {
        Passageiro p = passageiroService.criar(
                "Maria Silva",
                "maria@email.com",
                "senha123",
                "12345678900",
                "11988888888"
        );

        assertNotNull(p, "Passageiro não deve ser nulo ao criar");
        assertEquals("Maria Silva", p.getNome());
        assertEquals("maria@email.com", p.getEmail());
        assertEquals("12345678900", p.getCpf());

        // Testa login
        Passageiro login = passageiroService.login("maria@email.com", "senha123");
        assertNotNull(login, "Login deve retornar passageiro existente");
        assertEquals(p.getId(), login.getId());
    }

    @Test
    void testeAtualizarPassageiro() {
        Passageiro p = passageiroService.criar(
                "Ester",
                "ester@email.com",
                "senha123",
                "44455566677",
                "11912345678"
        );

        p.setNome("Ester Silva");
        passageiroService.atualizar(p);

        Passageiro atualizado = passageiroService.buscarPorId(p.getId());
        assertNotNull(atualizado, "Passageiro atualizado deve existir");
        assertEquals("Ester Silva", atualizado.getNome());
    }

    @Test
    void testeCriarPassageiroEmailDuplicado() {
        passageiroService.criar("João", "joao@email.com", "senha123", "11122233344", "11977777777");
        Passageiro duplicado = passageiroService.criar("João II", "joao@email.com", "senha456", "55566677788", "11999999999");
        assertNull(duplicado, "Não deve permitir criar passageiro com email duplicado");
    }

    @Test
    void testeCriarPassageiroCpfDuplicado() {
        passageiroService.criar("Lucas", "lucas@email.com", "senha123", "22233344455", "11955555555");
        Passageiro duplicado = passageiroService.criar("Lucas II", "lucas2@email.com", "senha456", "22233344455", "11944444444");
        assertNull(duplicado, "Não deve permitir criar passageiro com CPF duplicado");
    }

    @Test
    void testeLoginFalha() {
        passageiroService.criar("Ana", "ana@email.com", "senha123", "33344455566", "11966666666");

        // Senha errada
        assertNull(passageiroService.login("ana@email.com", "senhaErrada"));

        // Email inexistente
        assertNull(passageiroService.login("naoexiste@email.com", "senha123"));
    }
}
