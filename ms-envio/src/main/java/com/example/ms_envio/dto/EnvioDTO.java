package com.example.ms_envio.dto; // Ajusta tu paquete

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnvioDTO {

    private Integer idEnvio;

    @NotNull(message = "El ID del pedido es obligatorio")
    @Min(value = 1, message = "El ID del pedido debe ser mayor a 0")
    private Integer idPedido;

    @NotBlank(message = "La dirección no puede estar vacía")
    @Size(min = 10, max = 200, message = "La dirección debe tener entre 10 y 200 caracteres")
    private String direccion;

    @NotBlank(message = "La ciudad no puede estar vacía")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "La ciudad solo puede contener letras y espacios")
    private String ciudad;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    @PastOrPresent(message = "La fecha de despacho no puede ser en el futuro")
    private LocalDateTime fechaDespacho;
}