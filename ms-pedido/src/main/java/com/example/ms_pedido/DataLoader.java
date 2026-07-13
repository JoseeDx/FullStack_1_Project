package com.example.ms_pedido;

import com.example.ms_pedido.model.Pedido;
import com.example.ms_pedido.repository.PedidoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner initData(PedidoRepository pedidoRepository, JdbcTemplate jdbcTemplate) {
        return args -> {
            // Marca de control propia, independiente de cuántas filas haya insertado Liquibase
            Boolean yaSembrado = jdbcTemplate.queryForObject(
                    "SELECT datafaker_seeded FROM seed_control WHERE id = 1", Boolean.class);
            if (Boolean.TRUE.equals(yaSembrado)) {
                System.out.println("⚡ DataLoader: ya se generaron datos antes. Omitiendo inserción.");
                return;
            }

            Pedido pedido1 = new Pedido();
            // El idPedido es autogenerado, por lo que solo asignamos la fecha
            pedido1.setFechaPedido(LocalDateTime.now());

            Pedido pedido2 = new Pedido();
            pedido2.setFechaPedido(LocalDateTime.now().minusDays(2));

            Pedido pedido3 = new Pedido();
            pedido3.setFechaPedido(LocalDateTime.now().minusHours(5));

            // Guardamos todos los registros en la base de datos
            pedidoRepository.saveAll(Arrays.asList(pedido1, pedido2, pedido3));

            jdbcTemplate.update("UPDATE seed_control SET datafaker_seeded = TRUE WHERE id = 1");
            System.out.println("✅ DataLoader: Datos de prueba insertados en ms-pedido exitosamente.");
        };
    }
}