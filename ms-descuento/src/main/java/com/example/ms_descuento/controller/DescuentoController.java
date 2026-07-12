package com.example.ms_descuento.controller;

import com.example.ms_descuento.assemblers.DescuentoModelAssembler;
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
@RequestMapping("/api/v1/descuentos")
@Tag(name = "Descuentos", description = "Gestión de cupones de descuento y cálculo de montos con descuento aplicado")
public class DescuentoController {

    @Autowired
    private DescuentoService service;

    @Autowired
    private DescuentoModelAssembler assembler;

    @PostMapping
    @Operation(summary = "Crear un cupón de descuento", description = "Registra un nuevo cupón con su porcentaje y fecha de expiración.")
    public ResponseEntity<DescuentoDTO> crear(@Valid @RequestBody DescuentoDTO dto) {
        log.info("Petición REST para crear descuento recibida");
        return new ResponseEntity<>(service.crear(dto), HttpStatus.CREATED);
    }

    // HATEOAS implementado solo en GET según rúbrica
    @GetMapping("/{id}")
    @Operation(summary = "Obtener un descuento por ID", description = "Devuelve el detalle de un cupón de descuento junto con sus enlaces HATEOAS.")
    public ResponseEntity<EntityModel<DescuentoDTO>> obtenerPorId(@PathVariable Integer id) {
        log.info("Petición REST para obtener descuento por ID recibida");
        DescuentoDTO dto = service.obtenerPorId(id);
        return ResponseEntity.ok(assembler.toModel(dto));
    }

    // HATEOAS implementado solo en GET según rúbrica
    @GetMapping
    @Operation(summary = "Listar todos los descuentos", description = "Devuelve todos los cupones de descuento registrados.")
    public ResponseEntity<List<EntityModel<DescuentoDTO>>> obtenerTodos() {
        log.info("Petición REST para obtener todos los descuentos recibida");
        List<EntityModel<DescuentoDTO>> recursos = service.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(recursos);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un descuento", description = "Modifica los datos de un cupón de descuento existente.")
    public ResponseEntity<DescuentoDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody DescuentoDTO dto) {
        log.info("Petición REST para actualizar descuento recibida");
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un descuento", description = "Elimina un cupón de descuento por su ID.")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.info("Petición REST para eliminar descuento recibida");
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/aplicar")
    @Operation(summary = "Aplicar un cupón a un monto", description = "Calcula el monto final tras aplicar el porcentaje de descuento de un cupón vigente.")
    public ResponseEntity<Map<String, Double>> aplicarDescuento(@RequestParam String codigo, @RequestParam Double total) {
        Double nuevoTotal = service.calcularDescuento(codigo, total);
        return ResponseEntity.ok(Map.of("totalConDescuento", nuevoTotal));
    }
}