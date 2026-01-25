package com.enterprise.user.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

// es genericda indica que spring debe de gestionarla
@Component
public class JwtUtil {

    //sirve para inyectar valores de configuracion externo dentro de la clase
    @Value("${jwt.secret:miClaveSecretaSuperSeguraQueDebeTenerAlMenos256BitsParaHS256}")
    private String secret;

    @Value("${jwt.expiration:86400000}") // 24 horas en milisegundos
    private Long expiration;

    // Genera la clave secreta para firmar el token
    //secretkey representa una clave criptografica
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Genera un token JWT para un usuario
    public String generateToken(String email, String role){

        //los claims son datos adicionales que se guardan dentro del JWT
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .claims(claims)
                //se agrega a quien pertenece el token
                .subject(email)
                //fecha en la cual el token fue creado
                .issuedAt(new Date())
                //System.currentTimeMillis() devuelve el tiempo actual
                //Date crea la fecha de expiracion del token
                .expiration(new Date(System.currentTimeMillis() + expiration))
                //usa una clave criptografica que el metodo getSigningKey genera
                .signWith(getSigningKey())
                .compact();
    }

    // Extrae el email del token
    public String extractEmail(String token){
        return extractClaim(token, Claims::getSubject);
    }

    // Extrae el rol del token
    public  String extractRole(String token){
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    // Extrae la fecha de expiración
    public Date extractExpiration( String token){
        return extractClaim(token, Claims::getExpiration);
    }
    //Function es un interfaz de java que recibe un objeto claims y dfevuelve un tipo T
    //claimsResolver esw el nombre de lña variable donde se guarda una funcion
    public <T> T  extractClaim (String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractClaims(token);
        //se ejecuta la funcion claimsResolver
        return claimsResolver.apply(claims);
    }

    private Claims extractClaims (String token){
            //el parser se encarega de leer y validar la informacion del token
        return Jwts.parser()
                //verifica que la clave secreat no alla sido modificado
                .verifyWith(getSigningKey())
                .build()
                //decodifgica el JWT y verifica la fimra y expiracion
                .parseSignedClaims(token)
                //devuelve el payload
                .getPayload();
    }
    // Verifica si el token ha expirado
    private Boolean isTokenExpired(String token){
        //bnefores es ujn metodo de date que compara dos fechas la de ahora, con la de expiracion
        //depende de la posicxion de la variable ahora es
        //la fecha de expiracion es menor que la fecha de ahora? entonces si vencia es true y si no en false
        return extractExpiration(token).before(new Date());
    }

    // Valida el token
    public Boolean validateToken(String token, String email){
        final String tokenEmail = extractEmail(token);
        return (tokenEmail.equals(email) && !isTokenExpired(token));
    }


}
