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
import com.tienda.ms_producto.model.Categoria;
import com.tienda.ms_producto.service.CategoriaService;

@RestController
@RequestMapping("categorias/v2")
public class CategoriaControllerV2 {

    private final CategoriaService categoriaService;
    private final CategoriaModelAssembler assembler;
    private static final Logger logger = LoggerFactory.getLogger(CategoriaControllerV2.class);

    public CategoriaControllerV2(CategoriaService categoriaService, CategoriaModelAssembler assembler) {
        this.categoriaService = categoriaService;
        this.assembler = assembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<Categoria>> listarCategorias() {
        logger.info("V2 GET /categorias - Listando categorias");
        List<EntityModel<Categoria>> categorias = categoriaService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(categorias, linkTo(methodOn(CategoriaControllerV2.class).listarCategorias()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Categoria> obtenerCategoria(@PathVariable Integer id) {
        logger.info("V2 GET /categorias/{} - Obteniendo categoria", id);
        Categoria categoria = categoriaService.findById(id);
        return assembler.toModel(categoria);
    }
}
