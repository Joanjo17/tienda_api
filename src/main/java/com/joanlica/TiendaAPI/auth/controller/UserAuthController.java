package com.joanlica.TiendaAPI.auth.controller;


import com.joanlica.TiendaAPI.auth.dto.user.LoginUserRequestDTO;
import com.joanlica.TiendaAPI.auth.dto.user.RegisterUserRequestDTO;
import com.joanlica.TiendaAPI.auth.dto.user.UserAuthResponseDTO;
import com.joanlica.TiendaAPI.auth.service.UserAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "1. Autenticación", description = "Endpoints para el registro y login de usuarios.")
public class UserAuthController {

    private final UserAuthService userAuthService;

    @Operation(
            summary = "Registrar nuevo usuario",
            description = "Registra un nuevo usuario (UserAuth) y su perfil (Cliente) asociado. " +
                    "Retorna los datos del usuario y un token JWT.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody( // Define el cuerpo de la petición
                    required = true,
                    description = "Datos completos para el registro de usuario y cliente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RegisterUserRequestDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Usuario registrado exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserAuthResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409", // 409 Conflict
                            description = "El 'username' o el 'DNI' ya están en uso",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "404", // 404 Not Found
                            description = "No se encontró alguno de los roles (RoleIds) proporcionados",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos de entrada inválidos (falla de validación del DTO)",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<UserAuthResponseDTO> register(@Valid @RequestBody RegisterUserRequestDTO registerUserRequestDTO) {
        UserAuthResponseDTO userAuthResponseDTO = userAuthService.register(registerUserRequestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest() // Apunta a /api/v1/auth/register
                .path("/{id}") // Le añadimos el ID del usuario creado
                .buildAndExpand(userAuthResponseDTO.id())
                .toUri();

        return ResponseEntity.created(location).body(userAuthResponseDTO);
    }

    @Operation(
            summary = "Inicio de sesión de usuario",
            description = "Autentica a un usuario existente y retorna un token JWT.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Credenciales (username y password) para el login",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoginUserRequestDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuario logueado exitosamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserAuthResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "No autorizado / Credenciales incorrectas",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos de entrada inválidos (campos vacíos)",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<UserAuthResponseDTO> login(@Valid @RequestBody LoginUserRequestDTO loginUserRequestDTO) {
        UserAuthResponseDTO userAuthResponseDTO = userAuthService.login(loginUserRequestDTO);
        return ResponseEntity.ok(userAuthResponseDTO);
    }
}