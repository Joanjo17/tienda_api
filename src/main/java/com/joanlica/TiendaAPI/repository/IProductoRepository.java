package com.joanlica.TiendaAPI.repository;

import com.joanlica.TiendaAPI.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Long> {

    // Obtener todos los productos, activos o no.
    @Query(value = "SELECT * FROM producto", nativeQuery = true)
    List<Producto> findAllIncludingInactive();

    // Obtener un Listado de Productos cuyo Stock sea inferior a una cierta cantidad.
    // Obtendremos siempre productos Activos a causa del SoftDelete.
    List<Producto> findByCantidadDisponibleLessThan(Double cantidad);

}