package com.joanlica.TiendaAPI.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemVenta itemVenta = (ItemVenta) o;
        return Objects.equals(id, itemVenta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}