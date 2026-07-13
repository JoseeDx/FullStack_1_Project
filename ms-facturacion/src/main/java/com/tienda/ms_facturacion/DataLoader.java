package com.tienda.ms_facturacion;

import com.tienda.ms_facturacion.model.Factura;
import com.tienda.ms_facturacion.repository.FacturaRepository;
import net.datafaker.Faker;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final FacturaRepository facturaRepository;
    private final JdbcTemplate jdbcTemplate;

    DataLoader(FacturaRepository facturaRepository, JdbcTemplate jdbcTemplate) {
        this.facturaRepository = facturaRepository;
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
        String[] rutsValidos = {"11111111-1", "22222222-2", "12345678-5", "9876543-3"};
        // ms-pedido solo sembró los pedidos 1, 2 y 3: ciclamos entre esos IDs válidos
        int totalPedidosSembrados = 3;

        for (int i = 1; i <= 5; i++) {
            Factura factura = new Factura();
            factura.setId_pedido((long) (((i - 1) % totalPedidosSembrados) + 1));
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

        jdbcTemplate.update("UPDATE seed_control SET datafaker_seeded = TRUE WHERE id = 1");
    }
}