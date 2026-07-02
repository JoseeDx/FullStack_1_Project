package com.example.ms_envio;

import com.example.ms_envio.model.Envio;
import com.example.ms_envio.repository.EnvioRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final EnvioRepository envioRepository;

    DataLoader(EnvioRepository envioRepository) {
        this.envioRepository = envioRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();
        String[] estados = {"PREPARANDO", "EN_RUTA", "ENTREGADO"};

        for (int i = 1; i <= 10; i++) {
            Envio envio = new Envio();
            envio.setIdPedido(i);
            envio.setDireccion(faker.address().streetAddress());
            envio.setCiudad(faker.address().city());

            String estado = estados[faker.number().numberBetween(0, estados.length)];
            envio.setEstado(estado);

            if (!estado.equals("PREPARANDO")) {
                envio.setFechaDespacho(LocalDateTime.now().minusDays(faker.number().numberBetween(0, 10)));
            }

            envioRepository.save(envio);
        }
    }
}
