package com.joanlica.TiendaAPI.product.model;

import com.joanlica.TiendaAPI.order.model.ItemVenta;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "productos")
@SoftDelete(strategy = SoftDeleteType.ACTIVE, columnName = "activo")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Producto {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_producto")
    private Long codigoProducto;

    @Column(nullable = false)
    String nombre;
    @Column(nullable = false)
    String marca;
    @Column(nullable = false)
    Double costo;
    @Column(nullable = false, name = "cantidad_disponible")
    Double cantidadDisponible;

    @OneToMany(mappedBy = "producto")
    private List<ItemVenta> listaItemVenta;
}