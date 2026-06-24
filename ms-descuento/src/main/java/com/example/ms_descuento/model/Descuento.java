
package com.example.ms_descuento.model; // Ajusta el paquete si el tuyo se llama diferente

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "descuentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Descuento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_descuento")
    private Integer idDescuento;

    @Column(name = "codigo_cupon", unique = true, nullable = false, length = 50)
    private String codigoCupon;

    @Column(nullable = false)
    private Double porcentaje;

    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime fechaExpiracion;

    @Column(nullable = false)
    private Boolean activo;
}

