package com.example.ms_pedido.assemblers;

import com.example.ms_pedido.controller.PedidoControllerV2;
import com.example.ms_pedido.dto.PedidoDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PedidoModelAssembler implements RepresentationModelAssembler<PedidoDTO, EntityModel<PedidoDTO>> {

    @Override
    public EntityModel<PedidoDTO> toModel(PedidoDTO dto) {
        // Asegúrate de que los nombres de los métodos coincidan con los de PedidoControllerV2
        return EntityModel.of(dto,
                linkTo(methodOn(PedidoControllerV2.class).obtenerPorId(dto.getIdPedido())).withSelfRel(),
                linkTo(methodOn(PedidoControllerV2.class).listarTodos()).withRel("pedidos"));
    }
}