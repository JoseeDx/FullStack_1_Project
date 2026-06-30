package com.example.ms_descuento.dto; // Ajusta el paquete si es necesario

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DescuentoDTO {

    // El ID no lleva validación estricta porque al crear un nuevo cupón (POST), este campo viene nulo.
    private Integer idDescuento;

    @NotBlank(message = "El código del cupón no puede estar vacío")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "El código solo debe contener letras mayúsculas y números") // Formato válido 
    private String codigoCupon;

    @NotNull(message = "El porcentaje es obligatorio")
    @Min(value = 1, message = "El porcentaje mínimo es 1") // Valores mínimos 
    @Max(value = 100, message = "El porcentaje máximo es 100") // Valores máximos 
    private Double porcentaje;

    @NotNull(message = "La fecha de expiración es obligatoria")
    @Future(message = "La fecha de expiración debe ser una fecha en el futuro") // Fechas válidas 
    private LocalDateTime fechaExpiracion;

    @NotNull(message = "El estado del cupón es obligatorio")
    private Boolean activo;
}