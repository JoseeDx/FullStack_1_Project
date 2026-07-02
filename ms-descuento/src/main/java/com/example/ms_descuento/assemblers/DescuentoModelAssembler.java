package com.example.ms_descuento.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.ms_descuento.controller.DescuentoController;
import com.example.ms_descuento.dto.DescuentoDTO;

@Component
public class DescuentoModelAssembler implements RepresentationModelAssembler<DescuentoDTO, EntityModel<DescuentoDTO>> {

    @Override
    public EntityModel<DescuentoDTO> toModel(DescuentoDTO descuentoDTO) {
        return EntityModel.of(descuentoDTO,
                linkTo(methodOn(DescuentoController.class).obtenerPorId(descuentoDTO.getIdDescuento())).withSelfRel(),
                linkTo(methodOn(DescuentoController.class).obtenerTodos()).withRel("descuentos"));
    }
}
