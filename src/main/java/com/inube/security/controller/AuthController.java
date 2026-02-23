package com.inube.security.controller;

import com.inube.security.model.UserModel;
import com.inube.security.repository.UserRepository;
import com.inube.security.service.JwtUtilService;
import dto.AuthRequestDto;
import dto.AuthResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

// @Controller indica que esta clase maneja peticiones HTTP
// En APIs REST normalmente se usa @RestController
@Controller

// Ruta base del controlador
@RequestMapping("/api/v1/auth")
public class AuthController {

    // Manager que ejecuta el proceso de autenticación
    @Autowired
    private AuthenticationManager authenticationManager;

    // Servicio que carga usuarios desde la BD
    @Autowired
    private UserDetailsService userDetailsService;

    // Servicio que genera y valida JWT
    @Autowired
    private JwtUtilService jwtUtilService;

    // Repositorio para consultar datos del usuario
    @Autowired
    private UserRepository userRepository;

    // =========================================================
    // 🔐 LOGIN
    // =========================================================
    @PostMapping("/login")
    public ResponseEntity<?> auth(
            @RequestBody AuthRequestDto authRequestDto) {
            try {
                //autenticacion
                this.authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                authRequestDto.getUser(),
                                authRequestDto.getPassword()
                        )
                );

                //cargar usuario desde la base de datos.
                UserDetails userDetails = this.userDetailsService
                        .loadUserByUsername(
                          authRequestDto.getUser()
                );

                UserModel userModel =
                userRepository.findByName(authRequestDto.getUser());

                //Generar token
                String jwt =
                        this.jwtUtilService
                                .generateToken(
                                        userDetails,
                                        userModel.getRole()
                                );

                String refreshToken =
                        this.jwtUtilService
                                .generateRefreshToken(
                                        userDetails,
                                        userModel.getRole()
                                );

                //cosntruir respuesta
                AuthResponseDto authResposeDto =
                        new AuthResponseDto();
                authResposeDto.setToken(jwt);
                authResposeDto.setRefreshToken(refreshToken);
                authResposeDto.setSuccess(Boolean.TRUE);

                return new ResponseEntity<>(
                        authResposeDto,
                        HttpStatus.OK
                );

            }catch (Exception ex){
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Error Autenticacion:::"+ ex.getMessage());
            }

    }
}