package com.example.ms_pedido.dto;

import com.example.ms_pedido.model.Pedido;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoDTO {
    
    private Long idPedido;

    // Se agrega la restricción de que no sea nula, igual que en ms-transaccion
    @NotNull(message = "La fecha del pedido no puede ser nula")
    @PastOrPresent(message = "La fecha del pedido no puede ser en el futuro")
    private LocalDateTime fechaPedido;

    public Pedido toModel() {
        return new Pedido(idPedido, fechaPedido);
    }

    public static PedidoDTO fromModel(Pedido p) {
        if (p == null) return null;
        return new PedidoDTO(p.getIdPedido(), p.getFechaPedido());
    }
}