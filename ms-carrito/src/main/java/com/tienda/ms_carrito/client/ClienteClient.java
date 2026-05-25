package com.tienda.ms_carrito.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cliente-service", url = "http://localhost:9091")
public interface ClienteClient {

    @GetMapping("api/v1/clientes/{id}/exists")
    Boolean existeCliente(@PathVariable Long id);
}
