package com.example.ms_envio.controller;

import com.example.ms_envio.dto.EnvioDTO;
import com.example.ms_envio.service.EnvioService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/envios")
public class EnvioController {

    @Autowired
    private EnvioService service;

    @PostMapping
    public ResponseEntity<EnvioDTO> crear(@Valid @RequestBody EnvioDTO dto) {
        log.info("Petición REST recibida: Crear envío");
        return new ResponseEntity<>(service.crear(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<EnvioDTO>> obtenerPorId(@PathVariable Integer id) {
        log.info("Petición REST recibida: Obtener envío por ID");
        EnvioDTO dto = service.obtenerPorId(id);
        EntityModel<EnvioDTO> recurso = EntityModel.of(dto);
        recurso.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).obtenerPorId(id)).withSelfRel());
        return ResponseEntity.ok(recurso);
    }

    @GetMapping
    public ResponseEntity<List<EntityModel<EnvioDTO>>> obtenerTodos() {
        log.info("Petición REST recibida: Obtener todos los envíos");
        List<EntityModel<EnvioDTO>> recursos = service.obtenerTodos().stream()
                .map(dto -> {
                    EntityModel<EnvioDTO> recurso = EntityModel.of(dto);
                    recurso.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).obtenerPorId(dto.getIdEnvio())).withSelfRel());
                    return recurso;
                }).collect(Collectors.toList());
        return ResponseEntity.ok(recursos);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<EnvioDTO> actualizarEstado(@PathVariable Integer id, @RequestParam String estado) {
        log.info("Petición REST recibida: Actualizar estado de envío");
        return ResponseEntity.ok(service.actualizarEstado(id, estado));
    }
}