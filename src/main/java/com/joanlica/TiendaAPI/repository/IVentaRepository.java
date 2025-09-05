package com.joanlica.TiendaAPI.repository;

import com.joanlica.TiendaAPI.model.Cliente;
import com.joanlica.TiendaAPI.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IVentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findAllByFechaVenta(LocalDate fecha);
    List<Venta> findAllByUnCliente(Cliente cliente);
}