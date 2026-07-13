package com.example.cliente_service;

import com.example.cliente_service.model.Cliente;
import com.example.cliente_service.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        // Marca de control propia, independiente de cuántas filas haya insertado Liquibase
        Boolean yaSembrado = jdbcTemplate.queryForObject(
                "SELECT datafaker_seeded FROM seed_control WHERE id = 1", Boolean.class);
        if (Boolean.TRUE.equals(yaSembrado)) {
            return;
        }

        System.out.println("⏳ Cargando datos dinámicos de Clientes (DataLoader 30%)...");

        Cliente clienteDinamico = new Cliente();
        // Ajusta estos campos según los atributos exactos de tu entidad Cliente
        clienteDinamico.setNombre("Cliente Dinámico");
        clienteDinamico.setCorreo("pepito@example.com");
        // clienteDinamico.setDireccion("Calle Falsa 123");
        // clienteDinamico.setTelefono("+56912345678");

        clienteRepository.save(clienteDinamico);

        jdbcTemplate.update("UPDATE seed_control SET datafaker_seeded = TRUE WHERE id = 1");
        System.out.println("✅ Datos dinámicos de Clientes cargados exitosamente.");
    }
}