package com.tienda.ms_carrito.controller;

import com.tienda.ms_carrito.dto.CarritoItemDTO;
import com.tienda.ms_carrito.model.CarritoItem;
import com.tienda.ms_carrito.service.CarritoItemService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/carrito")
public class CarritoItemController {

    private static Logger log = LoggerFactory.getLogger(CarritoItemController.class);

    @Autowired
    private CarritoItemService carritoItemService;

    @GetMapping
    public ResponseEntity<List<CarritoItemDTO>> findAll() {
        log.info("GET /carrito - Listando todos los items del carrito");
        List<CarritoItemDTO> items = carritoItemService.findAll()
                .stream()
                .map(CarritoItemDTO::fromModel)
                .toList();
        if (items.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarritoItemDTO> findById(@PathVariable Integer id) {
        log.info("GET /carrito/{} - Buscando item del carrito", id);
        CarritoItem item = carritoItemService.findById(id);
        return ResponseEntity.ok(CarritoItemDTO.fromModel(item));
    }

    @GetMapping("/cliente/{id_cliente}")
    public ResponseEntity<List<CarritoItemDTO>> findByIdCliente(@PathVariable Integer id_cliente) {
        log.info("GET /carrito/cliente/{} - Buscando items del cliente", id_cliente);
        List<CarritoItemDTO> items = carritoItemService.findByIdCliente(id_cliente)
                .stream()
                .map(CarritoItemDTO::fromModel)
                .toList();
        if (items.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(items);
    }

    @GetMapping("/total/{id_cliente}")
    public ResponseEntity<Double> getTotalByIdCliente(@PathVariable Integer id_cliente) {
        log.info("GET /carrito/total/{} - Calculando total del carrito", id_cliente);
        return ResponseEntity.ok(carritoItemService.getTotalByIdCliente(id_cliente));
    }

    @PostMapping
    public ResponseEntity<CarritoItemDTO> save(@Valid @RequestBody CarritoItemDTO dto) {
        log.info("POST /carrito - Agregando item al carrito");
        CarritoItem saved = carritoItemService.save(dto.toModel());
        return new ResponseEntity<>(CarritoItemDTO.fromModel(saved), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarritoItemDTO> update(@PathVariable Integer id, @Valid @RequestBody CarritoItemDTO dto) {
        log.info("PUT /carrito/{} - Actualizando item del carrito", id);
        CarritoItem existing = carritoItemService.findById(id);
        existing.setId_cliente(dto.getId_cliente());
        existing.setId_producto(dto.getId_producto());
        existing.setCantidad(dto.getCantidad());
        existing.setPrecio_unitario(dto.getPrecio_unitario());
        existing.setFecha_agregado(dto.getFecha_agregado());
        return ResponseEntity.ok(CarritoItemDTO.fromModel(carritoItemService.save(existing)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("DELETE /carrito/{} - Eliminando item del carrito", id);
        carritoItemService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
