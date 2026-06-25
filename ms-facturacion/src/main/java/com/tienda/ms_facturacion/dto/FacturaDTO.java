package com.tienda.ms_facturacion.dto;

import com.tienda.ms_facturacion.model.Factura;
import jakarta.validation.constraints.NotBlank;
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
public class FacturaDTO {

    private Long id_factura;

    @NotNull(message = "El id del pedido no puede ser nulo")
    private Long id_pedido;

    @NotBlank(message = "El RUT no puede estar vacío")
    private String rut_cliente;

    @NotNull(message = "El subtotal no puede ser nulo")
    @Positive(message = "El subtotal debe ser mayor a 0")
    private Integer subtotal;

    public Factura toModel() {
        return new Factura(id_factura, id_pedido, rut_cliente, subtotal, null, null, null, null);
    }

    public static FacturaDTO fromModel(Factura f) {
        if (f == null) return null;
        return new FacturaDTO(f.getId_factura(), f.getId_pedido(), f.getRut_cliente(), f.getSubtotal());
    }
}