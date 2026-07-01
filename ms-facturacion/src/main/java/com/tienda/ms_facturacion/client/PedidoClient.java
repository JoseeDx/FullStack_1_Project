package com.tienda.ms_facturacion.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-pedido", url = "http://ms-pedido:9094")
public interface PedidoClient {

    @GetMapping("/api/v1/pedidos/{id}")
    PedidoResponse obtenerPedido(@PathVariable Long id);
}