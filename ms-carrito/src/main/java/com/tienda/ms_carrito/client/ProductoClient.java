package com.tienda.ms_carrito.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//Cliente HTTP que apunta a ms-producto, indica nombre de microservicio y url donde esta corriendo
//Interface pq feign implementa todo automaticamente
@FeignClient(name = "ms-producto", url = "http://localhost:8081")
public interface ProductoClient {

    //Se declara el metodo get 
    @GetMapping("api/v1/productos/{id}")
    ProductoResponse findById(@PathVariable Integer id);
    //retorna ProductoResponse con los datos que necesita carrito de producto

}
