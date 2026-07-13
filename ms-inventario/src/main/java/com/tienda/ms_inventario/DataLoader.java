package com.tienda.ms_inventario;

import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.tienda.ms_inventario.model.Inventario;
import com.tienda.ms_inventario.repository.InventarioRepository;

import java.time.LocalDateTime;


@Component
public class DataLoader implements CommandLineRunner {
    private final InventarioRepository inventarioRepository;
    private final JdbcTemplate jdbcTemplate;

    DataLoader(InventarioRepository inventarioRepository, JdbcTemplate jdbcTemplate) {
        this.inventarioRepository = inventarioRepository;
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
        // id_producto va del 1 al 10, que es el rango real sembrado en ms-producto

        Faker faker = new Faker();
        for (int i = 1; i <= 10; i++) {
            Inventario inventario = new Inventario();
            inventario.setId_producto(i);

            int stockMinimo = faker.number().numberBetween(5, 20);
            int stockMaximo = faker.number().numberBetween(stockMinimo + 30, stockMinimo + 100);
            int stockActual = faker.number().numberBetween(0, stockMaximo);

            inventario.setStock_minimo(stockMinimo);
            inventario.setStock_maximo(stockMaximo);
            inventario.setStock_actual(stockActual);
            inventario.setFecha_actualizacion(LocalDateTime.now());

            inventarioRepository.save(inventario);
        }

        jdbcTemplate.update("UPDATE seed_control SET datafaker_seeded = TRUE WHERE id = 1");
    }
}