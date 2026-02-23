package org.example.model.service;

import org.example.model.entity.Entregador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class EntregadorServiceTest {

    private EntregadorService service;

    @BeforeEach
    void setUp() {
        service = new EntregadorService();
        File arquivo = new File("src/main/resources/data/entregadores.json");
        if (arquivo.exists()) arquivo.delete();
    }

    @Test
    void testeCriarEntregadorSucesso() {
        Entregador e = service.criar("Pedro", "pedro@email.com", "senha123", "11122233344", "11977777777");
        assertNotNull(e);
        assertEquals("Pedro", e.getNome());

        Entregador login = service.login("pedro@email.com", "senha123");
        assertNotNull(login);
        assertEquals(e.getCpf(), login.getCpf());
    }

    @Test
    void testeAtualizarEntregador() {
        Entregador e = service.criar("Mariana", "mariana@email.com", "senha123", "22233344455", "11955555555");
        Entregador buscado = service.buscarPorId(e.getId());
        assertNotNull(buscado);

        buscado.setNome("Mariana Silva");
        service.atualizar(buscado);

        Entregador atualizado = service.buscarPorId(buscado.getId());
        assertEquals("Mariana Silva", atualizado.getNome());
    }

    @Test
    void testeCriarEntregadorDuplicado() {
        service.criar("Felipe", "felipe@email.com", "senha123", "33344455566", "11966666666");
        Entregador dupEmail = service.criar("Felipe II", "felipe@email.com", "senha456", "44455566677", "11900000000");
        assertNull(dupEmail);

        // duplicar por CPF
        Entregador dupCpf = service.criar("Felipe III", "felipe3@email.com", "senha789", "33344455566", "11912312312");
        assertNull(dupCpf);
    }
}
