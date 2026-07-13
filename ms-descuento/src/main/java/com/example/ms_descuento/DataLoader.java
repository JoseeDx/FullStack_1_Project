package com.example.ms_descuento;

import com.example.ms_descuento.model.Descuento;
import com.example.ms_descuento.repository.DescuentoRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final DescuentoRepository descuentoRepository;
    private final JdbcTemplate jdbcTemplate;

    DataLoader(DescuentoRepository descuentoRepository, JdbcTemplate jdbcTemplate) {
        this.descuentoRepository = descuentoRepository;
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
        String[] codigosCupon = {"DESC10", "VERANO20", "BIENVENIDA15", "FLASH30", "CYBER25"};

        for (String codigo : codigosCupon) {
            Descuento descuento = new Descuento();
            descuento.setCodigoCupon(codigo);
            descuento.setPorcentaje((double) faker.number().numberBetween(5, 50));
            descuento.setFechaExpiracion(LocalDateTime.now().plusDays(faker.number().numberBetween(10, 90)));
            descuento.setActivo(true);

            descuentoRepository.save(descuento);
        }

        jdbcTemplate.update("UPDATE seed_control SET datafaker_seeded = TRUE WHERE id = 1");
    }
}
