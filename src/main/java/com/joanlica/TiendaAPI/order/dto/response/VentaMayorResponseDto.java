package com.joanlica.TiendaAPI.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record VentaMayorResponseDto(
        @Schema(description = "ID de la venta con mayor importe.", example = "5008")
        Long codigo_venta,

        @Schema(description = "Monto total de la venta.", example = "12500.00")
        Double total,

        @Schema(description = "Cantidad total de productos en esa venta.", example = "15.0")
        Double cantidad_productos,

        @Schema(description = "Nombre del cliente de la venta.", example = "Joan")
        String nombre_cliente,

        @Schema(description = "Apellido del cliente de la venta.", example = "Lira")
        String apellido_cliente
) {
}