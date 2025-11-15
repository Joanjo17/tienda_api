package com.joanlica.TiendaAPI.repository;

import com.joanlica.TiendaAPI.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IClienteRepository extends JpaRepository<Cliente, Long> {

    // Obtenemos todos los clientes, incluyendo los inactivos.
    // El softDelete no se aplicará a esta query
    @Query(value = "SELECT * FROM cliente", nativeQuery = true)
    List<Cliente> findAllIncludingInactive();

    // Ejemplo de una consulta nativa de Spring Data JPA
    @Query(value = "SELECT EXISTS (SELECT 1 FROM cliente c WHERE c.dni = :dni)", nativeQuery = true)
    boolean existePorDni(@Param("dni") String dni);

}