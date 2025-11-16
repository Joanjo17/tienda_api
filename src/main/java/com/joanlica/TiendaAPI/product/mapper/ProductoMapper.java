package com.joanlica.TiendaAPI.product.mapper;

import com.joanlica.TiendaAPI.product.dto.ProductoRequestDto;
import com.joanlica.TiendaAPI.product.dto.ProductoResponseDto;
import com.joanlica.TiendaAPI.product.model.Producto;

public class ProductoMapper {

    public static Producto toEntity(ProductoRequestDto productoNuevo) {
        Producto producto = new Producto();
        producto.setNombre(productoNuevo.nombre());
        producto.setMarca(productoNuevo.marca());
        producto.setCosto(productoNuevo.costo());
        producto.setCantidadDisponible(productoNuevo.cantidad_disponible());
        return producto;
    }

    public static ProductoResponseDto toDto(Producto producto) {
        return new ProductoResponseDto(producto.getCodigoProducto(),
                producto.getNombre(), producto.getMarca(), producto.getCosto(), producto.getCantidadDisponible());
    }
}