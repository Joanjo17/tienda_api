package com.joanlica.TiendaAPI.order.repository;

import com.joanlica.TiendaAPI.order.model.Venta;
import com.joanlica.TiendaAPI.order.util.EstadoVenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    // Función para filtrar todas las ventas que no sean de un estado (p.e. no CANCELADA)
    List<Venta> findAllByEstado(EstadoVenta estado);

    Page<Venta> findAllByEstado(Pageable pageable, EstadoVenta estado);

    List<Venta> findAllByFechaVentaAndEstado(LocalDate fecha, EstadoVenta estado);
}