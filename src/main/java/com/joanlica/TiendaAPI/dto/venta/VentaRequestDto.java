package com.joanlica.TiendaAPI.dto.venta;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record VentaRequestDto(
        @Valid
        List<ItemVentaRequestsDto> itemsVendidos,
        @NotNull(message = "El id del Cliente no puede ser nulo.")
        Long id_cliente
){}