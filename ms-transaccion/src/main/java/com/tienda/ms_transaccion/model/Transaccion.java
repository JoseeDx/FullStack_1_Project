package com.tienda.ms_transaccion.model;

import java.time.LocalDateTime; //maneja fechas y horas

import jakarta.persistence.*; //importa todas las anotaciones jpa(@entity @table @id, etc)
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//getters, setters, constructores, etc


@Entity //entidad, representa una tabla en la base de datos
@Table(name = "transaccion") //nombre exacto en la base de datos
@Data //genera automaticamente getters y setters para todos los campos, toString(), equals() y hashCode()
@NoArgsConstructor //constructor vacio
@AllArgsConstructor //constructor con todos los campos
public class Transaccion {

    @Id //clave primaria de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY) //el valor se autoincrementa en la base de datos
    private Integer id_transaccion;

    @Column(nullable = false) //referencia al id del pedido, campo obligatorio
    private Integer id_pedido;

    @Column(nullable = false) //usuario que realiza la transaccion, obligatorio
    private Integer id_cliente;

    @Column(nullable = false) //metodo de pago obligatorio
    private String metodo_pago;

    @Column(nullable = false) //double para valores decimales, monto de pago obligatorio
    private Double monto_pago;

    @Column(nullable = false) //estado de pago obligatorio
    private String estado_pago;

    @Column(nullable = false) //fecha y hora exacta del pago, obligatorio
    private LocalDateTime fecha_transaccion;
}
    

