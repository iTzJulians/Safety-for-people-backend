package com.sape.safety_for_people.config;

import com.sape.safety_for_people.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Lecturas públicas (catálogo, dashboard, API docs, gestión admin)
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()

                        // Operaciones públicas que no requieren login
                        .requestMatchers(HttpMethod.POST, "/api/auth/**", "/api/sales", "/api/statuses").permitAll()

                        // Preflight de CORS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Todo lo demás (PUT/PATCH/DELETE y el resto de POST) requiere token JWT
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}