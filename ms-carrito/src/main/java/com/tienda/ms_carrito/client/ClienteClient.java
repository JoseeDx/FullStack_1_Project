package com.tienda.ms_carrito.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//Cliente http que apunta a cliente-service indicando su nombre y url donde esta corriendo
//Interface pq feign implementa todo automaticamente
@FeignClient(name = "cliente-service", url = "http://localhost:9091")
public interface ClienteClient {

    //Se declara metodo get para obtener los campos que necesitamos
    @GetMapping("api/v1/clientes/{id}/exists")
    Boolean existeCliente(@PathVariable Long id);//Recibe long pq el cliente usa long
    //Retorna un boolean ya que no necesita clase especial, la respuesta es true o false, no un objeto complejo
}
