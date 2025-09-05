package com.joanlica.TiendaAPI.dto.cliente;

import jakarta.validation.constraints.NotBlank;

public record ClienteRequestDto(
        @NotBlank(message = "El nombre es obligatorio.")
        String nombre,
        @NotBlank(message = "El apellido es obligatorio.")
        String apellido,
        @NotBlank(message = "El dni es obligatorio.")
        String dni
){}