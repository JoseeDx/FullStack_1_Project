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

import com.tienda.ms_producto.assemblers.CategoriaModelAssembler;
import com.tienda.ms_producto.dto.CategoriaDTO;
import com.tienda.ms_producto.model.Categoria;
import com.tienda.ms_producto.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("categorias/v2")
@Tag(name = "Categorías V2", description = "Operaciones de categorías con soporte HATEOAS")
public class CategoriaControllerV2 {

    private final CategoriaService categoriaService;
    private final CategoriaModelAssembler assembler;
    private static final Logger logger = LoggerFactory.getLogger(CategoriaControllerV2.class);

    public CategoriaControllerV2(CategoriaService categoriaService, CategoriaModelAssembler assembler) {
        this.categoriaService = categoriaService;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Listar categorías (HATEOAS)", description = "Retorna la colección de categorías con sus enlaces HATEOAS.")
    public CollectionModel<EntityModel<CategoriaDTO>> listarCategorias() {
        logger.info("V2 GET /categorias - Listando categorias");
        List<EntityModel<CategoriaDTO>> categorias = categoriaService.findAll().stream()
                .map(CategoriaDTO::fromModel)
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(categorias, linkTo(methodOn(CategoriaControllerV2.class).listarCategorias()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una categoría por ID (HATEOAS)", description = "Retorna una categoría específica con su enlace correspondiente.")
    public EntityModel<CategoriaDTO> obtenerCategoria(@PathVariable Integer id) {
        logger.info("V2 GET /categorias/{} - Obteniendo categoria", id);
        Categoria categoria = categoriaService.findById(id);
        return assembler.toModel(CategoriaDTO.fromModel(categoria));
    }
}
