package com.tienda.ms_producto.dto;

import com.tienda.ms_producto.model.Categoria;
import com.tienda.ms_producto.model.Producto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoDTO {

    private Integer id_producto;

    private Boolean activo;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 150, message = "Máximo 150 caracteres")
    private String nombre_producto;

    @NotBlank(message = "La descripcion no puede estar vacía")
    @Size(max = 500, message = "Máximo 500 caracteres")
    private String descripcion_producto;

    @NotNull(message = "El precio no puede ser nulo")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double precio_producto;

    @NotNull(message = "La categoría no puede ser nula")
    private Integer id_categoria; 

    public Producto toModel() {
        Categoria cat = new Categoria();
        cat.setId_categoria(id_categoria);
        return new Producto(id_producto, nombre_producto, descripcion_producto, precio_producto, cat, true);                                                                                 
    }

    public static ProductoDTO fromModel(Producto p){
        if (p == null)
             return null;
        return new ProductoDTO(p.getId_producto(),p.getActivo(),p.getNombre_producto(),p.getDescripcion_producto(),p.getPrecio_producto(),p.getCategoria() != null ? p.getCategoria().getId_categoria() : null);
    }

}

