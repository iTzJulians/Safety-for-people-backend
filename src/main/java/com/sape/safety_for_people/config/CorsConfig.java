package com.sape.safety_for_people.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Aplica a todos los endpoints del proyecto
                        .allowedOrigins(
                                "http://localhost:3000",
                                "http://localhost:5173",
                                "http://localhost:4200",
                                "http://localhost:5500",
                                "http://127.0.0.1:5500",
                                "http://127.0.0.1:5501",
                                "https://generation-classes.github.io"
                        ) // URLs comunes de frontend + deploy en GitHub Pages
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }
}