package com.tienda.ms_producto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity // Entidad debe tener campo identificador primario
@Table(name= "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_producto;

    @Column(length = 150, nullable=false)
    private String nombre_producto;

    @Column(length = 500, nullable =false )
    private String descripcion_producto;

    @Column(nullable = false)
    private Double precio_producto; //Tengo que arreglar todo esto pero es para tener una idea
    
    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private Boolean activo = true;

}
