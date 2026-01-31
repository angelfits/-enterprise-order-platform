package com.enterprise.user.config;


import com.enterprise.user.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//dice a spring que esta clase contiene configuracion de la aplicacion
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    //bean se encarga de gaurdar el objeto que devuelva este metodo para utilizarlo cuando lo necesita
    @Bean
    //el tipo de objeto que se va a crear passwordEncoder
    public PasswordEncoder passwordEncoder() {
        //se crea un codificador de contraseñas bcrypt
        return new BCryptPasswordEncoder();
    }

    @Bean
    //securityfilterchain aca decidimos quien puede entrar a que rutas y bajo que condiciones
    //http es un objeto que me permite configurar reglas de seguridad http
    //throws Exception nos dice que este metodo puede lanzar un error pero no lo vamos a controlar aqui si no quien lo llame

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //configuracion para proteccion contra CSRF
                //AbstractHttpConfigurer::disable → desactiva la protección CSRF. pq mi apop es un API REST usa tokens
                .csrf(AbstractHttpConfigurer::disable)
                //configuracion de como spring maneja las sesiones HTTP
                .sessionManagement(session  ->
                        //indica que la app no debe de guardar sessiones
                        //dentreo del parentesis - cada peticion debe contener su propio tokewn JWT
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //authorize.. define quioen puede acceder a que rutas
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas (sin token)
                        //requestMatchers aplica regloas de sxeguridad a las trutas
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        // Rutas protegidas (requieren token)
                        .requestMatchers("/api/users/**").authenticated()

                        //Todo lo demás requiere autenticación
                        .anyRequest().authenticated()
                )
        //permite configurear cabeceras HTTP de seguridad
        .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                //addFilterBefore agrega un filtro personalizado en la cadena de spring security
                //UsernamePasswor.. le dice a spring que antes que maneje autenticacion por usuario primero valida el JWT
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
