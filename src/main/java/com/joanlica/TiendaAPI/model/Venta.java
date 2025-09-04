package com.joanlica.TiendaAPI.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
@Entity
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo_venta;

    @Column(nullable = false)
    private LocalDate fecha_venta;

    @Column(nullable = false)
    private Double total;


    @ManyToMany
    private List<Producto> listaProductos;

    @ManyToOne
    @JoinColumn(name="id_cliente",
    referencedColumnName = "id_cliente")
    private Cliente unCliente;
}