package com.example.cliente_service.controller;

import com.example.cliente_service.assemblers.ClienteModelAssembler;
import com.example.cliente_service.dto.ClienteDTO;
import com.example.cliente_service.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/clientes") // OJO: Esta es la versión 2
@Tag(name = "Cliente V2", description = "Operaciones de clientes con soporte HATEOAS")
public class ClienteControllerV2 {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteModelAssembler assembler;

    @GetMapping
@Operation(summary = "Listar todos los clientes (HATEOAS)", description = "Retorna la colección de clientes con sus enlaces HATEOAS.")
public CollectionModel<EntityModel<ClienteDTO>> listarTodos() {
    List<EntityModel<ClienteDTO>> clientes = clienteService.listar().stream() // O el nombre de tu método
            // 1. Si tu servicio devuelve Cliente (entidad), primero lo conviertes a DTO usando tu método estándar:
            .map(cliente -> ClienteDTO.fromModel(cliente)) 
            // 2. Ahora que es un DTO, se lo pasas al assembler HATEOAS:
            .map(assembler::toModel)
            .collect(Collectors.toList());

    return CollectionModel.of(clientes,
            linkTo(methodOn(ClienteControllerV2.class).listarTodos()).withSelfRel()); // Ajusta el nombre aquí también si cambió
}

    @GetMapping("/{id}")
@Operation(summary = "Obtener un cliente por ID (HATEOAS)", description = "Retorna un cliente específico con su link correspondiente.")
public EntityModel<ClienteDTO> obtenerClientePorId(@PathVariable Long id) {
    // 1. Buscamos el cliente usando el servicio (Devuelve la Entidad Cliente)
    // ⚠️ RECUERDA: Cambia "obtenerPorId" si tu servicio usa otro nombre como "buscarPorId" o "obtenerCliente"
    var clienteEntidad = clienteService.buscarPorId(id); 
    
    // 2. Convertimos la Entidad a DTO usando el método de tu proyecto
    ClienteDTO clienteDTO = ClienteDTO.fromModel(clienteEntidad);
    
    // 3. Le pasamos el DTO ya convertido al ensamblador de HATEOAS
    return assembler.toModel(clienteDTO);
}
}