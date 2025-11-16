package com.joanlica.TiendaAPI.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record VentaRequestDto(
        @Schema(
                description = "Lista detallada de los productos y cantidades a vender.",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Valid // Importante para que valide la lista de DTOs internos
        List<ItemVentaRequestsDto> itemsVendidos,

        @Schema(
                description = "ID del cliente (id_cliente) que realiza la compra.",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "El id del Cliente no puede ser nulo.")
        Long id_cliente
) {
}