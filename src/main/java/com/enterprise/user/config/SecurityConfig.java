package com.enterprise.user.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//dice a spring que esta clase contiene configuracion de la aplicacion
@Configuration
//activa spring security para aplicaciones web
@EnableAspectJAutoProxy
public class SecurityConfig {
    //bean se encarga de gaurdar el objeto que devuelva este metodo para utilizarlo cuando lo necesita
    @Bean
    //el tipo de objeto que se va a crear passwordEncoder
    public PasswordEncoder passwordEncoder() {
        //se crea un codificador de contraseñas bcrypt
        return new BCryptPasswordEncoder();
    }

    @Bean
    //securityfilterchain aca decidimos quien puede entrar a que rutas y bajo que condiciones
    //http security lo usaos para activar o desactivar seguridad permisos o reglas
    //throws Exception nos dice que este metodo puede lanzar un error pero no lo vamos a controlar aqui si no quien lo llame

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //desactivmaos la proteccion contra ataques en formularios web
                //se desactiva por que el app es una api rest
                //no usamos formulario html tradicional usamos jwt o tokens no csrf
                .csrf(AbstractHttpConfigurer::disable)
                //todas las rutas estan libees sin autenticacion
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().permitAll()
                );
            //¿Por qué?
            //H2 Console se abre dentro de un iframe
            //Sin esto → pantalla en blanco
        return http.build();
    }
}
