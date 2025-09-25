package org.example.model.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    // Classe concreta fake só para permitir instanciar Usuario
    static class UsuarioFake extends Usuario {
        public UsuarioFake(String nome, String email, String senha, String cpf, String telefone) {
            super(nome, email, senha, cpf, telefone);
        }
    }

    @Test
    void deveCriarUsuarioComConstrutor() {
        Usuario usuario = new UsuarioFake("João", "joao@email.com", "1234", "11122233344", "999999999");

        assertEquals("João", usuario.getNome());
        assertEquals("joao@email.com", usuario.getEmail());
        assertEquals("1234", usuario.getSenha());
        assertEquals("11122233344", usuario.getCpf());
        assertEquals("999999999", usuario.getTelefone());
        assertEquals(0, usuario.getId()); // valor default de int
    }

    @Test
    void devePermitirAlterarValores() {
        Usuario usuario = new UsuarioFake("Ana", "ana@email.com", "abc", "55566677788", "888888888");

        usuario.setId(10);
        usuario.setNome("Maria");
        usuario.setEmail("maria@email.com");
        usuario.setSenha("novaSenha");
        usuario.setCpf("99988877766");
        usuario.setTelefone("777777777");

        assertEquals(10, usuario.getId());
        assertEquals("Maria", usuario.getNome());
        assertEquals("maria@email.com", usuario.getEmail());
        assertEquals("novaSenha", usuario.getSenha());
        assertEquals("99988877766", usuario.getCpf());
        assertEquals("777777777", usuario.getTelefone());
    }
}
