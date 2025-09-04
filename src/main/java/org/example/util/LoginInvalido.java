package org.example.util;

public class LoginInvalido extends RuntimeException {
    public LoginInvalido(String message) {
        super(message);
    }
}
