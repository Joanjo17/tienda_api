package com.joanlica.TiendaAPI.auth.service.implementation;


import com.joanlica.TiendaAPI.auth.dto.role.CreateRoleRequestDTO;
import com.joanlica.TiendaAPI.auth.dto.role.RoleResponseDTO;
import com.joanlica.TiendaAPI.auth.dto.role.UpdateRoleRequestDTO;
import com.joanlica.TiendaAPI.auth.model.Role;
import com.joanlica.TiendaAPI.auth.repository.RoleRepository;
import com.joanlica.TiendaAPI.auth.service.RoleService;
import com.joanlica.TiendaAPI.core.exception.RoleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<RoleResponseDTO> findAll() {

        return roleRepository.findAll().stream()
                .map(RoleResponseDTO::from)
                .collect(Collectors.toList());
    }

    @Override
    public RoleResponseDTO findById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + id));
        return RoleResponseDTO.from(role);
    }

    @Override
    public RoleResponseDTO findByName(String name) {
        Role role = roleRepository.findByRoleName(name.toUpperCase())
                .orElseThrow(() -> new RoleNotFoundException("Role not found with name: " + name));
        return RoleResponseDTO.from(role);
    }

    @Override
    public RoleResponseDTO save(CreateRoleRequestDTO role) {
        Role newRole = new Role();
        newRole.setRoleName(role.roleName().trim().toUpperCase());
        Role roleSaved = roleRepository.save(newRole);
        return RoleResponseDTO.from(roleSaved);
    }

    @Override
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public RoleResponseDTO update(Long id, UpdateRoleRequestDTO roleUpdate) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + id));
        if (roleUpdate.roleName() != null) role.setRoleName(roleUpdate.roleName().trim().toUpperCase());

        Role roleSaved = roleRepository.save(role);

        return RoleResponseDTO.from(roleSaved);
    }
}