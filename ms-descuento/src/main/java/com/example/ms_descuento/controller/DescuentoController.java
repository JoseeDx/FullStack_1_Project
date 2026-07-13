package com.example.ms_descuento.controller;

import com.example.ms_descuento.dto.DescuentoDTO;
import com.example.ms_descuento.service.DescuentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/api/v1/descuentos")
@Tag(name = "Descuentos", description = "Gestión de cupones de descuento y cálculo de montos con descuento aplicado")
public class DescuentoController {

    @Autowired
    private DescuentoService service;

    @PostMapping
    @Operation(summary = "Crear un cupón de descuento", description = "Registra un nuevo cupón con su porcentaje y fecha de expiración.")
    public ResponseEntity<DescuentoDTO> crear(@Valid @RequestBody DescuentoDTO dto) {
        log.info("Petición REST para crear descuento recibida");
        return new ResponseEntity<>(service.crear(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un descuento por ID", description = "Devuelve el detalle de un cupón de descuento.")
    public ResponseEntity<DescuentoDTO> obtenerPorId(@PathVariable Integer id) {
        log.info("Petición REST para obtener descuento por ID recibida");
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping
    @Operation(summary = "Listar todos los descuentos", description = "Devuelve todos los cupones de descuento registrados.")
    public ResponseEntity<List<DescuentoDTO>> obtenerTodos() {
        log.info("Petición REST para obtener todos los descuentos recibida");
        return ResponseEntity.ok(service.obtenerTodos());
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