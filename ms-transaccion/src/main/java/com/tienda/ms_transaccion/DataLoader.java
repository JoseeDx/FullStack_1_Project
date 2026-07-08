package com.tienda.ms_transaccion;

import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tienda.ms_transaccion.model.Transaccion;
import com.tienda.ms_transaccion.repository.TransaccionRepository;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final TransaccionRepository transaccionRepository;

    DataLoader(TransaccionRepository transaccionRepository) {
        this.transaccionRepository = transaccionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (transaccionRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker();
        String[] metodosPago = {"Tarjeta de crédito", "Tarjeta de débito", "Transferencia bancaria"};
        String[] estadosPago = {"PENDIENTE", "COMPLETADA", "ANULADA"};

        for (int i = 1; i <= 5; i++) {
            Transaccion transaccion = new Transaccion();
            transaccion.setId_pedido(i);
            transaccion.setId_cliente(faker.number().numberBetween(1, 5));
            transaccion.setMetodo_pago(metodosPago[faker.number().numberBetween(0, metodosPago.length)]);
            transaccion.setMonto_pago((double) faker.number().numberBetween(19990, 299990));
            transaccion.setEstado_pago(estadosPago[faker.number().numberBetween(0, estadosPago.length)]);
            transaccion.setFecha_transaccion(LocalDateTime.now().minusDays(faker.number().numberBetween(0, 15)));

            transaccionRepository.save(transaccion);
        }
    }
}
