package com.example.ms_descuento.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Descuentos - Proyecto Fullstack 1")
                        .version("1.0.0")
                        .description("Microservicio encargado de la gestión y aplicación de cupones de descuento.")
                        .contact(new Contact()
                                .name("Tu Nombre/Equipo")
                                .email("correo@ejemplo.com")));
    }
}