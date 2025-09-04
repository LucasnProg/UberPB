package org.example.util;

public class UsuarioNaoCadastrado extends RuntimeException {
    public UsuarioNaoCadastrado(String message) {
        super(message);
    }
}
