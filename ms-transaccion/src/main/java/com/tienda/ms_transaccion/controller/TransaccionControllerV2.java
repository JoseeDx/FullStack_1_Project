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
import com.tienda.ms_transaccion.model.Transaccion;
import com.tienda.ms_transaccion.service.TransaccionService;

@RestController
@RequestMapping("transacciones/v2")
public class TransaccionControllerV2 {

    private final TransaccionService transaccionService;
    private final TransaccionModelAssembler assembler;
    private static final Logger logger = LoggerFactory.getLogger(TransaccionControllerV2.class);

    public TransaccionControllerV2(TransaccionService transaccionService, TransaccionModelAssembler assembler) {
        this.transaccionService = transaccionService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Transaccion>> listarTransacciones() {
        logger.info("V2 GET /transacciones - Listando transacciones");
        List<EntityModel<Transaccion>> transacciones = transaccionService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(transacciones, linkTo(methodOn(TransaccionControllerV2.class).listarTransacciones()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Transaccion> obtenerTransaccion(@PathVariable Integer id) {
        logger.info("V2 GET /transacciones/{} - Obteniendo transaccion", id);
        Transaccion transaccion = transaccionService.findById(id);
        return assembler.toModel(transaccion);
    }
}
