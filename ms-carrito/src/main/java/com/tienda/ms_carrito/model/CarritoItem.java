package com.tienda.ms_carrito.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "carrito_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarritoItem {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer id_carrito;

    @Column(nullable = false)
    private Integer id_cliente;

    @Column(nullable = false)
    private Integer id_producto;

    @Column(nullable = false)
    private Double precio_unitario;

    @Column(nullable = false)
    private LocalDateTime fecha_agregado;

    @Column(nullable = false)
    private Integer cantidad;

}
