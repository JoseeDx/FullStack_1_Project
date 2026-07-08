package com.tienda.ms_carrito.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tienda.ms_carrito.controller.CarritoItemControllerV2;
import com.tienda.ms_carrito.model.CarritoItem;

@Component
public class CarritoItemModelAssembler implements RepresentationModelAssembler<CarritoItem, EntityModel<CarritoItem>> {

    @Override
    public EntityModel<CarritoItem> toModel(CarritoItem carritoItem) {
        return EntityModel.of(carritoItem,
                linkTo(methodOn(CarritoItemControllerV2.class).obtenerCarritoItem(carritoItem.getId_carrito())).withSelfRel(),
                linkTo(methodOn(CarritoItemControllerV2.class).listarCarrito()).withRel("carrito"));
    }
}
