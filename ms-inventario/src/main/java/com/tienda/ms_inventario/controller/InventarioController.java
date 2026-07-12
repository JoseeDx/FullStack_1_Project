package com.tienda.ms_inventario.controller;

import com.tienda.ms_inventario.dto.InventarioDTO;
import com.tienda.ms_inventario.model.Inventario;
import com.tienda.ms_inventario.service.InventarioService;
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
@RequestMapping("/api/v1/inventario")
@Tag(name = "Inventario", description = "Gestión del stock de productos")
public class InventarioController {

    private static Logger log = LoggerFactory.getLogger(InventarioController.class);

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    @Operation(summary = "Listar todo el inventario", description = "Devuelve el registro de stock de todos los productos.")
    public ResponseEntity<List<InventarioDTO>> listar() {
        log.info("GET /api/v1/inventario - Listando todo el inventario");
        List<Inventario> inventarios = inventarioService.listar();
        if (inventarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(inventarios.stream().map(InventarioDTO::fromModel).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener inventario por ID", description = "Devuelve el registro de stock con el ID indicado.")
    public ResponseEntity<InventarioDTO> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/v1/inventario/{} - Buscando inventario", id);
        return ResponseEntity.ok(InventarioDTO.fromModel(inventarioService.obtenerPorId(id)));
    }

    @GetMapping("/producto/{id_producto}")
    @Operation(summary = "Obtener inventario por producto", description = "Devuelve el registro de stock asociado a un producto específico.")
    public ResponseEntity<InventarioDTO> obtenerPorIdProducto(@PathVariable Integer id_producto) {
        log.info("GET /api/v1/inventario/producto/{} - Buscando inventario por producto", id_producto);
        return ResponseEntity.ok(InventarioDTO.fromModel(inventarioService.obtenerPorIdProducto(id_producto)));
    }

    @GetMapping("/bajo-stock")
    @Operation(summary = "Listar productos con bajo stock", description = "Devuelve los registros de inventario cuyo stock actual está por debajo del mínimo.")
    public ResponseEntity<List<InventarioDTO>> obtenerBajoStock() {
        log.info("GET /api/v1/inventario/bajo-stock - Buscando productos con bajo stock");
        List<Inventario> bajoStock = inventarioService.obtenerBajoStock();
        if (bajoStock.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bajoStock.stream().map(InventarioDTO::fromModel).collect(Collectors.toList()));
    }

    @GetMapping("/stock-suficiente/{id_producto}/{cantidad}")
    @Operation(summary = "Verificar stock suficiente", description = "Indica si hay stock suficiente de un producto para la cantidad solicitada.")
    public ResponseEntity<Boolean> hayStockSuficiente(@PathVariable Integer id_producto, @PathVariable Integer cantidad) {
        log.info("GET /api/v1/inventario/stock-suficiente/{}/{}", id_producto, cantidad);
        return ResponseEntity.ok(inventarioService.hayStockSuficiente(id_producto, cantidad));
    }

    @PostMapping
    @Operation(summary = "Crear un registro de inventario", description = "Registra el stock inicial de un producto, validando su existencia contra ms-producto.")
    public ResponseEntity<InventarioDTO> guardar(@Valid @RequestBody InventarioDTO dto) {
        log.info("POST /api/v1/inventario - Creando inventario");
        return new ResponseEntity<>(InventarioDTO.fromModel(inventarioService.guardar(dto.toModel())), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un registro de inventario", description = "Modifica el stock actual, mínimo y máximo de un producto.")
    public ResponseEntity<InventarioDTO> actualizar(@PathVariable Long id, @Valid @RequestBody InventarioDTO dto) {
        log.info("PUT /api/v1/inventario/{} - Actualizando inventario", id);
        return ResponseEntity.ok(InventarioDTO.fromModel(inventarioService.actualizar(id, dto.toModel())));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un registro de inventario", description = "Elimina el registro de stock por su ID.")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/inventario/{} - Eliminando inventario", id);
        inventarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

