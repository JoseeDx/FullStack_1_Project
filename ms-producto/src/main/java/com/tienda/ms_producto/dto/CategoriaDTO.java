package com.tienda.ms_producto.dto;

import com.tienda.ms_producto.model.Categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaDTO {
    private Integer id_categoria;

    @NotBlank(message = "El nombre de la categoría no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre_categoria;

    public Categoria toModel(){
        return new Categoria(id_categoria, nombre_categoria, null);
    }

    public static CategoriaDTO fromModel(Categoria c){
        if (c == null)
             return null;
        return new CategoriaDTO(c.getId_categoria(),c.getNombre_categoria());
    }

}
