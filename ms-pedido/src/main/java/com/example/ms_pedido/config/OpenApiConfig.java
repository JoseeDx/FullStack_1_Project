package com.example.ms_pedido.config;

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
                        .title("API de Pedidos")
                        .version("1.0.0")
                        .description("Documentación interactiva del microservicio de Pedidos (ms-pedido) para el ecosistema EJEMPLO-GATEWAY."));
    }
}