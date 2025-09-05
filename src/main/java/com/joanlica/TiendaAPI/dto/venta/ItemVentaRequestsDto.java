package com.joanlica.TiendaAPI.dto.venta;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ItemVentaRequestsDto(
        @NotNull(message = "La cantidad no puede ser nula")
        @Positive(message = "La cantidad no puede ser negativa o cero")
        Double cantidad,
        @NotNull(message = "El id del producto no puede ser nulo")
        Long codigo_producto
){}