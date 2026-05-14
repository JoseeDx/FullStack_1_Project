package com.tienda.ms_transaccion.controller;

import com.tienda.ms_transaccion.dto.TransaccionDTO;
import com.tienda.ms_transaccion.model.Transaccion;
import com.tienda.ms_transaccion.service.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transacciones")
public class TransaccionController {

    private static Logger log = LoggerFactory.getLogger(TransaccionController.class);

    @Autowired
    private TransaccionService transaccionService;

    @GetMapping
    public ResponseEntity<List<TransaccionDTO>> findAll() {
        log.info("Obteniendo todas las transacciones");
        List<TransaccionDTO> transacciones = transaccionService.findAll()
                .stream()
                .map(TransaccionDTO::fromModel)
                .toList();
        if (transacciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(transacciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransaccionDTO> findById(@PathVariable Integer id) {
        log.info("Obteniendo transaccion con ID: {}", id);
        Transaccion transaccion = transaccionService.findById(id);
        return ResponseEntity.ok(TransaccionDTO.fromModel(transaccion));
    }

    @PostMapping
    public ResponseEntity<TransaccionDTO> save(@Valid @RequestBody TransaccionDTO dto) {
        log.info("Creando transaccion");
        Transaccion saved = transaccionService.save(dto.toModel());
        return new ResponseEntity<>(TransaccionDTO.fromModel(saved), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransaccionDTO> update(@PathVariable Integer id, @Valid @RequestBody TransaccionDTO dto) {
        log.info("Actualizando transaccion con ID: {}", id);
        Transaccion existing = transaccionService.findById(id);
        existing.setId_pedido(dto.getId_pedido());
        existing.setId_usuario(dto.getId_usuario());
        existing.setMetodo_pago(dto.getMetodo_pago());
        existing.setMonto_pago(dto.getMonto_pago());
        existing.setEstado_pago(dto.getEstado_pago());
        existing.setFecha_transaccion(dto.getFecha_transaccion());
        return ResponseEntity.ok(TransaccionDTO.fromModel(transaccionService.save(existing)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("Eliminando transaccion con ID: {}", id);
        transaccionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
