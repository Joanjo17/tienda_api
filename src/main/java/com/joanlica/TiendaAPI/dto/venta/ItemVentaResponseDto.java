package com.joanlica.TiendaAPI.dto.venta;

public record ItemVentaResponseDto(
        Long id,
        String nombre_producto,
        String marca,
        Double precioUnitario,
        Double cantidad
)
{}