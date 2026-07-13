package com.example.ms_envio.controller;

import com.example.ms_envio.dto.EnvioDTO;
import com.example.ms_envio.service.EnvioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/envios")
@Tag(name = "Envíos", description = "Gestión del ciclo de vida logístico de los envíos de pedidos")
public class EnvioController {

    @Autowired
    private EnvioService service;

    @PostMapping
    @Operation(summary = "Crear un envío", description = "Registra un nuevo envío para un pedido, con estado inicial PREPARANDO.")
    public ResponseEntity<EnvioDTO> crear(@Valid @RequestBody EnvioDTO dto) {
        log.info("Petición REST recibida: Crear envío");
        return new ResponseEntity<>(service.crear(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un envío por ID", description = "Devuelve el detalle de un envío específico.")
    public ResponseEntity<EnvioDTO> obtenerPorId(@PathVariable Integer id) {
        log.info("Petición REST recibida: Obtener envío por ID");
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar todos los envíos", description = "Devuelve todos los envíos registrados en el sistema.")
    public ResponseEntity<List<EnvioDTO>> obtenerTodos() {
        log.info("Petición REST recibida: Obtener todos los envíos");
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar el estado de un envío", description = "Avanza el estado del envío (PREPARANDO, EN_RUTA, ENTREGADO). Al pasar a EN_RUTA se registra la fecha de despacho.")
    public ResponseEntity<EnvioDTO> actualizarEstado(@PathVariable Integer id, @RequestParam String estado) {
        log.info("Petición REST recibida: Actualizar estado de envío");
        return ResponseEntity.ok(service.actualizarEstado(id, estado));
    }
}