package com.tienda.ms_inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {
    private Integer id_producto;
    private String nombre_producto;
    private Boolean activo;
}