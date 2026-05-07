package com.example.compra_service.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.compra_service.dto.CompraDTO;
import com.example.compra_service.model.Compra;
import com.example.compra_service.service.CompraService;

@RestController
@RequestMapping("/compras")
public class CompraController {

    private static final Logger logger = LoggerFactory.getLogger(CompraController.class);

    private final CompraService compraService;

    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @PostMapping
    public ResponseEntity<CompraDTO> crearCompra(@RequestBody CompraDTO compraDto) {

        logger.info("POST /compras - idCliente={}, idProducto={}",
                compraDto.getIdCliente(), compraDto.getIdProducto());

        Compra nuevaCompra = compraService.guardar(compraDto.toModel());

        logger.info("Compra creada exitosamente id={}", nuevaCompra.getId());

        return ResponseEntity.ok(CompraDTO.fromModel(nuevaCompra));
    }

    @GetMapping
    public ResponseEntity<List<CompraDTO>> listarCompras() {

        logger.info("GET /compras");

        List<Compra> compras = compraService.listar();

        logger.debug("Cantidad de compras obtenidas: {}", compras.size());

        List<CompraDTO> dtos = compras.stream()
                .map(CompraDTO::fromModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}