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
import com.tienda.ms_inventario.dto.InventarioDTO;
import com.tienda.ms_inventario.model.Inventario;
import com.tienda.ms_inventario.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("inventario/v2")
@Tag(name = "Inventario V2", description = "Operaciones de inventario con soporte HATEOAS")
public class InventarioControllerV2 {

    private final InventarioService inventarioService;
    private final InventarioModelAssembler assembler;
    private static final Logger logger = LoggerFactory.getLogger(InventarioControllerV2.class);

    public InventarioControllerV2(InventarioService inventarioService, InventarioModelAssembler assembler) {
        this.inventarioService = inventarioService;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Listar inventario (HATEOAS)", description = "Retorna la colección de inventario con sus enlaces HATEOAS.")
    public CollectionModel<EntityModel<InventarioDTO>> listarInventario() {
        logger.info("V2 GET /inventario - Listando inventario");
        List<EntityModel<InventarioDTO>> inventarios = inventarioService.listar().stream()
                .map(InventarioDTO::fromModel)
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(inventarios, linkTo(methodOn(InventarioControllerV2.class).listarInventario()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener inventario por ID (HATEOAS)", description = "Retorna un registro de inventario específico con su enlace correspondiente.")
    public EntityModel<InventarioDTO> obtenerInventario(@PathVariable Long id) {
        logger.info("V2 GET /inventario/{} - Obteniendo inventario", id);
        Inventario inventario = inventarioService.obtenerPorId(id);
        return assembler.toModel(InventarioDTO.fromModel(inventario));
    }
}
