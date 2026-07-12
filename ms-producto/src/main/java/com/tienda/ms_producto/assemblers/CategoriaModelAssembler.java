package com.tienda.ms_producto.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tienda.ms_producto.controller.CategoriaControllerV2;
import com.tienda.ms_producto.dto.CategoriaDTO;

@Component
public class CategoriaModelAssembler implements RepresentationModelAssembler<CategoriaDTO, EntityModel<CategoriaDTO>> {

    @Override
    public EntityModel<CategoriaDTO> toModel(CategoriaDTO categoria) {
        return EntityModel.of(categoria,
                linkTo(methodOn(CategoriaControllerV2.class).obtenerCategoria(categoria.getId_categoria())).withSelfRel(),
                linkTo(methodOn(CategoriaControllerV2.class).listarCategorias()).withRel("categorias"));
    }
}
