package com.example.ms_descuento.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.ms_descuento.controller.DescuentoControllerV2;
import com.example.ms_descuento.dto.DescuentoDTO;

@Component
public class DescuentoModelAssemblerV2 implements RepresentationModelAssembler<DescuentoDTO, EntityModel<DescuentoDTO>> {

    @Override
    public EntityModel<DescuentoDTO> toModel(DescuentoDTO descuentoDTO) {
        return EntityModel.of(descuentoDTO,
                linkTo(methodOn(DescuentoControllerV2.class).obtenerPorId(descuentoDTO.getIdDescuento())).withSelfRel(),
                linkTo(methodOn(DescuentoControllerV2.class).obtenerTodos()).withRel("descuentos"));
    }
}
