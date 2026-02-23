package com.inube.security.service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

//import static jdk.internal.classfile.Classfile.build;

//@Service indica que esta clase es un componente de la capade servicio
//spring la administrara automaticamente (inyeccion dependencias)
@Service
public class JwtUtilService {

    //clave secreta se usa para firmar digitalmente el token
    //en produccion no se debe harcodear el KEY.
    //Debe ir en application.properties o variables de entorno.
    private static final String JWT_SECRET_KEY = "TExBVkVfTVVZX1NFQ1JFVEzE3Zmxu7BSGSJx72BSBXM";

    //Tiempo de expiracion del token (15 min)
    //1000 ms * 60s * 15
    private static final long JWT_TIME_VALIDITY =
            100 * 60 *15;

    //Tiempo de validez del refresh token (24 hrs)
    //1000 ms * 60 seg * 60min * 24 hrs
    private static final long JWT_TIME_REFRESH_VALIDATE =
            1000 * 60 * 60 * 24;

    public String generateToken(UserDetails userDetails, String role){
        //arreglamos el rol dentro del token
        var claims =  new HashMap<String, Object>();
        claims.put("role",role);

        //contruimos el JWT
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(
                        System.currentTimeMillis()))
                .setExpiration(new Date(
                    System.currentTimeMillis() +JWT_TIME_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails, String role) {

        var claims = new HashMap<String, Object>();
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))

                // Expira en 24 horas
                .setExpiration(new Date(
                        System.currentTimeMillis() + JWT_TIME_REFRESH_VALIDATE))

                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                .compact();
    }

    //VALIDAR TOKEN
    public boolean validateToken(String token, UserDetails userDetails){
        //Verifica que:
        //1. El username del token  sea igual al del usuario
        //2. El token no esté expirando.
        return extractClaim(
            token, Claims::getSubject)
            .equals(userDetails.getUsername())
            && !extractClaim(token, Claims::getExpiration)
            .before(new Date()
        );
    }

    //Metodo generico para extraer informacion del token.
    private <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        //parseamos el token y validamos la firma
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        //Aplicamos la funcion para extraer la informacion deseada
        return claimsResolver.apply(claims);
    }

    //extraer username
    public String extractUsername(String token){
        //extrae el "subject" (usuario)
        return extractClaim(token, Claims::getSubject);
    }

}
