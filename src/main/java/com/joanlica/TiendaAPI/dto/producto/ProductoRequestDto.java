package com.joanlica.TiendaAPI.dto.producto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductoRequestDto(
        @NotBlank(message = "El nombre es obligatorio.")
        String nombre,
        @NotBlank(message = "La marca es obligatoria.")
        String marca,
        @NotNull(message = "El costo no puede ser nulo.")
        @PositiveOrZero(message = "El costo no puede ser negativo.")
        Double costo,
        @NotNull(message = "La cantidad no puede ser nula")
        @PositiveOrZero(message = "La cantidad no puede ser negativa.")
        Double cantidad_disponible
){}