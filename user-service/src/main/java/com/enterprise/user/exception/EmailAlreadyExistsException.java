package com.enterprise.user.exception;

public class EmailAlreadyExistsException extends RuntimeException{

    public EmailAlreadyExistsException(String email) {
        super("El email ya existe: " + email);
    }
}
