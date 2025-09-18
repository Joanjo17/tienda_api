package com.joanlica.TiendaAPI.repository;

import com.joanlica.TiendaAPI.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IClienteRepository extends JpaRepository<Cliente, Long> {

    // Obtenemos todos los clientes, incluyendo los inactivos.
    // El softDelete no se aplicará a esta query
    @Query(value = "SELECT * FROM cliente", nativeQuery = true)
    List<Cliente> findAllIncludingInactive();
}