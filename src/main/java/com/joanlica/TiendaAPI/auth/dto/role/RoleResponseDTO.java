package com.joanlica.TiendaAPI.auth.dto.role;


import com.joanlica.TiendaAPI.auth.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;

public record RoleResponseDTO(
        @Schema(description = "ID único del rol.", example = "1")
        Long id,

        @Schema(description = "Nombre completo del rol (como se guarda en BBDD).", example = "ROLE_ADMIN")
        String roleName
) {
    public static RoleResponseDTO from(Role role) {
        return new RoleResponseDTO(role.getId(), role.getRoleName());
    }
}