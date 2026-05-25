package com.example.cliente_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional; // Usa org.springframework.transaction.annotation.Transactional si usas Spring Boot 2.x

import com.example.cliente_service.model.Cliente;
import com.example.cliente_service.repository.ClienteRepository;
import com.example.cliente_service.exception.ResourceNotFoundException;
import java.util.List;

@Service
@Transactional
public class ClienteService {

    private static final Logger log = LoggerFactory.getLogger(ClienteService.class);
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente guardar(Cliente cliente) {
        log.info("Guardando nuevo cliente");
        try {
            return clienteRepository.save(cliente);
        } catch (Exception e) {
            log.error("Error al guardar cliente: {}", e.getMessage());
            throw new RuntimeException("Error al procesar el guardado del cliente");
        }
    }

    public boolean existePorId(Long id) {
        log.info("Consultando si existe cliente con ID: {}", id);
        try {
            return clienteRepository.existsById(id);
        } catch (Exception e) {
            log.error("Error al verificar existencia del cliente con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al verificar la base de datos");
        }
    }

    public List<Cliente> listar() {
        log.info("Consultando todos los clientes");
        try {
            return clienteRepository.findAll();
        } catch (Exception e) {
            log.error("Error al consultar lista de clientes: {}", e.getMessage());
            throw new RuntimeException("Error al obtener los clientes");
        }
    }
    
    
    public Cliente buscarPorId(Long id) {
        log.info("Buscando cliente por ID: {}", id);
        return clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cliente con ID: {} no encontrado", id);
                    return new ResourceNotFoundException("Cliente no encontrado con ID: " + id);
                });
    }


    public Cliente actualizar(Long id, Cliente datosActualizados) {
        log.info("Actualizando cliente con ID: {}", id);
        try {
            Cliente existente = buscarPorId(id); // Reutilizamos el método que ya lanza el 404
            existente.setNombre(datosActualizados.getNombre());
            existente.setCorreo(datosActualizados.getCorreo());
            // Actualizar el rol requiere buscar el objeto Rol, asumiendo que solo cambian campos básicos:
            return clienteRepository.save(existente);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al actualizar cliente ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al actualizar el cliente");
        }
    }

    public void eliminar(Long id) {
        log.info("Borrando cliente con ID: {}", id);
        try {
            if (!clienteRepository.existsById(id)) {
                throw new ResourceNotFoundException("Cliente no encontrado con ID: " + id);
            }
            clienteRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al eliminar cliente con ID: {}", id);
            throw new RuntimeException("Error al eliminar el cliente");            
        }
    }
}