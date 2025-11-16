package com.joanlica.TiendaAPI.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductoRequestDto(
        @Schema(
                description = "Nombre del producto.",
                example = "Laptop Pro 15",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El nombre es obligatorio.")
        String nombre,

        @Schema(
                description = "Marca o fabricante del producto.",
                example = "TechCorp",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "La marca es obligatoria.")
        String marca,

        @Schema(
                description = "Precio de costo del producto.",
                example = "1250.99",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "El costo no puede ser nulo.")
        @PositiveOrZero(message = "El costo no puede ser negativo.")
        Double costo,

        @Schema(
                description = "Cantidad de stock disponible.",
                example = "50.0",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "La cantidad no puede ser nula")
        @PositiveOrZero(message = "La cantidad no puede ser negativa.")
        Double cantidad_disponible
) {
}