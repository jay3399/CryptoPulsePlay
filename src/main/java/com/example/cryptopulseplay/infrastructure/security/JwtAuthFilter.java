package com.example.cryptopulseplay.infrastructure.security;

import com.example.cryptopulseplay.domian.shared.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        if (isAuthenticated()) {
            System.out.println("이미인증");
            filterChain.doFilter(request, response);
            return;
        }

        String token = JwtUtil.extractToken(request);

        if (token == null || !jwtUtil.validateToken(token)) {
            filterChain.doFilter(request, response);
            System.out.println("1");
            return;
        }

        if (!isValidToken(token, response)) {
            System.out.println("2");
            return;
        }

        UserDetails userDetails = jwtUtil.getUserDetails(token);

        setAuth(userDetails);

        filterChain.doFilter(request, response);


    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    private boolean isValidToken(String token, HttpServletResponse response) {

        Claims claims = jwtUtil.getClaims(token);
        String issuer = claims.getIssuer();
        String audience = claims.getAudience();

        if (!"CryptoPulsePlay".equals(issuer) || !"UserOnPlay".equals(audience)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        return true;

    }

    private void setAuth(UserDetails userDetails) {

        System.out.println("userDetails.getUsername() = " + userDetails.getUsername());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        System.out.println(SecurityContextHolder.getContext().getAuthentication());

    }


}

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication == null || !authentication.isAuthenticated()) {
//
//            String token = JwtUtil.extractToken(request);
//
//            if (token != null && jwtUtil.validateToken(token)) {
//                Claims claims = jwtUtil.getClaims(token);
//
//                String issuer = claims.getIssuer();
//                String audience = claims.getAudience();
//
//                if ("CryptoPulsePlay".equals(issuer) && "UserOnPlay".equals(audience)) {
//                    UserDetails userDetails = jwtUtil.getUserDetails(token);
//                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                    SecurityContextHolder.getContext().setAuthentication(auth);
//                } else {
//                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                    return;
//                }
//            }
//        }
//
//        filterChain.doFilter(request, response);
//    }