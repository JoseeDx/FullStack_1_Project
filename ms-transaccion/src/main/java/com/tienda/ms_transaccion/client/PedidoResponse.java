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

    //este es el objeto que recibe la respuesta de ms-pedido (como queremos que llegue el json)
}
