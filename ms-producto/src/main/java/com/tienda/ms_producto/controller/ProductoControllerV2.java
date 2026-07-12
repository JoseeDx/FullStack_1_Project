package com.tienda.ms_producto.controller;

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

import com.tienda.ms_producto.assemblers.ProductoModelAssembler;
import com.tienda.ms_producto.dto.ProductoDTO;
import com.tienda.ms_producto.model.Producto;
import com.tienda.ms_producto.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("productos/v2")
@Tag(name = "Productos V2", description = "Operaciones de productos con soporte HATEOAS")
public class ProductoControllerV2 {

    private final ProductoService productoService;
    private final ProductoModelAssembler assembler;
    private static final Logger logger = LoggerFactory.getLogger(ProductoControllerV2.class);

    public ProductoControllerV2(ProductoService productoService, ProductoModelAssembler assembler) {
        this.productoService = productoService;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Listar productos (HATEOAS)", description = "Retorna la colección de productos con sus enlaces HATEOAS.")
    public CollectionModel<EntityModel<ProductoDTO>> listarProductos() {
        logger.info("V2 GET /productos - Listando productos");
        List<EntityModel<ProductoDTO>> productos = productoService.findAll().stream()
                .map(ProductoDTO::fromModel)
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(productos, linkTo(methodOn(ProductoControllerV2.class).listarProductos()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un producto por ID (HATEOAS)", description = "Retorna un producto específico con su enlace correspondiente.")
    public EntityModel<ProductoDTO> obtenerProducto(@PathVariable Integer id) {
        logger.info("V2 GET /productos/{} - Obteniendo producto", id);
        Producto producto = productoService.findById(id);
        return assembler.toModel(ProductoDTO.fromModel(producto));
    }
}
