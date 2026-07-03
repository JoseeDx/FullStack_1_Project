package com.tienda.ms_inventario;

import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tienda.ms_inventario.model.Inventario;
import com.tienda.ms_inventario.repository.InventarioRepository;

import java.time.LocalDateTime;


@Component
public class DataLoader implements CommandLineRunner {
    private final InventarioRepository inventarioRepository;

    DataLoader(InventarioRepository inventarioRepository) {
        this.inventarioRepository = inventarioRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (inventarioRepository.count() > 0) {
            return;
        }

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
    }
}