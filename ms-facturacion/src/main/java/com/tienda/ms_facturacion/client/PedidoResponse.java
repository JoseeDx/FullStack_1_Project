package com.tienda.ms_facturacion.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponse {
    private Long id_pedido;
    private String estado_pedido;
}