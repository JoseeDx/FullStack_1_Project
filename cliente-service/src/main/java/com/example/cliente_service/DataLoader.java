package com.example.cliente_service;

import com.example.cliente_service.model.Cliente;
import com.example.cliente_service.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public void run(String... args) throws Exception {
        // Verificamos si la base de datos está casi vacía para no duplicar datos en cada reinicio
        if (clienteRepository.count() <= 2) { 
            System.out.println("⏳ Cargando datos dinámicos de Clientes (DataLoader 30%)...");

            Cliente clienteDinamico = new Cliente();
            // Ajusta estos campos según los atributos exactos de tu entidad Cliente
            clienteDinamico.setNombre("Cliente Dinámico");
            clienteDinamico.setCorreo("pepito@example.com");
            // clienteDinamico.setDireccion("Calle Falsa 123"); 
            // clienteDinamico.setTelefono("+56912345678");

            clienteRepository.save(clienteDinamico);

            System.out.println("✅ Datos dinámicos de Clientes cargados exitosamente.");
        }
    }
}