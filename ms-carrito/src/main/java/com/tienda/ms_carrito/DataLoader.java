package com.tienda.ms_carrito;

import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.tienda.ms_carrito.model.CarritoItem;
import com.tienda.ms_carrito.repository.CarritoItemRepository;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final CarritoItemRepository carritoItemRepository;
    private final JdbcTemplate jdbcTemplate;

    DataLoader(CarritoItemRepository carritoItemRepository, JdbcTemplate jdbcTemplate) {
        this.carritoItemRepository = carritoItemRepository;
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
        for (int i = 1; i <= 10; i++) {
            CarritoItem item = new CarritoItem();
            // id_cliente: 1 al 3 (rango real sembrado en cliente-service)
            item.setId_cliente(faker.number().numberBetween(1, 4));
            // id_producto: 1 al 10 (rango real sembrado en ms-producto); numberBetween es exclusivo del máximo
            item.setId_producto(faker.number().numberBetween(1, 11));
            item.setCantidad(faker.number().numberBetween(1, 5));
            item.setPrecio_unitario((double) faker.number().numberBetween(9990, 199990));
            item.setFecha_agregado(LocalDateTime.now().minusDays(faker.number().numberBetween(0, 15)));

            carritoItemRepository.save(item);
        }

        jdbcTemplate.update("UPDATE seed_control SET datafaker_seeded = TRUE WHERE id = 1");
    }
}
