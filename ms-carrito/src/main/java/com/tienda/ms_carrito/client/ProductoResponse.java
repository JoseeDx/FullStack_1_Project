package com.tienda.ms_carrito.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponse {
    private Integer id_producto;
    private String nombre_producto;
    private Double precio_producto;
    private Boolean activo;
}
