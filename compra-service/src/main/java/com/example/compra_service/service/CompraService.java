package com.example.compra_service.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.compra_service.exception.BadRequestException;
import com.example.compra_service.exception.ResourceNotFoundException;
import com.example.compra_service.model.Compra;
import com.example.compra_service.repository.CompraRepository;

@Service
public class CompraService {

    private static final Logger logger = LoggerFactory.getLogger(CompraService.class);

    private final CompraRepository compraRepository;
    private final WebClient webClient;

    @Value("${api.cliente.exists}")
    private String clientePath;

    @Value("${api.producto.exists}")
    private String productoPath;

    public CompraService(CompraRepository compraRepository, WebClient webClient) {
        this.compraRepository = compraRepository;
        this.webClient = webClient;
    }

    public Compra guardar(Compra compra) {

        logger.info("Iniciando proceso de guardado de compra: idCliente={}, idProducto={}",
                compra.getIdCliente(), compra.getIdProducto());

        Boolean existeCliente;
        Boolean existeProducto;

        try {
            logger.debug("Validando existencia de cliente id={}", compra.getIdCliente());

            existeCliente = webClient.get()
                    .uri(String.format(clientePath, compra.getIdCliente()))
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            logger.debug("Respuesta existencia cliente: {}", existeCliente);

        } catch (Exception e) {
            logger.error("Error al validar cliente id={}", compra.getIdCliente(), e);
            throw new BadRequestException("Error al validar cliente");
        }

        try {
            logger.debug("Validando existencia de producto id={}", compra.getIdProducto());

            existeProducto = webClient.get()
                    .uri(String.format(productoPath, compra.getIdProducto()))
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            logger.debug("Respuesta existencia producto: {}", existeProducto);

        } catch (Exception e) {
            logger.error("Error al validar producto id={}", compra.getIdProducto(), e);
            throw new BadRequestException("Error al validar producto");
        }

        // Validaciones de negocio
        if (existeCliente == null) {
            logger.warn("Respuesta nula al validar cliente id={}", compra.getIdCliente());
            throw new BadRequestException("No se pudo validar la existencia del cliente");
        }

        if (Boolean.FALSE.equals(existeCliente)) {
            logger.warn("Cliente no existe id={}", compra.getIdCliente());
            throw new ResourceNotFoundException("Cliente no existe");
        }

        if (existeProducto == null) {
            logger.warn("Respuesta nula al validar producto id={}", compra.getIdProducto());
            throw new BadRequestException("No se pudo validar la existencia del producto");
        }

        if (Boolean.FALSE.equals(existeProducto)) {
            logger.warn("Producto no existe id={}", compra.getIdProducto());
            throw new ResourceNotFoundException("Producto no existe");
        }

        Compra compraGuardada = compraRepository.save(compra);

        logger.info("Compra guardada exitosamente con id={}", compraGuardada.getId());

        return compraGuardada;
    }

    public List<Compra> listar() {
        logger.info("Listando todas las compras");
        List<Compra> compras = compraRepository.findAll();
        logger.debug("Cantidad de compras encontradas: {}", compras.size());
        return compras;
    }
}