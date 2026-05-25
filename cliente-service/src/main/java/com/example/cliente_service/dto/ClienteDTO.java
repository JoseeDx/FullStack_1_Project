package com.example.cliente_service.dto;

import com.example.cliente_service.model.Cliente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ser una dirección de correo electrónico válida")
    private String correo;

    @NotNull(message = "El ID del rol es obligatorio")
    private Long idRol;

    public Cliente toModel() {
        return new Cliente(id, nombre, correo, null);
    }

    public static ClienteDTO fromModel(Cliente c) {
        if (c == null) return null;
        return new ClienteDTO(c.getId(), c.getNombre(), c.getCorreo(), c.getRol() != null ? c.getRol().getIdRol() : null);
    }
}