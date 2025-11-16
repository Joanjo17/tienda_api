package com.joanlica.TiendaAPI.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemVentaRequestsDto(
        @Schema(
                description = "Cantidad del producto a vender (debe ser mayor a 0).",
                example = "2.0",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "La cantidad no puede ser nula")
        @Positive(message = "La cantidad debe ser un número positivo")
        Double cantidad,

        @Schema(
                description = "ID (codigo_producto) del producto a vender.",
                example = "101",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "El id del producto no puede ser nulo")
        Long codigo_producto
) {
}