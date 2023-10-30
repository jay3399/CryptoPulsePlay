package com.example.cryptopulseplay.infrastructure.config;

import com.example.cryptopulseplay.application.exception.custom.SecurityRedirectionException;
import com.example.cryptopulseplay.infrastructure.security.JwtAuthFilter;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf(csrf -> csrf.disable()).sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).
                authorizeHttpRequests(
                        authz -> authz
                                .requestMatchers("/signIn", "/verifyEmail", "/", "/index.html",
                                        "/verifyLoginToken", "/btc-price", "/game", "/addPoint",
                                        "/notifications/**")
                                .permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated())
                .addFilterAfter(jwtAuthFilter, BasicAuthenticationFilter.class)
                .exceptionHandling(
                        e -> e.authenticationEntryPoint((request, response, authException)
                                        -> {
                                    try {
                                        response.sendRedirect("/");
                                    } catch (IOException ex) {
                                        throw new SecurityRedirectionException(
                                                "failed to redirect to the URL on Security", ex);
                                    }


                                }
                        )
                );

        return httpSecurity.build();

    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 상태 없음 설정
//                .authorizeHttpRequests(authz -> authz
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/signIn", "/verifyEmail", "/", "/index.html", "/verifyLoginToken", "/btc-price", "/game", "/addPoint", "/notifications/**").permitAll()
//                        .anyRequest().authenticated())
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//


}
