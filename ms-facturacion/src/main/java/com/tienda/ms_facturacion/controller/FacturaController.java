package com.tienda.ms_facturacion.controller;

import com.tienda.ms_facturacion.dto.FacturaDTO;
import com.tienda.ms_facturacion.model.Factura;
import com.tienda.ms_facturacion.service.FacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/facturas")
@Tag(name = "Facturas", description = "Gestión de la facturación de pedidos")
public class FacturaController {

    private static Logger log = LoggerFactory.getLogger(FacturaController.class);

    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las facturas", description = "Devuelve todas las facturas registradas.")
    public ResponseEntity<List<FacturaDTO>> listar() {
        log.info("GET /api/v1/facturas - Listando todas las facturas");
        List<Factura> facturas = facturaService.listar();
        if (facturas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(facturas.stream().map(FacturaDTO::fromModel).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una factura por ID", description = "Devuelve el detalle de una factura específica.")
    public ResponseEntity<FacturaDTO> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/v1/facturas/{}", id);
        return ResponseEntity.ok(FacturaDTO.fromModel(facturaService.obtenerPorId(id)));
    }

    @GetMapping("/pedido/{id_pedido}")
    @Operation(summary = "Obtener facturas por pedido", description = "Devuelve las facturas asociadas a un pedido específico.")
    public ResponseEntity<List<FacturaDTO>> obtenerPorIdPedido(@PathVariable Long id_pedido) {
        log.info("GET /api/v1/facturas/pedido/{}", id_pedido);
        List<Factura> facturas = facturaService.obtenerPorIdPedido(id_pedido);
        if (facturas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(facturas.stream().map(FacturaDTO::fromModel).collect(Collectors.toList()));
    }

    @GetMapping("/rut/{rut_cliente}")
    @Operation(summary = "Obtener facturas por RUT de cliente", description = "Devuelve las facturas asociadas a un cliente según su RUT.")
    public ResponseEntity<List<FacturaDTO>> obtenerPorRut(@PathVariable String rut_cliente) {
        log.info("GET /api/v1/facturas/rut/{}", rut_cliente);
        List<Factura> facturas = facturaService.obtenerPorRut(rut_cliente);
        if (facturas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(facturas.stream().map(FacturaDTO::fromModel).collect(Collectors.toList()));
    }

    @GetMapping("/estado/{estado_factura}")
    @Operation(summary = "Obtener facturas por estado", description = "Devuelve las facturas que se encuentran en un estado específico (ej: EMITIDA, ANULADA).")
    public ResponseEntity<List<FacturaDTO>> obtenerPorEstado(@PathVariable String estado_factura) {
        log.info("GET /api/v1/facturas/estado/{}", estado_factura);
        List<Factura> facturas = facturaService.obtenerPorEstado(estado_factura);
        if (facturas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(facturas.stream().map(FacturaDTO::fromModel).collect(Collectors.toList()));
    }

    @PostMapping
    @Operation(summary = "Crear una factura", description = "Emite una nueva factura para un pedido, calculando IVA y total, validando el pedido contra ms-pedido.")
    public ResponseEntity<FacturaDTO> guardar(@Valid @RequestBody FacturaDTO dto) {
        log.info("POST /api/v1/facturas - Creando factura");
        return new ResponseEntity<>(FacturaDTO.fromModel(facturaService.guardar(dto.toModel())), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una factura", description = "Modifica los datos de una factura existente y recalcula IVA y total.")
    public ResponseEntity<FacturaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody FacturaDTO dto) {
        log.info("PUT /api/v1/facturas/{}", id);
        return ResponseEntity.ok(FacturaDTO.fromModel(facturaService.actualizar(id, dto.toModel())));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una factura", description = "Elimina una factura por su ID.")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/facturas/{}", id);
        facturaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}