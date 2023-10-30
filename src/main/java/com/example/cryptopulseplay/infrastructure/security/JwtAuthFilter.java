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
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final Set<String> permitAllEndpointSet = Set.of("/signIn", "/verifyEmail", "/", "/index.html", "/verifyLoginToken", "/btc-price", "/game", "/addPoint");


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String path = request.getRequestURI();

        if (permitAllEndpointSet.contains(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = JwtUtil.extractToken(request);


        if (token != null) {

            try {

                jwtUtil.validateToken(token);

                Claims claims = jwtUtil.getClaims(token);

                String issuer = claims.getIssuer();

                System.out.println("issuer = " + issuer);

                String audience = claims.getAudience();

                System.out.println("audience = " + audience);

                if (!"CryptoPulsePlay".equals(issuer) || !"UserOnPlay".equals(audience)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }

                UserDetails userDetails = jwtUtil.getUserDetails(token);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);


                filterChain.doFilter(request, response);


            } catch (JwtException e) {

                throw new JwtValidationException("JWT Validation Exception", e);

            }
        }

        filterChain.doFilter(request, response);


    }
}
