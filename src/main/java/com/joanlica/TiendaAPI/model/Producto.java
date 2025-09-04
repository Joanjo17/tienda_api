package com.joanlica.TiendaAPI.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo_producto;

    @Column(nullable = false)
    String nombre;
    @Column(nullable = false)
    String marca;
    @Column(nullable = false)
    Double costo;
    @Column(nullable = false)
    Double cantidadDisponible;
}