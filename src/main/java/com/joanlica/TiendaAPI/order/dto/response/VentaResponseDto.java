package com.joanlica.TiendaAPI.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

public record VentaResponseDto(
        @Schema(description = "ID (código) único de la venta.", example = "5001")
        Long codigo_venta,

        @Schema(description = "Fecha en que se realizó la venta.", example = "2025-11-16")
        LocalDate fechaVenta,

        @Schema(description = "Monto total de la venta.", example = "299.99")
        Double precioTotal,

        @Schema(description = "Lista detallada de los productos vendidos en esta venta.")
        List<ItemVentaResponseDto> listaItemVenta,

        @Schema(
                description = "Información resumida del cliente que realizó la compra.",
                implementation = ClienteVentaInfoDto.class
        )
        ClienteVentaInfoDto cliente,

        @Schema(description = "Estado actual de la venta.", example = "COMPLETADA")
        String estado
) {
}