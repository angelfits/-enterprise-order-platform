package com.enterprise.user.service;


import com.enterprise.user.dto.CreateUserRequest;
import com.enterprise.user.dto.UserDTO;
import com.enterprise.user.exception.EmailAlreadyExistsException;
import com.enterprise.user.exception.UserNotFoundException;
import com.enterprise.user.model.Role;
import com.enterprise.user.model.User;
import com.enterprise.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// marca la clase como marca de servicio se usa donde va la logica de negocio
@Service
//Lombook crea constructor con los atributos "final"
@RequiredArgsConstructor
//Lombok crea logger
@Slf4j
//spring maneja transacciones, si falla algo se revierte todo
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //obtener todos los usuarios
    public List<UserDTO> findAll(){
        log.info("Buscando todos los usuarios");
        return userRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private UserDTO convertToDTO(User user){
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .build();
    }

    //obtener usuario por ID
    public UserDTO findById(Long id){
        log.info("Buscando el usuario {}", id);
        return userRepository.findById(id)
                .map(this::convertToDTO)
                //si el Optional esta vacio lanza una exception
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    //crear nuevo usuario
    public UserDTO create(CreateUserRequest request){
        log.info("Creandousuario con email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw  new EmailAlreadyExistsException(request.getEmail());
        }
        //crea la entidad user para poder guardarlo en la base de datos
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(Role.USER)
                .active(true)
                .build();
    User savedUser = userRepository.save(user);
        log.info("Creandousuario con email: {}", savedUser.getEmail());
    //convierte la entidad en un DTO
    return convertToDTO(savedUser);
    }

    //actualizar usuario
    public UserDTO update(Long id, CreateUserRequest request) {
        log.info("Actualizando el usuario {}", id);

        User user = userRepository.findById(id)
                .orElseThrow( () -> new UserNotFoundException(id));
        //verificar si el email ya existe en otro suuaario
        if (!user.getEmail().equals(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        return convertToDTO(userRepository.save(user));
    }

    //Eliminar Usuario
    public void delete(Long id){
        log.info("Eliminando el usuario {}", id);
        if(!userRepository.existsById(id)) {
          throw new UserNotFoundException(id);
        }

        userRepository.deleteById(id);
        log.info("eliminando el usuario {}", id);
    }
}
