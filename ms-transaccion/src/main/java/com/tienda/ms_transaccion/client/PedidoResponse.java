package com.tienda.ms_transaccion.client;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponse {
    private Long idPedido;
    private LocalDateTime fechaPedido;

}
