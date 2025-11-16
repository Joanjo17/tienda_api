package com.joanlica.TiendaAPI.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductoResponseDto(
        @Schema(description = "ID único (código) del producto.", example = "101")
        Long codigo_producto,

        @Schema(description = "Nombre del producto.", example = "Laptop Pro 15")
        String nombre,

        @Schema(description = "Marca del producto.", example = "TechCorp")
        String marca,

        @Schema(description = "Precio de costo del producto.", example = "1250.99")
        Double costo,

        @Schema(description = "Cantidad actual en stock.", example = "50.0")
        Double cantidad_disponible
) {
}