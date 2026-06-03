package com.tienda.ms_carrito.dto;

import java.time.LocalDateTime;

import com.tienda.ms_carrito.model.CarritoItem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarritoItemDTO {

    private Integer id_carrito;

    @NotNull(message = "El id del cliente no puede ser nulo")
    private Integer id_cliente;

    @NotNull(message = "El id del producto no puede ser nulo")
    private Integer id_producto;

    @NotNull(message = "La cantidad no puede ser nula")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    @NotNull(message = "El precio no puede ser nulo")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double precio_unitario;

    @NotNull(message = "La fecha no puede ser nula")
    private LocalDateTime fecha_agregado;

    //convierte dto a entidad para poder guardarlo en la base de datos
    public CarritoItem toModel() {
        return new CarritoItem(id_carrito, id_cliente, id_producto, precio_unitario, fecha_agregado, cantidad);
        //contruye el carrito usando los campos del propio dto
    }

    //convierte una entidad a dto para devolverlo al cliente 
    //static no necesita un dto precio, recibe un carrito y lo crea a dto desde cero
    public static CarritoItemDTO fromModel(CarritoItem c) {
        if (c == null) return null; //si llega null devuelve null en ves de lanzar error pa que no explote
        return new CarritoItemDTO(c.getId_carrito(), c.getId_cliente(), c.getId_producto(), c.getCantidad(), c.getPrecio_unitario(), c.getFecha_agregado());
        //construye dto nuevo usando los datos de la entidad recibida
    }

}
