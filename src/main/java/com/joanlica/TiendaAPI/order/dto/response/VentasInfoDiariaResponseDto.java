package com.joanlica.TiendaAPI.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record VentasInfoDiariaResponseDto(
        @Schema(description = "Monto total acumulado en la fecha.", example = "15890.50")
        Double monto,

        @Schema(description = "Número total de ventas en la fecha.", example = "42")
        Integer cantidad_ventas
) {
}