package org.example.model.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void testCriacaoUsuario() {
        Usuario usuario = new Usuario(
                "Lucas Silva",
                "lucas@email.com",
                "senha123",
                "12345678900",
                "11999999999"
        );

        // Verifica atributos atribuídos pelo construtor
        assertEquals("Lucas Silva", usuario.getNome());
        assertEquals("lucas@email.com", usuario.getEmail());
        assertEquals("senha123", usuario.getSenha());
        assertEquals("12345678900", usuario.getCpf());
        assertEquals("11999999999", usuario.getTelefone());
        assertEquals(0, usuario.getId()); // id ainda não foi setado
    }

    @Test
    void testSettersAndGetters() {
        Usuario usuario = new Usuario("Nome", "email", "senha", "cpf", "telefone");

        usuario.setNome("João");
        usuario.setEmail("joao@email.com");
        usuario.setSenha("novaSenha");
        usuario.setCpf("98765432100");
        usuario.setTelefone("11988888888");
        usuario.setId(10);

        assertEquals("João", usuario.getNome());
        assertEquals("joao@email.com", usuario.getEmail());
        assertEquals("novaSenha", usuario.getSenha());
        assertEquals("98765432100", usuario.getCpf());
        assertEquals("11988888888", usuario.getTelefone());
        assertEquals(10, usuario.getId());
    }
}
