package com.joanlica.TiendaAPI.auth.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginUserRequestDTO(
        @Schema(
                description = "Nombre de usuario único.",
                example = "jperez",
                minLength = 3,
                maxLength = 50,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El nombre de usuario es obligatorio")
        String username,

        @Schema(
                description = "Contraseña del usuario.",
                example = "PasswOrd!123",
                minLength = 8,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {
}