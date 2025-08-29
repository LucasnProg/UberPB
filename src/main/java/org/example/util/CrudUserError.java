package org.example.util;

public class CrudUserError extends RuntimeException {
    public CrudUserError(String message) {
        super(message);
    }
}
