package org.example.model.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Entidade Usuario (Abstrata)")
class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("João Silva", "joao@gmail.com", "senha123", "111.222.333-44", "83999999999") {};
    }

    @Test
    @DisplayName("Deve inicializar a entidade com os valores corretos no construtor")
    void testeConstrutor() {
        assertAll("Verificando construtor",
                () -> assertEquals("João Silva", usuario.getNome()),
                () -> assertEquals("joao@gmail.com", usuario.getEmail()),
                () -> assertEquals("senha123", usuario.getSenha()),
                () -> assertEquals("111.222.333-44", usuario.getCpf()),
                () -> assertEquals("83999999999", usuario.getTelefone()),
                () -> assertNull(usuario.getAvaliacao(), "Avaliação deve iniciar como nula")
        );
    }

    @Test
    @DisplayName("Deve validar todos os Getters e Setters corretamente")
    void testeGettersESetters() {
        usuario.setId(10);
        usuario.setNome("Maria Silva");
        usuario.setEmail("maria@gmail.com");
        usuario.setSenha("novaSenha");
        usuario.setCpf("000.000.000-00");
        usuario.setTelefone("83888888888");
        usuario.setAvaliacao(4.8);

        assertAll("Verificando getters e setters",
                () -> assertEquals(10, usuario.getId()),
                () -> assertEquals("Maria Silva", usuario.getNome()),
                () -> assertEquals("maria@gmail.com", usuario.getEmail()),
                () -> assertEquals("novaSenha", usuario.getSenha()),
                () -> assertEquals("000.000.000-00", usuario.getCpf()),
                () -> assertEquals("83888888888", usuario.getTelefone()),
                () -> assertEquals(4.8, usuario.getAvaliacao())
        );
    }
}