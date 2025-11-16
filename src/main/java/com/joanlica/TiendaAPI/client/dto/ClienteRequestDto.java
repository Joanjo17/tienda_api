package com.joanlica.TiendaAPI.client.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record ClienteRequestDto(
        @Schema(
                description = "Nombre(s) del cliente.",
                example = "Joan Josep",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El nombre es obligatorio.")
        String nombre,

        @Schema(
                description = "Apellido(s) del cliente.",
                example = "Lira Casanova",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El apellido es obligatorio.")
        String apellido,

        @Schema(
                description = "DNI del cliente (8 números y 1 letra mayúscula).",
                example = "12345678A",
                pattern = "[0-9]{8}[A-Z]",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El DNI es obligatorio.")
        @Pattern(regexp = "[0-9]{8}[A-Z]", message = "El DNI deben ser 8 números seguidos de una letra mayúscula.")
        String dni
) {
}