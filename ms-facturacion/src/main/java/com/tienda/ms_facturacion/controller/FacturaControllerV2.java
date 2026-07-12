package com.tienda.ms_facturacion.controller;

import com.tienda.ms_facturacion.assemblers.FacturaModelAssembler;
import com.tienda.ms_facturacion.dto.FacturaDTO;
import com.tienda.ms_facturacion.model.Factura;
import com.tienda.ms_facturacion.service.FacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("facturas/v2")
@Tag(name = "Facturas V2", description = "Operaciones de facturas con soporte HATEOAS")
public class FacturaControllerV2 {

    private final FacturaService facturaService;
    private final FacturaModelAssembler assembler;
    private static final Logger logger = LoggerFactory.getLogger(FacturaControllerV2.class);

    public FacturaControllerV2(FacturaService facturaService, FacturaModelAssembler assembler) {
        this.facturaService = facturaService;
        this.assembler = assembler;
    }

    @GetMapping
    @Operation(summary = "Listar facturas (HATEOAS)", description = "Retorna la colección de facturas con sus enlaces HATEOAS.")
    public CollectionModel<EntityModel<FacturaDTO>> listarFacturas() {
        logger.info("V2 GET /facturas - Listando facturas");
        List<EntityModel<FacturaDTO>> facturas = facturaService.listar().stream()
                .map(FacturaDTO::fromModel)
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return CollectionModel.of(facturas, linkTo(methodOn(FacturaControllerV2.class).listarFacturas()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una factura por ID (HATEOAS)", description = "Retorna una factura específica con su enlace correspondiente.")
    public EntityModel<FacturaDTO> obtenerFactura(@PathVariable Long id) {
        logger.info("V2 GET /facturas/{} - Obteniendo factura", id);
        Factura factura = facturaService.obtenerPorId(id);
        return assembler.toModel(FacturaDTO.fromModel(factura));
    }
}