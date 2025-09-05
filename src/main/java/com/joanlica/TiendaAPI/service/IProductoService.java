package com.joanlica.TiendaAPI.service;

import com.joanlica.TiendaAPI.dto.producto.ProductoRequestDto;
import com.joanlica.TiendaAPI.dto.producto.ProductoResponseDto;
import com.joanlica.TiendaAPI.model.Producto;

import java.util.List;

public interface IProductoService {
    ProductoResponseDto crearProducto(ProductoRequestDto productoNuevo);
    List<ProductoResponseDto> listarProductos();
    Producto buscarProductoEntidadPorId(Long codigo_producto);
    ProductoResponseDto buscarProductoPorId(Long codigo_producto);
    void eliminarProductoPorId(Long codigo_producto);
    ProductoResponseDto editarProductoPorId(Long id, ProductoRequestDto productoNuevo);

    List<ProductoResponseDto> listarProductosBajosStock(Double cantidad);
}