package com.joanlica.TiendaAPI.order.util;

import lombok.Getter;

@Getter
public enum EstadoVenta {
    COMPLETADA("Completada"),
    CANCELADA("Cancelada");

    private final String estado;

    EstadoVenta(String estado) {
        this.estado = estado;
    }
}