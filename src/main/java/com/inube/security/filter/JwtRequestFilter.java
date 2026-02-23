package com.inube.security.filter;

import com.inube.security.service.JwtUtilService;
import com.inube.security.service.UserDetailServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    //servicio que carga usuarios desde la base de datos
    @Autowired
    private UserDetailsService userDetailsService;

    //Servicio que genera y valida JWT
    @Autowired
    private JwtUtilService jwtUtilService;

    //Este metodo se ejecuta 1 vez porcada request HTTP
    //por eso extiende OnceRequestFilter
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        //Obtenemos el header Automatization
        final String authorizationHeader =
                request.getHeader("Authorization");

        //validamos que el header  exista
        //y que comience con "Bearer"
        if(authorizationHeader != null
        && authorizationHeader.startsWith("Bearer ")){
            //Extraemos el token quitando "Bearer"
            String jwt = authorizationHeader.substring(7) ;

            //extraemos el username del token
            String username = jwtUtilService.extractUsername(jwt);

            //verificaqmos:
            //Que el username no sea null
            //que no haya ya una autenticacion en el contexto
            if(username != null &&
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null
            ){
                //cargamos el usuario desde la base de datos
                UserDetails userDetails =
                        this.userDetailsService
                                .loadUserByUsername(username);

                //validamos token
                if(jwtUtilService.validateToken(jwt,userDetails)){
                    //creamos el objeto de autenticacion
                    UsernamePasswordAuthenticationToken autenticatonToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    //Agregamos detalles de la request
                    autenticatonToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    //Guardamos la autenticacion
                    //en el contexto de seuridad
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(autenticatonToken);
                }

            }

        }


        //continuamos con la cadena de filtros
        //si no hacemos esto, la peticion se bloquea
        filterChain.doFilter(request, response);
    }
}
