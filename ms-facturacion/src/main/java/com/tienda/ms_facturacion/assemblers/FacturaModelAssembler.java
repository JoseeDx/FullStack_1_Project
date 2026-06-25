package com.tienda.ms_facturacion.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.tienda.ms_facturacion.controller.FacturaControllerV2;
import com.tienda.ms_facturacion.model.Factura;

@Component
public class FacturaModelAssembler implements RepresentationModelAssembler<Factura, EntityModel<Factura>> {

    @Override
    public EntityModel<Factura> toModel(Factura factura) {
        return EntityModel.of(factura,
                linkTo(methodOn(FacturaControllerV2.class).obtenerFactura(factura.getId_factura())).withSelfRel(),
                linkTo(methodOn(FacturaControllerV2.class).listarFacturas()).withRel("facturas"));
    }
}