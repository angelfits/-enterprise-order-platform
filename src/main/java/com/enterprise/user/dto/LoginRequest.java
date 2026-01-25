package com.enterprise.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.el.stream.StreamELResolverImpl;

//genera los getters and setters constructor rquewreido, etc de forma automatica
@Data
//genera constructoir vacio
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    //no puede ser null
    @NotBlank(message = "Email es obligatorio")
    //que tenga el formato de un email
    @Email(message = "El email no es válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
