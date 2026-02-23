package org.example.model.service;

import org.example.model.entity.Motorista;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class MotoristaServiceTest {

    private MotoristaService service;

    @BeforeEach
    void setUp() {
        service = new MotoristaService();
        File arquivo = new File("src/main/resources/data/motoristas.json");
        if (arquivo.exists()) arquivo.delete();
    }

    @Test
    void testeCriarMotoristaSucesso() {
        Motorista m = service.criar("Carlos", "carlos@email.com", "senha123", "12345678900", "11988888888");
        assertNotNull(m);
        assertEquals("Carlos", m.getNome());

        Motorista login = service.login("carlos@email.com", "senha123");
        assertNotNull(login);
        assertEquals(m.getCpf(), login.getCpf());
    }

    @Test
    void testeAtualizarMotorista() {
        Motorista m = service.criar("Lucas", "lucas@email.com", "senha123", "98765432100", "11999999999");
        Motorista buscado = service.buscarPorId(m.getId());
        assertNotNull(buscado);

        buscado.setNome("Lucas Silva");
        service.atualizar(buscado);

        Motorista atualizado = service.buscarPorId(buscado.getId());
        assertEquals("Lucas Silva", atualizado.getNome());
    }

    @Test
    void testeCriarMotoristaEmailDuplicado() {
        service.criar("João", "joao@email.com", "senha123", "11122233344", "11977777777");
        Motorista duplicado = service.criar("João II", "joao@email.com", "senha456", "55566677788", "11999999999");
        assertNull(duplicado);
    }

    @Test
    void testeLoginFalha() {
        service.criar("Ana", "ana@email.com", "senha123", "33344455566", "11966666666");

        assertNull(service.login("ana@email.com", "senhaErrada"));
        assertNull(service.login("naoexiste@email.com", "senha123"));
    }
}