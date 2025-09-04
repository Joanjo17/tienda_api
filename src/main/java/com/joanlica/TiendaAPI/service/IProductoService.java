package com.joanlica.TiendaAPI.service;

import com.joanlica.TiendaAPI.dto.ProductoRequestDto;
import com.joanlica.TiendaAPI.dto.ProductoResponseDto;
import com.joanlica.TiendaAPI.model.Producto;

import java.util.List;

public interface IProductoService {
    ProductoResponseDto crearProducto(ProductoRequestDto productoNuevo);
    List<ProductoResponseDto> listarProductos();
    Producto buscarClienteEntidadPorId(Long id);
    ProductoResponseDto buscarProductoPorId(Long id);
    void eliminarProductoPorId(Long id);
    ProductoResponseDto editarProductoPorId(Long id, ProductoRequestDto productoNuevo);

    List<ProductoResponseDto> listarProductosBajosStock(Double cantidad);
}