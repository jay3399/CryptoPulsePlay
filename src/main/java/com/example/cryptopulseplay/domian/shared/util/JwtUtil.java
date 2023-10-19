package com.example.cryptopulseplay.domian.shared.util;

import com.example.cryptopulseplay.domian.user.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    public final String secret;

    @Autowired
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }



    public  String generateToken(User user, String purpose) {

        try {
            return Jwts.builder()
                    .setSubject(user.getEmail())
                    .claim("userId", user.getId())
                    .claim("purpose", purpose)
                    .setExpiration(
                            new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1)))
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .compact();

        } catch (JwtException e) {
            return null;
        }
    }

    public String generateToken(String email, String purpose) {

        try {

            return Jwts.builder()
                    .setSubject(email)
                    .claim("purpose", purpose)
                    .setExpiration(
                            new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(1)))
                    .signWith(SignatureAlgorithm.HS256, secret)
                    .compact();

        } catch (JwtException e) {

            return null;
        }

    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .claim("purpose", "refresh")
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7)))  // 7일 동안 유효
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            return claims.get("userId", Long.class);
        } catch (JwtException e) {
            return null;
            //예외추가
        }
    }


    public Claims getEmailFromToken(String token) {

        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            return null;
        }

    }



    public boolean validateToken(String token) {

        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

            return true;

//            return !isTokenExpired(claims) && isTokenExpired(claims);

        } catch (JwtException | IllegalArgumentException exception) {

            return false;
        }

    }

    public static String extractToken(HttpServletRequest request) {
        String token = getAuthorization(request);
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7); // "Bearer " 접두사를 제거합니다.
        }
        return null;
    }

    private static String getAuthorization(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }


    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    private boolean isTokenForLoginCheck(Claims claims) {
        return "loginCheck".equals(claims.get("purpose", String.class));
    }








}
