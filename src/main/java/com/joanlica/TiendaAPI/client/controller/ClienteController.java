package com.joanlica.TiendaAPI.client.controller;

import com.joanlica.TiendaAPI.client.dto.ClienteRequestDto;
import com.joanlica.TiendaAPI.client.dto.ClienteResponseDto;
import com.joanlica.TiendaAPI.client.service.ClienteService;
import com.joanlica.TiendaAPI.core.util.pages.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/clientes")
@Tag(name = "2. Clientes", description = "Endpoints para la gestión de perfiles de clientes. " +
        "La mayoría requieren rol de ADMIN.")
// Añadimos el requisito de seguridad (el candado) a todos los endpoints de este controlador
@SecurityRequirement(name = "bearerAuth")
public class ClienteController {

    private final ClienteService clienteService;

    @Operation(
            summary = "Listar clientes Activos (ADMIN)",
            description = "Obtener una lista paginada con todos los clientes " +
                    "que están 'activos' en la base de datos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de clientes activos paginada"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado (requiere ROLE_ADMIN)")
            }
    )
    @GetMapping("")
    public ResponseEntity<PageResponse<ClienteResponseDto>> listarClientesActivos(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(clienteService.listarClientesActivos(pageable));
    }

    @Operation(
            summary = "Listar todos los clientes (ADMIN)",
            description = "Obtener una lista paginada con todos los clientes, " +
                    "incluyendo activos e inactivos (borrado lógico).",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de todos los clientes paginada"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado (requiere ROLE_ADMIN)")
            }
    )
    @GetMapping("/all")
    public ResponseEntity<PageResponse<ClienteResponseDto>> listarTodosClientes(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(clienteService.listarTodosLosClientes(pageable));
    }

    @Operation(
            summary = "Obtener cliente por ID (ADMIN)",
            description = "Obtener un cliente específico a partir de su ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
                    @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado (requiere ROLE_ADMIN)")
            }
    )
    @GetMapping("/{id_cliente}")
    public ResponseEntity<ClienteResponseDto> obtenerCliente(@PathVariable Long id_cliente) {
        return ResponseEntity.ok(clienteService.buscarClientePorId(id_cliente));
    }

    @Operation(
            summary = "Eliminar cliente (Soft Delete) (ADMIN)",
            description = "Elimina (borrado lógico) un cliente a partir de su ID.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado (requiere ROLE_ADMIN)")
            }
    )
    @DeleteMapping("/{id_cliente}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id_cliente) {
        clienteService.cancelarClientePorId(id_cliente);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Editar cliente (ADMIN)",
            description = "Edita la información de un cliente a partir de su ID.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Nuevos datos para el cliente",
                    content = @Content(schema = @Schema(implementation = ClienteRequestDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
                    @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado (requiere ROLE_ADMIN)")
            }
    )
    @PutMapping("/{id_cliente}")
    public ResponseEntity<ClienteResponseDto> editarCliente(@PathVariable Long id_cliente,
                                                            @Valid @RequestBody ClienteRequestDto clienteNuevo) {
        return ResponseEntity.ok(clienteService.editarClientePorId(id_cliente, clienteNuevo));
    }

    // --- Endpoints 'ME' (para el usuario logueado) ---

    @Operation(
            summary = "Obtener mi perfil de cliente",
            description = "Obtiene la información de Cliente asociada al usuario autenticado.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Perfil del cliente encontrado"),
                    @ApiResponse(responseCode = "404", description = "Perfil no encontrado para este usuario"),
                    @ApiResponse(responseCode = "401", description = "No autenticado")
            }
    )
    @GetMapping("/me")
    public ResponseEntity<ClienteResponseDto> obtenerPerfilPropio(Principal principal) {
        ClienteResponseDto cliente = clienteService.obtenerPerfilPropio(principal.getName());
        return ResponseEntity.ok(cliente);
    }

    @Operation(
            summary = "Editar mi perfil de cliente",
            description = "Edita la información del perfil de Cliente asociada al usuario autenticado.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Nuevos datos para mi perfil de cliente",
                    content = @Content(schema = @Schema(implementation = ClienteRequestDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente"),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
                    @ApiResponse(responseCode = "404", description = "Perfil no encontrado para este usuario"),
                    @ApiResponse(responseCode = "401", description = "No autenticado")
            }
    )
    @PutMapping("/me")
    public ResponseEntity<ClienteResponseDto> editarClientePropio(Principal principal,
                                                                  @Valid @RequestBody ClienteRequestDto clienteNuevo) {
        return ResponseEntity.ok(clienteService.editarClientePropio(principal.getName(), clienteNuevo));
    }
}