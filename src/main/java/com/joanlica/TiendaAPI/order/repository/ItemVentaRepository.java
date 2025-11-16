package com.joanlica.TiendaAPI.order.repository;

import com.joanlica.TiendaAPI.order.model.ItemVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemVentaRepository extends JpaRepository<ItemVenta, Long> {
}