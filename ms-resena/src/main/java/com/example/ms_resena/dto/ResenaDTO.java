package com.example.ms_resena.dto;

import com.example.ms_resena.model.Resena;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResenaDTO {
    private Long idResena;

    @NotNull(message = "El ID del producto es obligatorio")
    private Long idProducto;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long idCliente;

    @NotNull(message = "La calificación es obligatoria")
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    private Integer calificacion;

    @Size(max = 500, message = "El comentario no puede superar los 500 caracteres")
    private String comentario;

    private LocalDateTime fechaCreacion;

    // Convertir de Entidad a DTO
    public static ResenaDTO fromModel(Resena resena) {
        return ResenaDTO.builder()
                .idResena(resena.getIdResena())
                .idProducto(resena.getIdProducto())
                .idCliente(resena.getIdCliente())
                .calificacion(resena.getCalificacion())
                .comentario(resena.getComentario())
                .fechaCreacion(resena.getFechaCreacion())
                .build();
    }

    // Convertir de DTO a Entidad
    public Resena toModel() {
        Resena resena = new Resena();
        resena.setIdProducto(this.idProducto);
        resena.setIdCliente(this.idCliente);
        resena.setCalificacion(this.calificacion);
        resena.setComentario(this.comentario);
        return resena;
    }
}