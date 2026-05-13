package com.example.cliente_service.dto;

import com.example.cliente_service.model.Cliente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteDTO {
    private Long id;
    private String nombre;
    private String correo;
    private Long idRol;
    public Cliente toModel() {
        return new Cliente(id, nombre, correo, null);
    }

    public static ClienteDTO fromModel(Cliente c) {
        if (c == null) return null;
        return new ClienteDTO(c.getId(), c.getNombre(), c.getCorreo(), c.getRol() != null ? c.getRol().getIdRol() : null);
    }
}
