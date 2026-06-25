package com.tienda.ms_inventario.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_inventario;

    @Column(nullable = false)
    private Integer id_producto;

    @Column(nullable = false)
    private Integer stock_actual;

    @Column(nullable = false)
    private Integer stock_minimo;

    @Column(nullable = false)
    private Integer stock_maximo;

    @Column(nullable = false)
    private LocalDateTime fecha_actualizacion;    
}
