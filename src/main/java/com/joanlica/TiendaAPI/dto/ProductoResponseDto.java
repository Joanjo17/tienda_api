package com.joanlica.TiendaAPI.dto;

public record ProductoResponseDto(
        Long codigo_producto,
        String nombre,
        String marca,
        Double costo,
        Double cantidad_disponible
){}