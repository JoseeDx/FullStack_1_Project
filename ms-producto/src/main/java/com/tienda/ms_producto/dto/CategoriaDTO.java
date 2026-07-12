package com.tienda.ms_producto.dto;

import com.tienda.ms_producto.model.Categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(value = "categoria", collectionRelation = "categoriaList")
public class CategoriaDTO {
    private Integer id_categoria;

    @NotBlank(message = "El nombre de la categoría no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre_categoria;

    private Boolean activo;

    public Categoria toModel(){
        return new Categoria(id_categoria, nombre_categoria, activo);
    }

    public static CategoriaDTO fromModel(Categoria c){
        if (c == null)
             return null;
        return new CategoriaDTO(c.getId_categoria(), c.getNombre_categoria(), c.getActivo());
    }

}
