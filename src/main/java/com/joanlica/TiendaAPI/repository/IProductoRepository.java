package com.joanlica.TiendaAPI.repository;

import com.joanlica.TiendaAPI.model.Cliente;
import com.joanlica.TiendaAPI.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Long> {

    public List<Producto> findByCantidad_disponibleLessThan(Double disponible);
}