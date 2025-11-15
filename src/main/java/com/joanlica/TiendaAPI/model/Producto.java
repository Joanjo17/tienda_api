package com.joanlica.TiendaAPI.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SoftDelete(strategy = SoftDeleteType.ACTIVE, columnName = "activo")
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

    @OneToMany(mappedBy = "producto")
    private List<ItemVenta> listaItemVenta;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return Objects.equals(codigo_producto, producto.codigo_producto);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(codigo_producto);
    }
}