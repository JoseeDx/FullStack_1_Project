package com.tienda.ms_transaccion.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "transaccion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_transaccion;

    @Column(nullable = false)
    private Integer id_pedido;

    @Column(nullable = false)
    private Integer id_usuario;

    @Column(nullable = false)
    private String metodo_pago;

    @Column(nullable = false)
    private Double monto_pago;

    @Column(nullable = false)
    private String estado_pago;

    @Column(nullable = false)
    private LocalDateTime fecha_transaccion;
}
    

