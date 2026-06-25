package com.tienda.ms_inventario.dto;

import com.tienda.ms_inventario.model.Inventario;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioDTO {

    private Long id_inventario;

    @NotNull(message = "El id del producto no puede ser nulo")
    private Integer id_producto;

    @NotNull(message = "El stock actual no puede ser nulo")
    @Positive(message = "El stock actual debe ser mayor a 0")
    private Integer stock_actual;

    @NotNull(message = "El stock mínimo no puede ser nulo")
    @Positive(message = "El stock mínimo debe ser mayor a 0")
    private Integer stock_minimo;

    @NotNull(message = "El stock máximo no puede ser nulo")
    @Positive(message = "El stock máximo debe ser mayor a 0")
    private Integer stock_maximo;

    public Inventario toModel() {
        return new Inventario(id_inventario, id_producto, stock_actual, stock_minimo, stock_maximo, null);
    }

    public static InventarioDTO fromModel(Inventario i) {
        if (i == null) return null;
        return new InventarioDTO(i.getId_inventario(), i.getId_producto(), i.getStock_actual(), i.getStock_minimo(), i.getStock_maximo());
    }
}