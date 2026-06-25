package com.tienda.ms_inventario.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.tienda.ms_inventario.dto.ProductoDTO;

@FeignClient(name = "ms-producto", url = "http://localhost:8081")
public interface ProductoClient {

    @GetMapping("/api/v1/productos/{id}")
    ProductoDTO obtenerProducto(@PathVariable Integer id);
}