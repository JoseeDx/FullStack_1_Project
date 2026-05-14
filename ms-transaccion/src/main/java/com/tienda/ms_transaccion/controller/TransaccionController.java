package com.tienda.ms_transaccion.controller;

import com.tienda.ms_transaccion.model.Transaccion;
import com.tienda.ms_transaccion.service.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transacciones")
public class TransaccionController {

    private static Logger log = LoggerFactory.getLogger(TransaccionController.class);
    
    @Autowired
    private TransaccionService transaccionService;

    @GetMapping
    public ResponseEntity<List<Transaccion>> findAll() {
        log.info("Obteniendo todas las transacciones");
        List<Transaccion> transacciones = transaccionService.findAll();
        if (transacciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(transacciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaccion> findById(@PathVariable Integer id) {
        log.info("Obteniendo transaccion con ID: {}", id);
        Transaccion transaccion = transaccionService.findById(id);
        return ResponseEntity.ok(transaccion);
    }

    @PostMapping
    public ResponseEntity<Transaccion> save(@RequestBody Transaccion transaccion) {
        log.info("Creando transaccion");
        return new ResponseEntity<>(transaccionService.save(transaccion), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaccion> update(@PathVariable Integer id, @RequestBody Transaccion transaccion) {
        log.info("Actualizando transaccion con ID: {}", id);
        Transaccion existing = transaccionService.findById(id);
        existing.setId_pedido(transaccion.getId_pedido());
        existing.setId_usuario(transaccion.getId_usuario());
        existing.setMetodo_pago(transaccion.getMetodo_pago());
        existing.setMonto_pago(transaccion.getMonto_pago());
        existing.setEstado_pago(transaccion.getEstado_pago());
        existing.setFecha_transaccion(transaccion.getFecha_transaccion());
        return ResponseEntity.ok(transaccionService.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("Eliminando transaccion con ID: {}", id);
        transaccionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
