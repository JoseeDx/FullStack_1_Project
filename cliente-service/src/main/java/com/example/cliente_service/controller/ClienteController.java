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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/clientes")
@Tag(name = "Clientes", description = "Gestión de clientes de la tienda")
public class ClienteController {

    private static final Logger log = LoggerFactory.getLogger(ClienteController.class);
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    @Operation(summary = "Crear un cliente", description = "Registra un nuevo cliente con su nombre, correo y rol.")
    public ResponseEntity<ClienteDTO> crearCliente(@Valid @RequestBody ClienteDTO clienteDto) {
    log.info("POST /api/v1/clientes - Creando cliente");
    Cliente nuevoCliente = clienteService.guardar(clienteDto.toModel());
    return new ResponseEntity<>(ClienteDTO.fromModel(nuevoCliente), HttpStatus.CREATED);
}

    @GetMapping
    @Operation(summary = "Listar todos los clientes", description = "Devuelve todos los clientes registrados en el sistema.")
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
    @Operation(summary = "Verificar existencia de un cliente", description = "Indica si existe un cliente con el ID indicado. Pensado para ser consumido por otros microservicios.")
    public ResponseEntity<Boolean> existeCliente(@PathVariable Long id) {
        log.info("GET /api/v1/clientes/{}/exists - Verificando existencia de cliente", id);
        return ResponseEntity.ok(clienteService.existePorId(id));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un cliente por ID", description = "Devuelve el detalle de un cliente específico.")
    public ResponseEntity<ClienteDTO> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/v1/clientes/{} - Buscando cliente", id);
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(ClienteDTO.fromModel(cliente));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un cliente", description = "Modifica los datos de un cliente existente.")
    public ResponseEntity<ClienteDTO> actualizarCliente(@PathVariable Long id, @Valid @RequestBody ClienteDTO dto) {
        log.info("PUT /api/v1/clientes/{} - Actualizando cliente", id);
        Cliente clienteActualizado = clienteService.actualizar(id, dto.toModel());
        return ResponseEntity.ok(ClienteDTO.fromModel(clienteActualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un cliente", description = "Elimina un cliente por su ID.")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        log.info("DELETE /api/v1/clientes/{} - Eliminando cliente", id);
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}