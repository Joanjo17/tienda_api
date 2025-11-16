package com.joanlica.TiendaAPI.auth.dto.role;


import com.joanlica.TiendaAPI.auth.model.Role;

public record RoleResponseDTO(
        Long id,
        String roleName
) {
    public static RoleResponseDTO from(Role role) {
        return new RoleResponseDTO(role.getId(), role.getRoleName());
    }
}