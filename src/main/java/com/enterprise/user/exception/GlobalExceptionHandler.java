package com.enterprise.user.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

//devuelve el json construido por @ExceptionHandler
@RestControllerAdvice
public class GlobalExceptionHandler {

    //devuelve un json dice que cuando ocurra un UserNot... ejecute este metodo
@ExceptionHandler(UserNotFoundException.class)
//la respuesta http del cuerpo sera dinmico y sera armado por un map
    public ResponseEntity<Map<String,Object>> handleUserNotFound(UserNotFoundException ex){
    //se crea un map para aramr el json
    Map<String,Object> error = new HashMap<>();
    error.put("timestamp", LocalDateTime.now());
    error.put("status", 404);
    error.put("error", "Not Found");
    error.put("message", ex.getMessage());

return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
}

@ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String,Object>> handleEmailExists(EmailAlreadyExistsException ex){
    Map<String,Object> error = new HashMap<>();
    error.put("timestamp", LocalDateTime.now());
    error.put("status", 409);
    error.put("error", "Email already exists");
    error.put("message", ex.getMessage());
   //CONFLICT conflictos caso duplicado o existencia de un recursos codigo 409
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
}

//le dice a spring que cunaod falle una validacion que ejecute este metodo
    //devuelve error por campos
@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleValidation(MethodArgumentNotValidException ex){
   Map<String,Object> error = new HashMap<>();
   error.put("timestamp", LocalDateTime.now());
   error.put("status", 400);
   error.put("error", "Bad Request");

   Map<String, String> fieldErrors = new HashMap<>();
   //getBindingResult contine el resultado de la validacion
    //getFieldErrors devuelve una lista de errores por campo
   ex.getBindingResult().getFieldErrors().forEach(fieldError ->
    //fieldError.getField nombre del campo (email, password, etc)
           //fieldError.getDefaultMessage mensaje definido en la validacion
           fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage()));
   error.put("errors", fieldErrors);
   return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
}

}
