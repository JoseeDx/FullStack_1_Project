package com.example.ms_resena.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.ms_resena.assemblers.ResenaModelAssembler;
import com.example.ms_resena.dto.ResenaDTO;
import com.example.ms_resena.service.ResenaService;

@RestController
@RequestMapping("resenas/v2")
@Tag(name = "Reseñas V2", description = "Endpoints v2 con HATEOAS para la gestión de reseñas")
public class ResenaControllerV2 {

    private final ResenaService resenaService;
    private final ResenaModelAssembler assembler;
    private static final Logger logger = LoggerFactory.getLogger(ResenaControllerV2.class);

    public ResenaControllerV2(ResenaService resenaService, ResenaModelAssembler assembler) {
        this.resenaService = resenaService;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Listar reseñas", description = "Obtiene todas las reseñas disponibles como recursos HATEOAS.")
    public CollectionModel<EntityModel<ResenaDTO>> listarResenas() {
        logger.info("V2 GET /resenas - Listando reseñas");
        List<EntityModel<ResenaDTO>> resenas = resenaService.listar().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(resenas, linkTo(methodOn(ResenaControllerV2.class).listarResenas()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reseña", description = "Obtiene la reseña especificada por su ID y la devuelve como recurso HATEOAS.")
    public EntityModel<ResenaDTO> obtenerResena(@PathVariable Long id) {
        logger.info("V2 GET /resenas/{} - Obteniendo reseña", id);
        ResenaDTO resenaDTO = resenaService.obtenerPorId(id);
        return assembler.toModel(resenaDTO);
    }
}
