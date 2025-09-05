package com.joanlica.TiendaAPI.dto.venta;

public record ClienteVentaInfoDto(
        Long idCliente,
        String nombre,
        String apellido
){}