package com.joanlica.TiendaAPI.core.exception;

public class VentaNotFoundException extends RuntimeException {
    public VentaNotFoundException(String message) {
        super(message);
    }
}