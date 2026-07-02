package com.example.ms_descuento;

import com.example.ms_descuento.model.Descuento;
import com.example.ms_descuento.repository.DescuentoRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final DescuentoRepository descuentoRepository;

    DataLoader(DescuentoRepository descuentoRepository) {
        this.descuentoRepository = descuentoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
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
    }
}
