package com.example.cryptopulseplay.infrastructure.security;

import com.example.cryptopulseplay.application.exception.custom.JwtValidationException;
import com.example.cryptopulseplay.domian.shared.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

                Claims claims = jwtUtil.getClaims(token);

                String issuer = claims.getIssuer();
                String audience = claims.getAudience();

                if (!"CryptoPulsePlay".equals(issuer) || !"UserOnPlay".equals(audience)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }


                String role = jwtUtil.extractRoleFromToken(token);

                List<SimpleGrantedAuthority> simpleGrantedAuthorities = List.of(new SimpleGrantedAuthority("ROLE" + role));

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(null, null,
                        simpleGrantedAuthorities);

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);


                filterChain.doFilter(request, response);

            } catch (JwtException e) {

                throw new JwtValidationException("JWT Validation Exception", e);

            }
        }

        filterChain.doFilter(request, response);


    }
}
