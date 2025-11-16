package com.joanlica.TiendaAPI.order.model;


import com.joanlica.TiendaAPI.client.model.Cliente;
import com.joanlica.TiendaAPI.order.util.EstadoVenta;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ventas")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Venta {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo_venta")
    private Long codigoVenta;

    @Column(nullable = false, name = "fecha_venta")
    private LocalDate fechaVenta;

    @Column(nullable = false)
    private Double total;


    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemVenta> listaItemVenta;

    @ManyToOne
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente", nullable = false)
    private Cliente unCliente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoVenta estado = EstadoVenta.COMPLETADA;
}