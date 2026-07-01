package com.tienda.ms_transaccion.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-pedido", url = "http://ms-pedido:9094") //crea un cliente que apunta a ms-pedido, hace las peticiones auto
public interface PedidoClient { //interface pq feign la implementa automaticamente

    @GetMapping("/api/v1/pedidos/{id}")
    PedidoResponse findById(@PathVariable Long id); //le indica que dato queremos 
    //no tiene cuerpo pq feign decide como hacerlo, solo se declaran los metodos
    //@PathVariable: indica que el id debe insertarse dentro de la url

}
