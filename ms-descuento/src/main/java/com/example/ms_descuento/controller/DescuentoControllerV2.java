package com.example.ms_descuento.controller;

import com.example.ms_descuento.assemblers.DescuentoModelAssemblerV2;
import com.example.ms_descuento.dto.DescuentoDTO;
import com.example.ms_descuento.service.DescuentoService;
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
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v2/descuentos")
@Tag(name = "Descuentos V2", description = "Versión 2 de la API de descuentos. (Preparada para futuras evoluciones de contrato)")
public class DescuentoControllerV2 {

    @Autowired
    private DescuentoService service;

    @Autowired
    private DescuentoModelAssemblerV2 assemblerV2;

    @PostMapping
    @Operation(summary = "Crear un cupón de descuento (V2)", description = "Registra un nuevo cupón de descuento utilizando la interfaz V2.")
    public ResponseEntity<DescuentoDTO> crear(@Valid @RequestBody DescuentoDTO dto) {
        log.info("V2 - Petición REST recibida: Crear descuento");
        return new ResponseEntity<>(service.crear(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un descuento por ID (V2)", description = "Devuelve el detalle de un cupón junto con sus enlaces HATEOAS de la versión V2.")
    public ResponseEntity<EntityModel<DescuentoDTO>> obtenerPorId(@PathVariable Integer id) {
        log.info("V2 - Petición REST recibida: Obtener descuento por ID");
        DescuentoDTO dto = service.obtenerPorId(id);
        return ResponseEntity.ok(assemblerV2.toModel(dto));
    }

    @GetMapping
    @Operation(summary = "Listar todos los descuentos (V2)", description = "Devuelve todos los cupones de descuento registrados en el sistema en formato V2.")
    public ResponseEntity<List<EntityModel<DescuentoDTO>>> obtenerTodos() {
        log.info("V2 - Petición REST recibida: Obtener todos los descuentos");
        List<EntityModel<DescuentoDTO>> recursos = service.obtenerTodos().stream()
                .map(assemblerV2::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(recursos);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un descuento (V2)", description = "Modifica los datos de un cupón de descuento existente utilizando la versión V2.")
    public ResponseEntity<DescuentoDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody DescuentoDTO dto) {
        log.info("V2 - Petición REST recibida: Actualizar descuento");
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un descuento (V2)", description = "Elimina un cupón de descuento por su ID utilizando la ruta V2.")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.info("V2 - Petición REST recibida: Eliminar descuento");
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/aplicar")
    @Operation(summary = "Aplicar un cupón a un monto (V2)", description = "Calcula el monto final tras aplicar el porcentaje de descuento de un cupón vigente utilizando la API V2.")
    public ResponseEntity<Map<String, Double>> aplicarDescuento(@RequestParam String codigo, @RequestParam Double total) {
        log.info("V2 - Petición REST recibida: Aplicar descuento");
        Double nuevoTotal = service.calcularDescuento(codigo, total);
        return ResponseEntity.ok(Map.of("totalConDescuento", nuevoTotal));
    }
}
