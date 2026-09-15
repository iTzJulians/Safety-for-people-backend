package com.sape.safety_for_people.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Safety for People API")
                        .version("1.0.0")
                        .description("Documentación interactiva de los endpoints del backend Safety for People.")
                        .contact(new Contact()
                                .name("Equipo Safety for People")));
    }
}