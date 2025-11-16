package com.joanlica.TiendaAPI.client.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ClienteResponseDto(
        @Schema(description = "ID único del cliente.", example = "1")
        Long id_cliente,

        @Schema(description = "Nombre(s) del cliente.", example = "Joan Josep")
        String nombre,

        @Schema(description = "Apellido(s) del cliente.", example = "Lira Casanova")
        String apellido,

        @Schema(description = "DNI del cliente.", example = "12345678A")
        String dni
) {
}