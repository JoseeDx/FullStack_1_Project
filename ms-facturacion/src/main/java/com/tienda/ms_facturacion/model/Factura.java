package com.tienda.ms_facturacion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "factura")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_factura;

    @Column(nullable = false)
    private Long id_pedido;

    @Column(nullable = false)
    private String rut_cliente;

    @Column(nullable = false)
    private Integer subtotal;

    @Column(nullable = false)
    private Integer iva;

    @Column(nullable = false)
    private Integer total;

    @Column(nullable = false)
    private String estado_factura;

    @Column(nullable = false)
    private LocalDateTime fecha_factura;
}