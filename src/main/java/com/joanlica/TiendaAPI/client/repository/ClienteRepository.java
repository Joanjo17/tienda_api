package com.joanlica.TiendaAPI.client.repository;

import com.joanlica.TiendaAPI.client.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Obtenemos todos los clientes, incluyendo los inactivos.
    // El softDelete no se aplicará a esta query
    @Query(value = "SELECT * FROM clientes", nativeQuery = true)
    Page<Cliente> findAllIncludingInactive(Pageable pageable);

    // Ejemplo de una consulta nativa de Spring Data JPA
    @Query(value = "SELECT EXISTS (SELECT 1 FROM clientes c WHERE c.dni = :dni)", nativeQuery = true)
    Long countPorDni(@Param("dni") String dni);

    Optional<Cliente> findClienteByUser_Username(String userUsername);
}