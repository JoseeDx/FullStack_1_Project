package com.tienda.ms_carrito.controller;

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

import com.tienda.ms_carrito.assemblers.CarritoItemModelAssembler;
import com.tienda.ms_carrito.dto.CarritoItemDTO;
import com.tienda.ms_carrito.model.CarritoItem;
import com.tienda.ms_carrito.service.CarritoItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("carrito/v2")
@Tag(name = "Carrito V2", description = "Operaciones de carrito con soporte HATEOAS")
public class CarritoItemControllerV2 {

    private final CarritoItemService carritoItemService;
    private final CarritoItemModelAssembler assembler;
    private static final Logger logger = LoggerFactory.getLogger(CarritoItemControllerV2.class);

    public CarritoItemControllerV2(CarritoItemService carritoItemService, CarritoItemModelAssembler assembler) {
        this.carritoItemService = carritoItemService;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Listar items del carrito (HATEOAS)", description = "Retorna la colección de items del carrito con sus enlaces HATEOAS.")
    public CollectionModel<EntityModel<CarritoItemDTO>> listarCarrito() {
        logger.info("V2 GET /carrito - Listando items del carrito");
        List<EntityModel<CarritoItemDTO>> items = carritoItemService.findAll().stream()
                .map(CarritoItemDTO::fromModel)
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(items, linkTo(methodOn(CarritoItemControllerV2.class).listarCarrito()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un item del carrito por ID (HATEOAS)", description = "Retorna un item del carrito específico con su enlace correspondiente.")
    public EntityModel<CarritoItemDTO> obtenerCarritoItem(@PathVariable Integer id) {
        logger.info("V2 GET /carrito/{} - Obteniendo item del carrito", id);
        CarritoItem carritoItem = carritoItemService.findById(id);
        return assembler.toModel(CarritoItemDTO.fromModel(carritoItem));
    }
}
