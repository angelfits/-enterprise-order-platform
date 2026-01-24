package com.enterprise.user.controller;


import com.enterprise.user.dto.CreateUserRequest;
import com.enterprise.user.dto.UserDTO;
import com.enterprise.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//indica que la clase maneja solicitusdes http y convierte los objetos devueltos en json
@RestController
//definimos la ruta abse para todos los metodos del controlador
@RequestMapping("/api/users")
//lombok generael controlador con todos los campos final
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //GET /api/users - Obtener todos los usuarios
    @GetMapping
    //ResponseEntity es una clase de spring que representa la respuesta http
   //este metodo obtinee desde el servicio todos los usarios en una lista de UserDTO
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        // devuelve una respuesta HTTP 200
        return ResponseEntity.ok(userService.findAll());
    }

    // GET /api/users/{id} - Obtener usuario por ID
    @GetMapping("/{id}")
    //la anotacion pathvariable le dice a spring que tome el id de la url y que lo guarde en
    // esta variable id
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    //requestbody convierte el json que envia el front en un objeto java
    //activa las validaciones de RequestBody
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserRequest request){
        UserDTO createdUser = userService.create(request);
        //HttpStatus.CREATED devuelve 20 y se usa cuando se crea correctamente un recurso
        //body es el cuerpo de la respuesta http psea dentro de responseemtity
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // PUT /api/users/{id} - Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody CreateUserRequest request){
        return ResponseEntity.ok(userService.update(id, request));
    }

    @DeleteMapping("/{id}")
    //void significa que no hay cuerpo
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.delete(id);
        //noceontetn define elstatus 204 no contenido
        //build dice que no hay cuerpo
        return ResponseEntity.noContent().build();
    }


}
