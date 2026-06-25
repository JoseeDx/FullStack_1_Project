package com.tienda.ms_inventario.controller;

import com.tienda.ms_inventario.dto.InventarioDTO;
import com.tienda.ms_inventario.model.Inventario;
import com.tienda.ms_inventario.service.InventarioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventario")
public class InventarioController {

    private static Logger log = LoggerFactory.getLogger(InventarioController.class);

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public ResponseEntity<List<Inventario>> listar() {
        log.info("GET /api/v1/inventario - Listando todo el inventario");
        List<Inventario> inventarios = inventarioService.listar();
        if (inventarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(inventarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventario> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/v1/inventario/{} - Buscando inventario", id);
        return ResponseEntity.ok(inventarioService.obtenerPorId(id));
    }

    @GetMapping("/producto/{id_producto}")
    public ResponseEntity<Inventario> obtenerPorIdProducto(@PathVariable Integer id_producto) {
        log.info("GET /api/v1/inventario/producto/{} - Buscando inventario por producto", id_producto);
        return ResponseEntity.ok(inventarioService.obtenerPorIdProducto(id_producto));
    }

    @GetMapping("/bajo-stock")
    public ResponseEntity<List<Inventario>> obtenerBajoStock() {
        log.info("GET /api/v1/inventario/bajo-stock - Buscando productos con bajo stock");
        List<Inventario> bajoStock = inventarioService.obtenerBajoStock();
        if (bajoStock.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bajoStock);
    }

    @GetMapping("/stock-suficiente/{id_producto}/{cantidad}")
    public ResponseEntity<Boolean> hayStockSuficiente(@PathVariable Integer id_producto, @PathVariable Integer cantidad) {
        log.info("GET /api/v1/inventario/stock-suficiente/{}/{}", id_producto, cantidad);
        return ResponseEntity.ok(inventarioService.hayStockSuficiente(id_producto, cantidad));
    }

    @PostMapping
    public ResponseEntity<Inventario> guardar(@Valid @RequestBody InventarioDTO dto) {
        log.info("POST /api/v1/inventario - Creando inventario");
        return new ResponseEntity<>(inventarioService.guardar(dto.toModel()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizar(@PathVariable Long id, @Valid @RequestBody InventarioDTO dto) {
        log.info("PUT /api/v1/inventario/{} - Actualizando inventario", id);
        return ResponseEntity.ok(inventarioService.actualizar(id, dto.toModel()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/inventario/{} - Eliminando inventario", id);
        inventarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

