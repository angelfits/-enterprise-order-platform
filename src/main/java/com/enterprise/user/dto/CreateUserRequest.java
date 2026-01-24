package com.enterprise.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//DTO de entrada valida y filtra lo que el usuario envia y no expone campos sensibles
public class CreateUserRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message =  "El email es obligatorio")
    @Email(message = "El email no es válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min =6, message = "La contraseña debe tener al menos 6 caracteres" )
    private String password;
}
