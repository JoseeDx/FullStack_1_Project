package com.tienda.ms_transaccion.dto;

import com.tienda.ms_transaccion.model.Transaccion; //dto necesita convertirse a model y viceversa
import jakarta.validation.constraints.NotBlank; //validacion
import jakarta.validation.constraints.NotNull; // validacion
import jakarta.validation.constraints.Positive; // validacion
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@Data //getters, setters, equals, hashcode, toString
@NoArgsConstructor //constructor vacio
@AllArgsConstructor //constructor con parametros
@Builder //permite crear un objeto
@Relation(value = "transaccion", collectionRelation = "transaccionList")
public class TransaccionDTO {

    private Integer id_transaccion; //sin validacion pq la base de datos lo crea solo

    @NotNull(message = "El id del pedido no puede ser nulo") 
    private Integer id_pedido;

    @NotNull(message = "El id del cliente no puede ser nulo")
    private Integer id_cliente;

    @NotBlank(message = "El método de pago no puede estar vacío") //notblank para strings
    private String metodo_pago;

    @NotNull(message = "El monto no puede ser nulo")
    @Positive(message = "El monto debe ser mayor a 0") //para validar que el numero sea positivo
    private Double monto_pago;

    @NotBlank(message = "El estado de pago no puede estar vacío")
    private String estado_pago;

    @NotNull(message = "La fecha no puede ser nula")
    private LocalDateTime fecha_transaccion;


    //convierte el dto a entidad para poder guardarlo en la base de datos
    public Transaccion toModel() {
        return new Transaccion(id_transaccion, id_pedido, id_cliente, metodo_pago, monto_pago, estado_pago, fecha_transaccion);
    }

    //convierto la entidad a dto para devolverlo al cliente, static pq no necesita instancia
    public static TransaccionDTO fromModel(Transaccion t) {
        if (t == null) return null;
        return new TransaccionDTO(t.getId_transaccion(), t.getId_pedido(), t.getId_cliente(), t.getMetodo_pago(), t.getMonto_pago(), t.getEstado_pago(), t.getFecha_transaccion());
    }
    
    //La entidad esta ligada a la base de datos, si se expone directamente se pueden filtrar campos sensibles
    // El DTO actua como filtro entre la API y la base de datos
    //se hacen las validaciones aqui para rechazar datos incorrectos antes de que lleguen a la base de datos
}
