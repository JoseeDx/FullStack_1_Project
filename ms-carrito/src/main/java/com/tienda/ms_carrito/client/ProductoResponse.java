package com.tienda.ms_carrito.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

//Existe para poder aterrizar o traducir los datos del otro microservicio
public class ProductoResponse {
    //Se declaran solo los campos que carrito necesita
    private Integer id_producto;
    private String nombre_producto;
    private Double precio_producto;
    private Boolean activo;
}
