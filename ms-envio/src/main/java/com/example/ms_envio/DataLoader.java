package com.example.ms_envio;

import com.example.ms_envio.model.Envio;
import com.example.ms_envio.repository.EnvioRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final EnvioRepository envioRepository;
    private final JdbcTemplate jdbcTemplate;

    DataLoader(EnvioRepository envioRepository, JdbcTemplate jdbcTemplate) {
        this.envioRepository = envioRepository;
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
        String[] estados = {"PREPARANDO", "EN_RUTA", "ENTREGADO"};
        // ms-pedido solo sembró los pedidos 1, 2 y 3: ciclamos entre esos IDs válidos
        int totalPedidosSembrados = 3;

        for (int i = 1; i <= 10; i++) {
            Envio envio = new Envio();
            envio.setIdPedido(((i - 1) % totalPedidosSembrados) + 1);
            envio.setDireccion(faker.address().streetAddress());
            envio.setCiudad(faker.address().city());

            String estado = estados[faker.number().numberBetween(0, estados.length)];
            envio.setEstado(estado);

            if (!estado.equals("PREPARANDO")) {
                envio.setFechaDespacho(LocalDateTime.now().minusDays(faker.number().numberBetween(0, 10)));
            }

            envioRepository.save(envio);
        }

        jdbcTemplate.update("UPDATE seed_control SET datafaker_seeded = TRUE WHERE id = 1");
    }
}
