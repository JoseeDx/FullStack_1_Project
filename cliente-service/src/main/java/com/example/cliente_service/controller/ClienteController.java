package com.example.cliente_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.example.cliente_service.dto.ClienteDTO;
import com.example.cliente_service.model.Cliente;
import com.example.cliente_service.service.ClienteService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private static final Logger log = LoggerFactory.getLogger(ClienteController.class);
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> crearCliente(@Valid @RequestBody ClienteDTO clienteDto) {
    log.info("POST /api/v1/clientes - Creando cliente");
    Cliente nuevoCliente = clienteService.guardar(clienteDto.toModel());
    return new ResponseEntity<>(ClienteDTO.fromModel(nuevoCliente), HttpStatus.CREATED);
}

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listarClientes() {
        log.info("GET /api/v1/clientes - Listando todos los clientes");
        List<Cliente> clientes = clienteService.listar();
        
        if (clientes.isEmpty()) {
            return ResponseEntity.noContent().build(); // Devuelve 204 si no hay datos
        }
        
        List<ClienteDTO> dtos = clientes.stream()
                .map(ClienteDTO::fromModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existeCliente(@PathVariable Long id) {
        log.info("GET /api/v1/clientes/{}/exists - Verificando existencia de cliente", id);
        return ResponseEntity.ok(clienteService.existePorId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/v1/clientes/{} - Buscando cliente", id);
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(ClienteDTO.fromModel(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> actualizarCliente(@PathVariable Long id, @Valid @RequestBody ClienteDTO dto) {
        log.info("PUT /api/v1/clientes/{} - Actualizando cliente", id);
        Cliente clienteActualizado = clienteService.actualizar(id, dto.toModel());
        return ResponseEntity.ok(ClienteDTO.fromModel(clienteActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        log.info("DELETE /api/v1/clientes/{} - Eliminando cliente", id);
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}