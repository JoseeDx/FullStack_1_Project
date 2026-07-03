package com.tienda.ms_facturacion;

import com.tienda.ms_facturacion.model.Factura;
import com.tienda.ms_facturacion.repository.FacturaRepository;
import net.datafaker.Faker;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final FacturaRepository facturaRepository;

    DataLoader(FacturaRepository facturaRepository) {
        this.facturaRepository = facturaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (facturaRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker();
        String[] rutsValidos = {"11111111-1", "22222222-2", "12345678-5", "9876543-3"};

        for (int i = 1; i <= 5; i++) {
            Factura factura = new Factura();
            factura.setId_pedido((long) i);
            factura.setRut_cliente(rutsValidos[faker.number().numberBetween(0, rutsValidos.length)]);

            int subtotal = faker.number().numberBetween(10000, 200000);
            int iva = subtotal * 19 / 100;

            factura.setSubtotal(subtotal);
            factura.setIva(iva);
            factura.setTotal(subtotal + iva);
            factura.setEstado_factura("EMITIDA");
            factura.setFecha_factura(LocalDateTime.now());

            facturaRepository.save(factura);
        }
    }
}