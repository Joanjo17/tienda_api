package com.joanlica.TiendaAPI.auth.controller;

import com.joanlica.TiendaAPI.auth.dto.role.CreateRoleRequestDTO;
import com.joanlica.TiendaAPI.auth.dto.role.RoleResponseDTO;
import com.joanlica.TiendaAPI.auth.dto.role.UpdateRoleRequestDTO;
import com.joanlica.TiendaAPI.auth.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;


    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getAllRoles() {
        List<RoleResponseDTO> roles = roleService.findAll();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getRoleById(@PathVariable Long id) {
        RoleResponseDTO role = roleService.findById(id);
        return ResponseEntity.ok(role);
    }

    @GetMapping("/name/{roleName}")
    public ResponseEntity<RoleResponseDTO> getRoleByName(@PathVariable String roleName) {
        RoleResponseDTO role = roleService.findByName(roleName);
        return ResponseEntity.ok(role);
    }

    @PostMapping
    public ResponseEntity<RoleResponseDTO> createRole(@RequestBody CreateRoleRequestDTO role) {
        RoleResponseDTO newRole = roleService.save(role);
        return ResponseEntity.ok(newRole);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> updateRole(@PathVariable Long id,
                                                      @RequestBody UpdateRoleRequestDTO role) {
        RoleResponseDTO newRole = roleService.update(id, role);
        return ResponseEntity.ok(newRole);
    }
}