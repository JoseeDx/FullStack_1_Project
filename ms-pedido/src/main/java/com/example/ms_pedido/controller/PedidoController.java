package com.example.ms_pedido.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import com.example.ms_pedido.dto.PedidoDTO;
import com.example.ms_pedido.model.Pedido;
import com.example.ms_pedido.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/pedidos")
@Tag(name = "Pedidos", description = "Gestión de pedidos de la tienda")
public class PedidoController {

    private static final Logger log = LoggerFactory.getLogger(PedidoController.class);
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los pedidos", description = "Devuelve todos los pedidos registrados en el sistema.")
    public ResponseEntity<List<PedidoDTO>> listarPedidos() {
        log.info("GET /api/v1/pedidos - Listando todos los pedidos");
        List<Pedido> pedidos = pedidoService.listarTodos();

        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<PedidoDTO> pedidosDTO = pedidos.stream()
                .map(PedidoDTO::fromModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(pedidosDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un pedido por ID", description = "Devuelve el detalle de un pedido específico.")
    public ResponseEntity<PedidoDTO> obtenerPedido(@PathVariable Long id) {
        log.info("GET /api/v1/pedidos/{} - Buscando pedido", id);
        Pedido pedido = pedidoService.buscarPorId(id);
        return ResponseEntity.ok(PedidoDTO.fromModel(pedido));
    }

    @PostMapping
    @Operation(summary = "Crear un pedido", description = "Registra un nuevo pedido en el sistema.")
    public ResponseEntity<PedidoDTO> crearPedido(@Valid @RequestBody PedidoDTO pedidoDTO) {
        log.info("POST /api/v1/pedidos - Creando pedido");
        Pedido pedidoGuardado = pedidoService.guardar(pedidoDTO.toModel());
        return new ResponseEntity<>(PedidoDTO.fromModel(pedidoGuardado), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un pedido", description = "Modifica los datos de un pedido existente.")
    public ResponseEntity<PedidoDTO> actualizarPedido(@PathVariable Long id, @Valid @RequestBody PedidoDTO dto) {
        log.info("PUT /api/v1/pedidos/{} - Actualizando pedido", id);
        Pedido pedidoActualizado = pedidoService.actualizar(id, dto.toModel());
        return ResponseEntity.ok(PedidoDTO.fromModel(pedidoActualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un pedido", description = "Elimina un pedido por su ID.")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        log.info("DELETE /api/v1/pedidos/{} - Eliminando pedido", id);
        pedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}