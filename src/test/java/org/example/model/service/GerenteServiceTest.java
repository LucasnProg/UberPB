package org.example.model.service;

import org.example.model.entity.Gerente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class GerenteServiceTest {

    private GerenteService service;

    @BeforeEach
    void setUp() {
        service = new GerenteService();
        File arquivo = new File("src/main/resources/data/gerentes.json");
        if (arquivo.exists()) arquivo.delete();
    }

    @Test
    void testeCriarGerenteELogin() {
        Gerente g = service.criar("Rafael", "rafael@email.com", "senha123", "12345678900", "11988888888");
        assertNotNull(g);

        Gerente login = service.login("rafael@email.com", "senha123");
        assertNotNull(login);
        assertEquals(g.getCpf(), login.getCpf());
    }

    @Test
    void testeCriarGerenteEmailDuplicado() {
        service.criar("Bianca", "bianca@email.com", "senha123", "98765432100", "11999999999");
        Gerente duplicado = service.criar("Bianca II", "bianca@email.com", "senha456", "55566677788", "11911111111");
        assertNull(duplicado);
    }
}
package org.example.model.service;

import org.example.model.entity.Gerente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GerenteServiceTest {

    private GerenteService service;

    @BeforeEach
    void setUp() {
        service = new GerenteService();
        service.limpar(); // limpa dados antigos antes de cada teste
    }

    @Test
    void testCriarGerente() {
        Gerente g = service.criar("Rafael", "rafael@email.com", "senha123", "12345678900", "11988888888");

        assertNotNull(g);
        assertEquals("Rafael", g.getNome());
        assertEquals("rafael@email.com", g.getEmail());
        assertEquals("senha123", g.getSenha());
        assertEquals("12345678900", g.getCpf());
        assertEquals("11988888888", g.getTelefone());
    }

    @Test
    void testLoginGerente() {
        Gerente g = service.criar("Ana", "ana@email.com", "senha123", "55566677788", "11966666666");

        // Login correto
        Gerente loginOk = service.login("ana@email.com", "senha123");
        assertNotNull(loginOk);
        assertEquals(g.getCpf(), loginOk.getCpf());

        // Senha errada
        Gerente loginErrado = service.login("ana@email.com", "senhaErrada");
        assertNull(loginErrado);

        // Email não cadastrado
        Gerente loginInvalido = service.login("outro@email.com", "senha123");
        assertNull(loginInvalido);
    }

    @Test
    void testCriarGerenteComEmailDuplicado() {
        Gerente g1 = service.criar("Lucas", "lucas@email.com", "senha123", "11122233344", "11977777777");
        assertNotNull(g1);

        Gerente g2 = service.criar("Outro", "lucas@email.com", "senha999", "99988877766", "11900000000");
        assertNull(g2); // não deve criar porque email já existe
    }
}
