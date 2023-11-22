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

        String token = JwtUtil.extractToken(request);

        if (token == null || !jwtUtil.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!isValidToken(token, response)) {
            return;
        }

        UserDetails userDetails = jwtUtil.getUserDetails(token);

        setAuth(userDetails);

        filterChain.doFilter(request, response);


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


        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        System.out.println(SecurityContextHolder.getContext().getAuthentication());

    }


}


/**
 *  현재는 , 무상태 세션정책을 사용중.
 *  but , 다중필터체인이라던지 , 추가적인 세션기반인증을 혼합해서 사용하거나 , 인증후 처리를 할때는 필요하다.
 */


//        if (isAuthenticated()) {
//            System.out.println("0");
//            filterChain.doFilter(request, response);
//            return;
//        }


//    private boolean isAuthenticated() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return authentication != null && authentication.isAuthenticated();
//    }


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