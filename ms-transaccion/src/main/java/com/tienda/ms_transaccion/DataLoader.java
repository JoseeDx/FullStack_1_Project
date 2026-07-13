package com.tienda.ms_transaccion;

import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.tienda.ms_transaccion.model.Transaccion;
import com.tienda.ms_transaccion.repository.TransaccionRepository;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final TransaccionRepository transaccionRepository;
    private final JdbcTemplate jdbcTemplate;

    DataLoader(TransaccionRepository transaccionRepository, JdbcTemplate jdbcTemplate) {
        this.transaccionRepository = transaccionRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        // Marca de control propia, independiente de cuántas filas haya insertado Liquibase
        Boolean yaSembrado = jdbcTemplate.queryForObject(
                "SELECT datafaker_seeded FROM seed_control WHERE id = 1", Boolean.class);
        if (Boolean.TRUE.equals(yaSembrado)) {
            return;
        }

        Faker faker = new Faker();
        String[] metodosPago = {"Tarjeta de crédito", "Tarjeta de débito", "Transferencia bancaria"};
        String[] estadosPago = {"PENDIENTE", "COMPLETADA", "ANULADA"};
        // ms-pedido solo sembró los pedidos 1, 2 y 3: ciclamos entre esos IDs válidos
        int totalPedidosSembrados = 3;

        for (int i = 1; i <= 5; i++) {
            Transaccion transaccion = new Transaccion();
            transaccion.setId_pedido(((i - 1) % totalPedidosSembrados) + 1);
            // id_cliente: 1 al 3 (rango real sembrado en cliente-service)
            transaccion.setId_cliente(faker.number().numberBetween(1, 4));
            transaccion.setMetodo_pago(metodosPago[faker.number().numberBetween(0, metodosPago.length)]);
            transaccion.setMonto_pago((double) faker.number().numberBetween(19990, 299990));
            transaccion.setEstado_pago(estadosPago[faker.number().numberBetween(0, estadosPago.length)]);
            transaccion.setFecha_transaccion(LocalDateTime.now().minusDays(faker.number().numberBetween(0, 15)));

            transaccionRepository.save(transaccion);
        }

        jdbcTemplate.update("UPDATE seed_control SET datafaker_seeded = TRUE WHERE id = 1");
    }
}
