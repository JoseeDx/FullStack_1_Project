package com.example.ms_envio.model; // Ajusta tu paquete

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "envios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_envio")
    private Integer idEnvio;

    @Column(name = "id_pedido", nullable = false)
    private Integer idPedido;

    @Column(nullable = false, length = 200)
    private String direccion;

    @Column(nullable = false, length = 100)
    private String ciudad;

    @Column(nullable = false, length = 50)
    private String estado; // PREPARANDO, EN_RUTA, ENTREGADO

    @Column(name = "fecha_despacho")
    private LocalDateTime fechaDespacho;
}