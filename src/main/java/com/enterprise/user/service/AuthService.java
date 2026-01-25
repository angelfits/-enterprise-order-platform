package com.enterprise.user.service;


import com.enterprise.user.dto.LoginRequest;
import com.enterprise.user.dto.LoginResponse;
import com.enterprise.user.model.User;
import com.enterprise.user.repository.UserRepository;
import com.enterprise.user.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponse login (LoginRequest request) {
        log.info("Intento de login para: {}", request.getEmail());

        // Buscar usuario por email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales invalidas"));

        // Verificar contraseña
        //match sirve para comparar contraseñas ´lanas con una contraseña encriptada
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            log.warn("Contraseña incorrecta para: {}", request.getEmail());
            //detiene la ejecucion y lanza el error
            throw new RuntimeException("Credenciales invalidad");
        }

        //verificar si el usuario esta activo
        if (!user.getActive()){
            log.warn("Usuario inactivo: {}", request.getEmail());
            throw new RuntimeException("Usuario inactivo");
        }

        // Generar token JWT
        //el name es un metodo propio de enum convierete el enum en string, el enum es el tipo que esta definido role
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        log.info("Login exitoso para: {}", request.getEmail());

        return  LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .message("Login exitoso")
                .build();
    }

}
