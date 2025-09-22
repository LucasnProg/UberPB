package org.example.model.service;

import org.example.model.entity.Motorista;
import org.example.model.repository.MotoristaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MotoristaServiceTest {

    private MotoristaService service;

    @BeforeEach
    void setUp() {
        // não existe limparTodos, então apenas cria nova instância
        service = new MotoristaService();
    }

    @Test
    void testCriarMotorista() {
        Motorista m = service.criar("Carlos", "carlos@email.com", "senha123", "12345678900", "11988888888");

        assertNotNull(m);
        assertEquals("Carlos", m.getNome());
        assertEquals("carlos@email.com", m.getEmail());
        assertEquals("senha123", m.getSenha());
        assertEquals("12345678900", m.getCpf());
        assertEquals("11988888888", m.getTelefone());
    }

    @Test
    void testLoginMotorista() {
        Motorista m = service.criar("Ana", "ana@email.com", "senha123", "55566677788", "11966666666");

        // Login correto
        Motorista loginOk = service.login("ana@email.com", "senha123");
        assertNotNull(loginOk);
        assertEquals(m.getCpf(), loginOk.getCpf());

        // Senha errada
        Motorista loginErrado = service.login("ana@email.com", "senhaErrada");
        assertNull(loginErrado);

        // Email não cadastrado
        Motorista loginInvalido = service.login("outro@email.com", "senha123");
        assertNull(loginInvalido);
    }

    @Test
    void testCriarMotoristaComEmailDuplicado() {
        Motorista m1 = service.criar("Lucas", "lucas@email.com", "senha123", "11122233344", "11977777777");
        assertNotNull(m1);

        Motorista m2 = service.criar("Outro", "lucas@email.com", "senha999", "99988877766", "11900000000");
        assertNull(m2); // não deve criar porque email já existe
    }
}
