package com.joanlica.TiendaAPI.product.repository;

import com.joanlica.TiendaAPI.product.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Long> {

    // Obtener todos los productos, activos o no.
    @Query(value = "SELECT * FROM productos", nativeQuery = true)
    Page<Producto> findAllIncludingInactive(Pageable pageable);

    // Obtener un Listado de Productos cuyo Stock sea inferior a una cierta cantidad.
    // Obtendremos siempre productos Activos a causa del SoftDelete.
    Page<Producto> findByCantidadDisponibleLessThan(Pageable pageable, Double cantidad);

}