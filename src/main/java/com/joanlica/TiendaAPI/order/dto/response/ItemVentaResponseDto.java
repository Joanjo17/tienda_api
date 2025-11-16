package com.joanlica.TiendaAPI.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ItemVentaResponseDto(
        @Schema(description = "ID único del ítem de venta.", example = "1")
        Long id,

        @Schema(description = "Nombre del producto vendido.", example = "Laptop Pro 15")
        String nombre_producto,

        @Schema(description = "Marca del producto vendido.", example = "TechCorp")
        String marca,

        @Schema(description = "Precio del producto al momento de la venta.", example = "149.99")
        Double precioUnitario,

        @Schema(description = "Cantidad vendida de este producto.", example = "2.0")
        Double cantidad
) {
}