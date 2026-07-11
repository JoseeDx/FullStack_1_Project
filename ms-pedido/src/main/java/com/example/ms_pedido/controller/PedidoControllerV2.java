package com.example.ms_pedido.controller;

import com.example.ms_pedido.assemblers.PedidoModelAssembler;
import com.example.ms_pedido.dto.PedidoDTO;
import com.example.ms_pedido.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/pedidos")
@Tag(name = "Pedido V2", description = "API de Pedidos con HATEOAS")
public class PedidoControllerV2 {

    @Autowired private PedidoService service;
    @Autowired private PedidoModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Listar todos los pedidos")
    public CollectionModel<EntityModel<PedidoDTO>> listarTodos() {
        var pedidos = service.listarTodos().stream()
                .map(PedidoDTO::fromModel) // Conversión de Entidad a DTO
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(pedidos, linkTo(methodOn(PedidoControllerV2.class).listarTodos()).withSelfRel());
    }

    // Dentro de PedidoControllerV2.java
@GetMapping("/{id}")
public EntityModel<PedidoDTO> obtenerPorId(@PathVariable Long id) {
    // 1. Llamada al servicio (devuelve Entidad)
    var pedido = service.buscarPorId(id); 
    
    // 2. CONVERSIÓN CRÍTICA: Entidad -> DTO
    PedidoDTO dto = PedidoDTO.fromModel(pedido); // Ajusta el nombre de este método si es distinto
    
    return assembler.toModel(dto);
}
    }
