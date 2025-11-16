package com.joanlica.TiendaAPI.auth.dto.role;

import jakarta.validation.constraints.NotBlank;

public record CreateRoleRequestDTO(
        @NotBlank
        String roleName
) {
}