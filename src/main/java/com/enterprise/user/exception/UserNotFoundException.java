package com.enterprise.user.exception;

//es el amnejom de errores de forma centralizada extendiendo runtimeexception
public class UserNotFoundException extends RuntimeException {

    public  UserNotFoundException(Long id){
        super("Usuario no encontrado con id: " + id);
    }

    public UserNotFoundException(String message){
        super(message);
    }
}
