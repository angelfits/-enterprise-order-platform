package com.enterprise.user.service;

import com.enterprise.user.dto.CreateUserRequest;
import com.enterprise.user.dto.UserDTO;
import com.enterprise.user.exception.EmailAlreadyExistsException;
import com.enterprise.user.exception.UserNotFoundException;
import com.enterprise.user.model.Role;
import com.enterprise.user.model.User;
import com.enterprise.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


//mockito es uina libreria para creasr objetos falsos que sirve para test unitarios
@ExtendWith(MockitoExtension.class) //Activa Mockito
class UserServiceTest {

    @Mock //crea un objerto falso de UserRepository
    private UserRepository userRepository;

    @Mock //crea un objeto falso  de PasswordEncoder
    private PasswordEncoder passwordEncoder;

    @InjectMocks    //Inyecta los mocks en USerService
    private UserService userService;

    private User user;
    private CreateUserRequest createUserRequest;

    @BeforeEach //se ejecuta antes d ecada test
    void setuP (){
        user = User.builder()
                .id(1L)
                .name("Angel")
                .email("angel@test.com")
                .password("encodedPassword")
                .role(Role.USER)
                .active(true)
                .build();
        createUserRequest = CreateUserRequest.builder()
                .name("Angel")
                .email("angel@test.com")
                .password("123456")
                .build();

    }


    // ========== TESTS PARA findAll() ==========
    @Test
    //es el nombre el cual le ponemos al meotod que sale en la consola
    @DisplayName("finAll - debe retornar lista de usuarios")
    void findAll_DebeRetornarListDeuUIsuarios(){
        //ARRANGE (Preparar)
        User user2 = User.builder()
                .id(2L)
                .name("Maria")
                .email("maria@gmail.com")
                .password("pass")
                .role(Role.USER)
                .active(true)
                .build();

        //cuando finAll sea llamado devuelve una lista
        when(userRepository.findAll()).thenReturn(Arrays.asList(user, user2));

        //ACT (ejecutar)
        List<UserDTO> resultado = userService.findAll();

        //ASSERT (verificar)
        //verifica que la lista tenga 2 elementos
        assertThat(resultado).hasSize(2);
        //verificamos que el primer usuario se llame Angel
        assertThat(resultado.get(0).getName()).isEqualTo("Angel");
        //verificamos que el segundo usuario se llame Maria
        assertThat(resultado.get(1).getName()).isEqualTo("Maria");

        //verifica que el metodo findAll del repositorio sea llamado solo una vez, para verificar que no hay llamadas duplicados
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll -  Debe retornar lista vacía cuando no hay usuarios")
    void indAll_DebeRetornarListaVacia() {
        //ARRANGE
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        //ACT
        List<UserDTO> resultado = userService.findAll();

        //ASSERT
        assertThat(resultado).isEmpty();
    }

    // ========== TESTS PARA findById() ==========
    @Test
    @DisplayName("findById - Debe retornar usuario cuando existe")
    void findById_DebeRetornarUsuario_CuandoExiste()
    {
        //ARRANGE
       when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        //ACT
        UserDTO resultado = userService.findById(1L);

        //ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getName()).isEqualTo("Angel");
        assertThat(resultado.getEmail()).isEqualTo("angel@test.com");

        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("findById - Debe lanzar excepción cuando no existe")
    void findById_DebeLanzarExcepcion_CuandoNoExiste(){
        //ARRANGE
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        //ACT & ASSDERT
        //verifica qwue se lance una exception
        assertThatThrownBy(() -> userService.findById(99L))
                //Verifica que lka excepcion sea de este tipo y no el generico
                .isInstanceOf(UserNotFoundException.class)
                //verifica que el mensjae de la excepcion contengta el id 99
                .hasMessageContaining("99");
    }

    // ========== TESTS PARA create() ==========
        @Test
    @DisplayName("create - Debe crear usuario correctamente")
     void create_DebeCrearUsuario_Correctamente() {

        //ARRANGE
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenReturn(user);
         //ACT
            UserDTO resultado = userService.create(createUserRequest);

          //ASSENT
            assertThat(resultado).isNotNull();
            assertThat(resultado.getName()).isEqualTo("Angel");
            assertThat(resultado.getEmail()).isEqualTo("angel@test.com");
            assertThat(resultado.getRole()).isEqualTo(Role.USER);

            verify(userRepository).existsByEmail("angel@test.com");
            verify(passwordEncoder).encode("123456");
            verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("create - Debe lanzar excepción cuando email ya existe")
    void  create_DebeLanzarExcepcion_CuandoEmailYaExiste() {

        //ARRANGE
            when(userRepository.existsByEmail("angel@test.com")).thenReturn(true);

         //ACT & ASSERT
            assertThatThrownBy(() -> userService.create(createUserRequest))
                    .isInstanceOf(EmailAlreadyExistsException.class)
                    .hasMessageContaining("angel@test.com");
         // Verificar que NUNCA se intentó guardar el usuario
            verify(userRepository, never()).save(any(User.class));
    }

    // ========== TESTS PARA delete() ==========
    @Test
    @DisplayName("delete - Debe eliminar usuario cuando existe")
    void delete_DebeEliminarUsuario_CuandoExiste(){

        //ARRANGE
        when(userRepository.existsById(1L)).thenReturn(true);
        //devuelve un void pq no hay nada que retornar
        doNothing().when(userRepository).deleteById(1L);

        //ACT
        userService.delete(1L);

        //ASSERT
        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    @DisplayName("delete - Debe lanzar excepción cuando no existe")
    void delete_DebeLanzarExcepcion_CuandoNoExiste(){
        //ARRANGE
        when(userRepository.existsById(99L)).thenReturn(false);

        //ACT &ASSENT
        assertThatThrownBy(() -> userService.delete(99L))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository, times(1)).existsById(99L);
        verify(userRepository, never()).deleteById(any());
    }
}
