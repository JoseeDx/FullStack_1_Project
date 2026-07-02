package com.example.ms_resena.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.ms_resena.controller.ResenaControllerV2;
import com.example.ms_resena.dto.ResenaDTO;

@Component
public class ResenaModelAssembler implements RepresentationModelAssembler<ResenaDTO, EntityModel<ResenaDTO>> {

    @Override
    public EntityModel<ResenaDTO> toModel(ResenaDTO resenaDTO) {
        return EntityModel.of(resenaDTO,
                linkTo(methodOn(ResenaControllerV2.class).obtenerResena(resenaDTO.getIdResena())).withSelfRel(),
                linkTo(methodOn(ResenaControllerV2.class).listarResenas()).withRel("resenas"));
    }
}
