package com.example.ms_resena;

import com.example.ms_resena.model.Resena;
import com.example.ms_resena.repository.ResenaRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final ResenaRepository resenaRepository;
    private final JdbcTemplate jdbcTemplate;

    DataLoader(ResenaRepository resenaRepository, JdbcTemplate jdbcTemplate) {
        this.resenaRepository = resenaRepository;
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
            Resena resena = new Resena();
            // id_producto: 1 al 10 (rango real sembrado en ms-producto); numberBetween es exclusivo del máximo
            resena.setIdProducto((long) faker.number().numberBetween(1, 11));
            // id_cliente: 1 al 3 (rango real sembrado en cliente-service)
            resena.setIdCliente((long) faker.number().numberBetween(1, 4));
            resena.setCalificacion(faker.number().numberBetween(1, 6));
            resena.setComentario(faker.lorem().sentence());

            resenaRepository.save(resena);
        }

        jdbcTemplate.update("UPDATE seed_control SET datafaker_seeded = TRUE WHERE id = 1");
    }
}
