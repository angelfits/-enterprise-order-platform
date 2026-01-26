package com.enterprise.user.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            //representa la peticion http que llega al servidor contiene la cabesera, url, parametros cuerpo request
         HttpServletRequest request,
         //representa la respuesta http que se enviara al cliente
         HttpServletResponse response,
         //cadena de filtros de Spring security permite pasar la peticion al siguiente filtro
         FilterChain filterChain)
            //indica que el metodo puede lanzar excepciones: ServletException: error en el procesdamiento del servlet
            //IOException: error de entrada/salida
            throws ServletException, IOException{

         // 1. Obtener el header Authorization
         final String authHeader = request.getHeader("Authorization");

                // 2. Verificar si tiene el formato correcto: "Bearer token"
                //startsWith es un metodo de la clase String que verifica si un texto empiza con otro
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    //doFilter es un metodo de filterChain que pasar la petición HTTP al siguiente filtro
                    filterChain.doFilter(request, response);
                    return;
                }
                // 3. Extraer el token (quitar "Bearer ")
                //substrig devuelve una nueva cadena empesando del indice n
                final String token = authHeader.substring(7);

                try {
                    // 4. Extraer email y rol del token
                    final String email = jwtUtil.extractEmail(token);
                    final String role = jwtUtil.extractRole(token);

                    // 5. Si el email existe y no hay autenticación previa
                    //SecurityContextHolder: guarda informacion de seguridad sobre el usuario actual
                    //getContext():Devuelve el SecurityContext, es un objeto que contiene informacion de autenticacion del usuario
                    //getContext():devuelve un objeto authentication que representa al usuario que esta logueado
                    if(email!= null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        // 6. Validar el token
                        if (jwtUtil.validateToken(token, email)) {

                            // 7. Crear la autenticación
                            //List.of crea una lista inmutable
                            List<SimpleGrantedAuthority> authorities = List.of(
                                    //SimpleGrantedAuthority una clase de s. security que reprersenta un rol o autoridad de un usuario
                                    //"ROLE_" + role contruye elo nombnre del rol
                                    new SimpleGrantedAuthority("ROLE_" + role)
                            );


                            //Usernam... es una clase de S. security que representa un usuario autrenticado
                            UsernamePasswordAuthenticationToken authToken =
                                    //creramo un objewto de la clase.. (principal, contraseña, permiso del usuario);
                                    new UsernamePasswordAuthenticationToken(email, null, authorities);
                            //setDetails es un metodo de username... y sirve para agregar informacionm adicionasl sobre la autenticacion
                            //WebAuthent.. clase de S. security su funcion es tomar informacion de la peticion http
                            //buildDetails extrae informacion de la peticion http
                            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                            // 8. Guardar en el contexto de seguridad
                            SecurityContextHolder.getContext().setAuthentication(authToken);

                        }
                    }
                }catch (Exception e){
                    log.info("Error validando token JWT: {}", e.getMessage());

                }
                // 9. Continuar con la cadena de filtros
                filterChain.doFilter(request, response);
    }

}
