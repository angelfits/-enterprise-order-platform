package com.enterprise.user.controller;


import com.enterprise.user.dto.CreateUserRequest;
import com.enterprise.user.dto.LoginRequest;
import com.enterprise.user.dto.LoginResponse;
import com.enterprise.user.dto.UserDTO;
import com.enterprise.user.service.AuthService;
import com.enterprise.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


        private final AuthService authService;
        private final UserService userService;

        // POST /api/auth/log
        @PostMapping("/login")
        public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        }

        // POST /api/auth/register
        @PostMapping("/register")
        public ResponseEntity<UserDTO> register(@Valid @RequestBody CreateUserRequest request) {
            UserDTO user = userService.create(request);
            //indica que la peticion fue exitosa y se creo un recurso nuevo
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }

}
