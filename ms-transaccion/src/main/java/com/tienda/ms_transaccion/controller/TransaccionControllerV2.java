package com.tienda.ms_transaccion.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tienda.ms_transaccion.assemblers.TransaccionModelAssembler;
import com.tienda.ms_transaccion.dto.TransaccionDTO;
import com.tienda.ms_transaccion.model.Transaccion;
import com.tienda.ms_transaccion.service.TransaccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("transacciones/v2")
@Tag(name = "Transacciones V2", description = "Operaciones de transacciones con soporte HATEOAS")
public class TransaccionControllerV2 {

    private final TransaccionService transaccionService;
    private final TransaccionModelAssembler assembler;
    private static final Logger logger = LoggerFactory.getLogger(TransaccionControllerV2.class);

    public TransaccionControllerV2(TransaccionService transaccionService, TransaccionModelAssembler assembler) {
        this.transaccionService = transaccionService;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Listar transacciones (HATEOAS)", description = "Retorna la colección de transacciones con sus enlaces HATEOAS.")
    public CollectionModel<EntityModel<TransaccionDTO>> listarTransacciones() {
        logger.info("V2 GET /transacciones - Listando transacciones");
        List<EntityModel<TransaccionDTO>> transacciones = transaccionService.findAll().stream()
                .map(TransaccionDTO::fromModel)
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(transacciones, linkTo(methodOn(TransaccionControllerV2.class).listarTransacciones()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una transacción por ID (HATEOAS)", description = "Retorna una transacción específica con su enlace correspondiente.")
    public EntityModel<TransaccionDTO> obtenerTransaccion(@PathVariable Integer id) {
        logger.info("V2 GET /transacciones/{} - Obteniendo transaccion", id);
        Transaccion transaccion = transaccionService.findById(id);
        return assembler.toModel(TransaccionDTO.fromModel(transaccion));
    }
}
