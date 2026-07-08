package com.tienda.ms_carrito;

import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tienda.ms_carrito.model.CarritoItem;
import com.tienda.ms_carrito.repository.CarritoItemRepository;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final CarritoItemRepository carritoItemRepository;

    DataLoader(CarritoItemRepository carritoItemRepository) {
        this.carritoItemRepository = carritoItemRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (carritoItemRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker();
        for (int i = 1; i <= 10; i++) {
            CarritoItem item = new CarritoItem();
            item.setId_cliente(faker.number().numberBetween(1, 5));
            item.setId_producto(faker.number().numberBetween(1, 10));
            item.setCantidad(faker.number().numberBetween(1, 5));
            item.setPrecio_unitario((double) faker.number().numberBetween(9990, 199990));
            item.setFecha_agregado(LocalDateTime.now().minusDays(faker.number().numberBetween(0, 15)));

            carritoItemRepository.save(item);
        }
    }
}
