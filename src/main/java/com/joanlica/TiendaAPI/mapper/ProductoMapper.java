package com.joanlica.TiendaAPI.mapper;

import com.joanlica.TiendaAPI.dto.ProductoRequestDto;
import com.joanlica.TiendaAPI.dto.ProductoResponseDto;
import com.joanlica.TiendaAPI.model.Producto;

import java.util.List;
import java.util.stream.Collectors;

public class ProductoMapper{

    public static Producto toEntity(ProductoRequestDto productoNuevo){
        Producto producto = new Producto();
        producto.setNombre(productoNuevo.nombre());
        producto.setMarca(productoNuevo.marca());
        producto.setCosto(productoNuevo.costo());
        producto.setCantidadDisponible(productoNuevo.cantidad_disponible());
        return producto;
    }

    public static ProductoResponseDto toDto(Producto producto){
        return new ProductoResponseDto(producto.getCodigo_producto(),
                producto.getNombre(),producto.getMarca(),producto.getCosto(),producto.getCantidadDisponible());
    }

    public static List<ProductoResponseDto> toDtoList(List<Producto> productos){
        return productos.stream()
                .map(ProductoMapper::toDto)
                .collect(Collectors.toList());
    }
}