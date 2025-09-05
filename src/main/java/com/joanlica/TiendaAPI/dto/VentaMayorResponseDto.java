package com.joanlica.TiendaAPI.dto;

public record VentaMayorResponseDto(
        Long codigo_venta,
        Double total,
        Double cantidad_productos,
        String nombre_cliente,
        String apellido_cliente
) {
}