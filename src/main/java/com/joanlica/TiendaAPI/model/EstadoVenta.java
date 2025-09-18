package com.joanlica.TiendaAPI.model;

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