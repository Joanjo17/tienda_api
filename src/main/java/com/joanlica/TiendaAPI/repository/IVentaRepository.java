package com.joanlica.TiendaAPI.repository;

import com.joanlica.TiendaAPI.model.EstadoVenta;
import com.joanlica.TiendaAPI.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IVentaRepository extends JpaRepository<Venta, Long> {

    // Función para filtrar todas las ventas que no sean de un estado (p.e. no CANCELADA)
    List<Venta> findAllByEstado(EstadoVenta estado);

    List<Venta> findAllByFechaVentaAndEstado(LocalDate fecha, EstadoVenta estado);
}