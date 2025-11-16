package com.joanlica.TiendaAPI.order.model;

import com.joanlica.TiendaAPI.product.model.Producto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "item_ventas")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemVenta {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double cantidad;

    @Column(name = "precio_unitario")
    private Double precioUnitario;

    @ManyToOne
    @JoinColumn(name = "codigo_venta")
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "codigo_producto", nullable = false)
    private Producto producto;
}