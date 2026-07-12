package com.tienda.ms_carrito.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tienda.ms_carrito.controller.CarritoItemControllerV2;
import com.tienda.ms_carrito.dto.CarritoItemDTO;

@Component
public class CarritoItemModelAssembler implements RepresentationModelAssembler<CarritoItemDTO, EntityModel<CarritoItemDTO>> {

    @Override
    public EntityModel<CarritoItemDTO> toModel(CarritoItemDTO carritoItem) {
        return EntityModel.of(carritoItem,
                linkTo(methodOn(CarritoItemControllerV2.class).obtenerCarritoItem(carritoItem.getId_carrito())).withSelfRel(),
                linkTo(methodOn(CarritoItemControllerV2.class).listarCarrito()).withRel("carrito"));
    }
}
