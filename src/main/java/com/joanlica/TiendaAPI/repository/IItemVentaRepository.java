package com.joanlica.TiendaAPI.repository;

import com.joanlica.TiendaAPI.model.ItemVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IItemVentaRepository extends JpaRepository<ItemVenta, Long> {
}