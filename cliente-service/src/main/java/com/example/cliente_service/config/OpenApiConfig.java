package com.example.cliente_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Cliente Service")
                        .version("1.0.0")
                        .description("Documentación robusta del microservicio de clientes, nivelado según estándares del proyecto."));
    }
}