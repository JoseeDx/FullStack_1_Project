package com.tienda.ms_facturacion.controller;

import com.tienda.ms_facturacion.dto.FacturaDTO;
import com.tienda.ms_facturacion.model.Factura;
import com.tienda.ms_facturacion.service.FacturaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/facturas")
public class FacturaController {

    private static Logger log = LoggerFactory.getLogger(FacturaController.class);

    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @GetMapping
    public ResponseEntity<List<Factura>> listar() {
        log.info("GET /api/v1/facturas - Listando todas las facturas");
        List<Factura> facturas = facturaService.listar();
        if (facturas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(facturas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Factura> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/v1/facturas/{}", id);
        return ResponseEntity.ok(facturaService.obtenerPorId(id));
    }

    @GetMapping("/pedido/{id_pedido}")
    public ResponseEntity<List<Factura>> obtenerPorIdPedido(@PathVariable Long id_pedido) {
        log.info("GET /api/v1/facturas/pedido/{}", id_pedido);
        List<Factura> facturas = facturaService.obtenerPorIdPedido(id_pedido);
        if (facturas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(facturas);
    }

    @GetMapping("/rut/{rut_cliente}")
    public ResponseEntity<List<Factura>> obtenerPorRut(@PathVariable String rut_cliente) {
        log.info("GET /api/v1/facturas/rut/{}", rut_cliente);
        List<Factura> facturas = facturaService.obtenerPorRut(rut_cliente);
        if (facturas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(facturas);
    }

    @GetMapping("/estado/{estado_factura}")
    public ResponseEntity<List<Factura>> obtenerPorEstado(@PathVariable String estado_factura) {
        log.info("GET /api/v1/facturas/estado/{}", estado_factura);
        List<Factura> facturas = facturaService.obtenerPorEstado(estado_factura);
        if (facturas.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(facturas);
    }

    @PostMapping
    public ResponseEntity<Factura> guardar(@Valid @RequestBody FacturaDTO dto) {
        log.info("POST /api/v1/facturas - Creando factura");
        return new ResponseEntity<>(facturaService.guardar(dto.toModel()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Factura> actualizar(@PathVariable Long id, @Valid @RequestBody FacturaDTO dto) {
        log.info("PUT /api/v1/facturas/{}", id);
        return ResponseEntity.ok(facturaService.actualizar(id, dto.toModel()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/facturas/{}", id);
        facturaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}