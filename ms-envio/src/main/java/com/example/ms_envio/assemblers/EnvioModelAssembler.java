package com.example.ms_envio.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.ms_envio.controller.EnvioController;
import com.example.ms_envio.dto.EnvioDTO;

@Component
public class EnvioModelAssembler implements RepresentationModelAssembler<EnvioDTO, EntityModel<EnvioDTO>> {

    @Override
    public EntityModel<EnvioDTO> toModel(EnvioDTO envioDTO) {
        return EntityModel.of(envioDTO,
                linkTo(methodOn(EnvioController.class).obtenerPorId(envioDTO.getIdEnvio())).withSelfRel(),
                linkTo(methodOn(EnvioController.class).obtenerTodos()).withRel("envios"));
    }
}
