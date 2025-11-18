package com.joanlica.TiendaAPI.auth.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterUserRequestDTO(
        @Schema(
                description = "Nombre de usuario único.",
                example = "jperez",
                minLength = 3,
                maxLength = 50,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El nombre de usuario es obligatorio")
        @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
        String username,

        @Schema(
                description = "Contraseña con mínimo 8 caracteres, al menos una letra, un número y un símbolo.",
                example = "PasswOrd!123",
                minLength = 8,
                pattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d]).+$",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d]).+$",
                message = "La contraseña debe tener al menos una letra, un número y un símbolo")
        String password,
        /*
        @Schema(
                description = "Lista de IDs de los roles para el nuevo usuario. (Ej: 1 para USER)",
                example = "[1]",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotEmpty(message = "Debe asignarse al menos un rol")
        Set<Long> roleIds,
        */
        @Schema(
                description = "Nombre(s) de la persona que se registra.",
                example = "Joan Josep",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El nombre es obligatorio.")
        String nombre,

        @Schema(
                description = "Apellido(s) de la persona que se registra.",
                example = "Lira Casanova",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El apellido es obligatorio.")
        String apellido,

        @Schema(
                description = "DNI de la persona que se registra (8 números y 1 letra).",
                example = "12345678A",
                pattern = "[0-9]{8}[A-Z]",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El DNI es obligatorio.")
        @Pattern(regexp = "[0-9]{8}[A-Z]", message = "El DNI deben ser 8 números seguidos de una letra mayúscula.")
        String dni
) {
}