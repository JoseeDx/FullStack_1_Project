package com.example.ms_resena.dto;

import com.example.ms_resena.model.Resena;

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
    private Long idProducto;
    private Long idCliente;
    private Integer calificacion;
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