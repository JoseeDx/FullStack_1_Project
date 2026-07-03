package com.example.ms_resena;

import com.example.ms_resena.model.Resena;
import com.example.ms_resena.repository.ResenaRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final ResenaRepository resenaRepository;

    DataLoader(ResenaRepository resenaRepository) {
        this.resenaRepository = resenaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (resenaRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker();

        for (int i = 1; i <= 10; i++) {
            Resena resena = new Resena();
            resena.setIdProducto((long) faker.number().numberBetween(1, 20));
            resena.setIdCliente((long) faker.number().numberBetween(1, 15));
            resena.setCalificacion(faker.number().numberBetween(1, 6));
            resena.setComentario(faker.lorem().sentence());

            resenaRepository.save(resena);
        }
    }
}
