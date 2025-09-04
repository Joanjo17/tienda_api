package com.joanlica.TiendaAPI.dto;

public record ClienteResponseDto(
        Long id_cliente,
        String nombre,
        String apellido,
        String dni){
}