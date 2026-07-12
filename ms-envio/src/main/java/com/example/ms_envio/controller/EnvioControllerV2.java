package com.example.ms_envio.controller;

import com.example.ms_envio.assemblers.EnvioModelAssemblerV2;
import com.example.ms_envio.dto.EnvioDTO;
import com.example.ms_envio.service.EnvioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v2/envios")
@Tag(name = "Envíos V2", description = "Versión 2 de la API de envíos. (Preparada para futuras evoluciones de contrato)")
public class EnvioControllerV2 {

    @Autowired
    private EnvioService service;

    // Inyectamos el Assembler específico de la V2 para que el HATEOAS no apunte a la V1
    @Autowired
    private EnvioModelAssemblerV2 assemblerV2;

    @PostMapping
    @Operation(summary = "Crear un envío (V2)", description = "Registra un nuevo envío para un pedido utilizando la interfaz V2.")
    public ResponseEntity<EnvioDTO> crear(@Valid @RequestBody EnvioDTO dto) {
        log.info("V2 - Petición REST recibida: Crear envío");
        return new ResponseEntity<>(service.crear(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un envío por ID (V2)", description = "Devuelve el detalle de un envío junto con sus enlaces HATEOAS que apuntan a la V2.")
    public ResponseEntity<EntityModel<EnvioDTO>> obtenerPorId(@PathVariable Integer id) {
        log.info("V2 - Petición REST recibida: Obtener envío por ID");
        EnvioDTO dto = service.obtenerPorId(id);
        return ResponseEntity.ok(assemblerV2.toModel(dto));
    }

    @GetMapping
    @Operation(summary = "Listar todos los envíos (V2)", description = "Devuelve todos los envíos registrados en el sistema, formateados para la V2.")
    public ResponseEntity<List<EntityModel<EnvioDTO>>> obtenerTodos() {
        log.info("V2 - Petición REST recibida: Obtener todos los envíos");
        List<EntityModel<EnvioDTO>> recursos = service.obtenerTodos().stream()
                .map(assemblerV2::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(recursos);
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar el estado de un envío (V2)", description = "Avanza el estado del envío a través de la ruta V2.")
    public ResponseEntity<EnvioDTO> actualizarEstado(@PathVariable Integer id, @RequestParam String estado) {
        log.info("V2 - Petición REST recibida: Actualizar estado de envío");
        return ResponseEntity.ok(service.actualizarEstado(id, estado));
    }
}