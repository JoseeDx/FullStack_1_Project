package com.tienda.ms_inventario.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tienda.ms_inventario.controller.InventarioControllerV2;
import com.tienda.ms_inventario.model.Inventario;

@Component
public class InventarioModelAssembler implements RepresentationModelAssembler<Inventario, EntityModel<Inventario>> {

    @Override
    public EntityModel<Inventario> toModel(Inventario inventario) {
        return EntityModel.of(inventario,
                linkTo(methodOn(InventarioControllerV2.class).obtenerInventario(inventario.getId_inventario())).withSelfRel(),
                linkTo(methodOn(InventarioControllerV2.class).listarInventario()).withRel("inventario"));

        
    }


}
