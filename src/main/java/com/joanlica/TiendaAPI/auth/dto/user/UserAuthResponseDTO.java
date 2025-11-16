package com.joanlica.TiendaAPI.auth.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

public record UserAuthResponseDTO(
        @Schema(
                description = "ID único del usuario en la base de datos.",
                example = "1"
        )
        Long id,

        @Schema(
                description = "Token JWT generado para la sesión del usuario.",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        )
        String token,

        @Schema(
                description = "Lista de roles asignados al usuario.",
                example = "[\"ADMIN\",\"USER\"]"
        )
        Set<String> rolesList
) {
}