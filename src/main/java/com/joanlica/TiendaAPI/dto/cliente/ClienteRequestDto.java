package com.joanlica.TiendaAPI.dto.cliente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record ClienteRequestDto(
        @NotBlank(message = "El nombre es obligatorio.")
        String nombre,
        @NotBlank(message = "El apellido es obligatorio.")
        String apellido,
        @NotBlank(message = "El dni es obligatorio.")
        @Pattern(regexp = "[0-9]{8}[A-Z]", message = "El DNI deben ser 8 números seguido de una letra.")
        String dni
){}