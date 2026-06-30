package com.example.ms_descuento.controller;

import com.example.ms_descuento.dto.DescuentoDTO;
import com.example.ms_descuento.service.DescuentoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/descuentos")
public class DescuentoController {

    @Autowired
    private DescuentoService service;

    @PostMapping
    public ResponseEntity<DescuentoDTO> crear(@Valid @RequestBody DescuentoDTO dto) {
        log.info("Petición REST para crear descuento recibida");
        return new ResponseEntity<>(service.crear(dto), HttpStatus.CREATED);
    }

    // HATEOAS implementado solo en GET según rúbrica
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<DescuentoDTO>> obtenerPorId(@PathVariable Integer id) {
        log.info("Petición REST para obtener descuento por ID recibida");
        DescuentoDTO dto = service.obtenerPorId(id);
        EntityModel<DescuentoDTO> recurso = EntityModel.of(dto);
        recurso.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).obtenerPorId(id)).withSelfRel());
        return ResponseEntity.ok(recurso);
    }

    // HATEOAS implementado solo en GET según rúbrica
    @GetMapping
    public ResponseEntity<List<EntityModel<DescuentoDTO>>> obtenerTodos() {
        log.info("Petición REST para obtener todos los descuentos recibida");
        List<EntityModel<DescuentoDTO>> recursos = service.obtenerTodos().stream()
                .map(dto -> {
                    EntityModel<DescuentoDTO> recurso = EntityModel.of(dto);
                    recurso.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).obtenerPorId(dto.getIdDescuento())).withSelfRel());
                    return recurso;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(recursos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DescuentoDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody DescuentoDTO dto) {
        log.info("Petición REST para actualizar descuento recibida");
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        log.info("Petición REST para eliminar descuento recibida");
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/aplicar")
    public ResponseEntity<Map<String, Double>> aplicarDescuento(@RequestParam String codigo, @RequestParam Double total) {
        Double nuevoTotal = service.calcularDescuento(codigo, total);
        return ResponseEntity.ok(Map.of("totalConDescuento", nuevoTotal));
    }
}