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
                authorizeHttpRequests(authz -> authz
                                .requestMatchers("/signIn", "/verifyEmail", "/", "/index.html", "/verifyLoginToken", "/btc-price", "/game", "/addPoint", "/test", "/notifications/**" ,"/currentUser")
                                .permitAll()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, BasicAuthenticationFilter.class)
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

}
