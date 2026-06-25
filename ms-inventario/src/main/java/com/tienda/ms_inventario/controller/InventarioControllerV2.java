package com.tienda.ms_inventario.controller;


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
import com.tienda.ms_inventario.assemblers.InventarioModelAssembler;
import com.tienda.ms_inventario.model.Inventario;
import com.tienda.ms_inventario.service.InventarioService;

@RestController
@RequestMapping("inventario/v2")
public class InventarioControllerV2 {

    private final InventarioService inventarioService;
    private final InventarioModelAssembler assembler;
    private static final Logger logger = LoggerFactory.getLogger(InventarioControllerV2.class);

    public InventarioControllerV2(InventarioService inventarioService, InventarioModelAssembler assembler) {
        this.inventarioService = inventarioService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Inventario>> listarInventario() {
        logger.info("V2 GET /inventario - Listando inventario");
        List<EntityModel<Inventario>> inventarios = inventarioService.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(inventarios, linkTo(methodOn(InventarioControllerV2.class).listarInventario()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Inventario> obtenerInventario(@PathVariable Long id) {
        logger.info("V2 GET /inventario/{} - Obteniendo inventario", id);
        Inventario inventario = inventarioService.obtenerPorId(id);
        return assembler.toModel(inventario);
    }
}
