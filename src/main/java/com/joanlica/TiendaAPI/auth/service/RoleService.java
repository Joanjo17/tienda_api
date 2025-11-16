package com.joanlica.TiendaAPI.auth.service;

import com.joanlica.TiendaAPI.auth.dto.role.CreateRoleRequestDTO;
import com.joanlica.TiendaAPI.auth.dto.role.RoleResponseDTO;
import com.joanlica.TiendaAPI.auth.dto.role.UpdateRoleRequestDTO;

import java.util.List;

public interface RoleService {
    List<RoleResponseDTO> findAll();

    RoleResponseDTO findById(Long id);

    RoleResponseDTO findByName(String name);

    RoleResponseDTO save(CreateRoleRequestDTO newRole);

    void deleteById(Long id);

    RoleResponseDTO update(Long id, UpdateRoleRequestDTO roleUpdate);
}