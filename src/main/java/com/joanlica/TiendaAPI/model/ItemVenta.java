package com.joanlica.TiendaAPI.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ItemVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double cantidad;

    private Double precioUnitario;

    @ManyToOne
    @JoinColumn(name="codigo_venta")
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "codigo_producto", nullable = false)
    private  Producto producto;
}