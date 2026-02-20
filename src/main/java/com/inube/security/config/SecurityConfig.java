package com.inube.security.config;

import com.inube.security.filter.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;
@Configuration
public class SecurityConfig {

    //Inyectamos nuestro filtro JWT personalizado
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    //CONFIGURACION PRINCIPAL DE SEGURIDAD.
    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception{
        http
        //Habilita CORS con configuracion por defecto
        .cors(withDefaults())
        //se desactiva porque estamos usando JWT stateless
        .csrf(crf -> crf.disable())
        //configuracion de autorizacion
        .authorizeHttpRequests((authorize ) -> authorize
            //permite acceso libre a rutas de authenticacion
            .requestMatchers("api/v1/auth/**").permitAll()
            //cualquier otra peticion requiere autenticacion
            .anyRequest().authenticated()
        )
        //agregamos nuestro filtro JWT antes del filtro
        //de autenticacion tradicional
        .addFilterBefore(
                jwtRequestFilter,
                UsernamePasswordAuthenticationFilter.class
        )
        //inidcaremos no usaremos sessions
        //cada request debe traer su token
        .sessionManagement((session) -> session
                .sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS)

        );

        //construimos la configuracion final
        return http.build();
    }

    /*
    * ENCODER DE PASSWORDS
    * */
    @Bean
    PasswordEncoder passwordEncoder(){
        //BCrypt es un algoritmo de hashing seguro
        return new BCryptPasswordEncoder();
    }

    /*
    * AUTENTICATION MANAGER
    * */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration autenticationConfiguration
            )throws Exception{
            //Obtiene el autenticationManagr
            // que spring configura automaticamente
            return autenticationConfiguration.getAuthenticationManager();
    }
}
