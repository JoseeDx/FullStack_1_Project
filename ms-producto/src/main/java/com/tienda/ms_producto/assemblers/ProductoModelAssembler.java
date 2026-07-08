package com.tienda.ms_producto.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tienda.ms_producto.controller.ProductoControllerV2;
import com.tienda.ms_producto.model.Producto;

@Component
public class ProductoModelAssembler implements RepresentationModelAssembler<Producto, EntityModel<Producto>> {

    @Override
    public EntityModel<Producto> toModel(Producto producto) {
        return EntityModel.of(producto,
                linkTo(methodOn(ProductoControllerV2.class).obtenerProducto(producto.getId_producto())).withSelfRel(),
                linkTo(methodOn(ProductoControllerV2.class).listarProductos()).withRel("productos"));
    }
}
