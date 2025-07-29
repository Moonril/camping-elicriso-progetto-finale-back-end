package it.epicode.camping_elicriso_progetto_finale_back_end.security;


import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.NotFoundException;
import it.epicode.camping_elicriso_progetto_finale_back_end.exceptions.UnauthorizedException;
import it.epicode.camping_elicriso_progetto_finale_back_end.models.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class JwtFilter extends OncePerRequestFilter {
    // vai su errore, implement method


    @Autowired
    private JwtTool jwtTool;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        if(authorization== null || !authorization.startsWith("Bearer ")){
            throw new UnauthorizedException("Token not present");
        } else {
            String token = authorization.substring(7);
            jwtTool.validateToken(token);

            try{


                User user = jwtTool.getUserFromToken(token);

                Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());


                SecurityContextHolder.getContext().setAuthentication(authentication);


            } catch (NotFoundException e) {
                throw new UnauthorizedException("User connected to token not valid");
            }




            filterChain.doFilter(request, response);
        }
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        String method = request.getMethod();

        AntPathMatcher matcher = new AntPathMatcher();

        // Escludi solo GET e POST su /camping/bookings/**
        if (matcher.match("/camping/bookings/**", path) && (method.equals("GET") || method.equals("POST"))) {
            return true;
        }
        if (matcher.match("/restaurant/reservations/**", path) && (method.equals("POST"))) {
            return true;
        }

        if (matcher.match("/accommodations/**", path) && (method.equals("GET"))) {
            return true;
        }


        // Altri endpoint da escludere completamente
        String[] excludedEndpoints = new String[] {
                "/auth/**"
        };

        return Arrays.stream(excludedEndpoints)
                .anyMatch(e -> matcher.match(e, path));
    }



}