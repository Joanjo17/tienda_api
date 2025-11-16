package com.joanlica.TiendaAPI.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

// Este DTO es para ser usado dentro de VentaResponseDto
public record ClienteVentaInfoDto(
        @Schema(description = "ID del cliente.", example = "1")
        Long idCliente,

        @Schema(description = "Nombre del cliente.", example = "Joan")
        String nombre,

        @Schema(description = "Apellido del cliente.", example = "Lira")
        String apellido
) {
}