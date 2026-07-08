package com.tienda.ms_transaccion.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tienda.ms_transaccion.controller.TransaccionControllerV2;
import com.tienda.ms_transaccion.model.Transaccion;

@Component
public class TransaccionModelAssembler implements RepresentationModelAssembler<Transaccion, EntityModel<Transaccion>> {

    @Override
    public EntityModel<Transaccion> toModel(Transaccion transaccion) {
        return EntityModel.of(transaccion,
                linkTo(methodOn(TransaccionControllerV2.class).obtenerTransaccion(transaccion.getId_transaccion())).withSelfRel(),
                linkTo(methodOn(TransaccionControllerV2.class).listarTransacciones()).withRel("transacciones"));
    }
}
