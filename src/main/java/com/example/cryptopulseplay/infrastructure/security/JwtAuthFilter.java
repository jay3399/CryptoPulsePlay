package com.example.cryptopulseplay.infrastructure.security;

import com.example.cryptopulseplay.application.exception.custom.JwtValidationException;
import com.example.cryptopulseplay.domian.shared.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String token = JwtUtil.extractToken(request);

        if (token != null) {

            try {

                jwtUtil.validateToken(token);

                filterChain.doFilter(request, response);

            } catch (JwtException e) {

                throw new JwtValidationException("JWT Validation Exception", e);

            }
        }

        filterChain.doFilter(request, response);


    }
}
