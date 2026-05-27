package com.tienda.ms_transaccion.dto;

import com.tienda.ms_transaccion.model.Transaccion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransaccionDTO {

private Integer id_transaccion;

    @NotNull(message = "El id del pedido no puede ser nulo")
    private Integer id_pedido;

    @NotNull(message = "El id del cliente no puede ser nulo")
    private Integer id_cliente;

    @NotBlank(message = "El método de pago no puede estar vacío")
    private String metodo_pago;

    @NotNull(message = "El monto no puede ser nulo")
    @Positive(message = "El monto debe ser mayor a 0")
    private Double monto_pago;

    @NotBlank(message = "El estado de pago no puede estar vacío")
    private String estado_pago;

    @NotNull(message = "La fecha no puede ser nula")
    private LocalDateTime fecha_transaccion;

    public Transaccion toModel() {
        return new Transaccion(id_transaccion, id_pedido, id_cliente, metodo_pago, monto_pago, estado_pago, fecha_transaccion);
    }

    public static TransaccionDTO fromModel(Transaccion t) {
        if (t == null) return null;
        return new TransaccionDTO(t.getId_transaccion(), t.getId_pedido(), t.getId_cliente(), t.getMetodo_pago(), t.getMonto_pago(), t.getEstado_pago(), t.getFecha_transaccion());
    }
    
}
