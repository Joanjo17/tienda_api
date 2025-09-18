package com.joanlica.TiendaAPI.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SoftDelete(strategy = SoftDeleteType.ACTIVE, columnName = "activo")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_cliente;

    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String apellido;
    @Column(nullable = false)
    private String dni;

}