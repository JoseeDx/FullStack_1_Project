package com.example.cliente_service.assemblers;

import com.example.cliente_service.controller.ClienteControllerV2;
import com.example.cliente_service.dto.ClienteDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ClienteModelAssembler implements RepresentationModelAssembler<ClienteDTO, EntityModel<ClienteDTO>> {

    @Override
    public EntityModel<ClienteDTO> toModel(ClienteDTO clienteDTO) {
        // Aquí agregamos los links hipermedia (HATEOAS) a la respuesta
        return EntityModel.of(clienteDTO,
                linkTo(methodOn(ClienteControllerV2.class).obtenerClientePorId(clienteDTO.getId())).withSelfRel(),
                linkTo(methodOn(ClienteControllerV2.class).listarTodos()).withRel("clientes"));
    }
}