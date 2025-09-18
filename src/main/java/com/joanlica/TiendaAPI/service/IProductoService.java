package com.joanlica.TiendaAPI.service;

import com.joanlica.TiendaAPI.dto.producto.ProductoRequestDto;
import com.joanlica.TiendaAPI.dto.producto.ProductoResponseDto;
import com.joanlica.TiendaAPI.dto.venta.ItemVentaRequestsDto;
import com.joanlica.TiendaAPI.model.ItemVenta;
import com.joanlica.TiendaAPI.model.Producto;

import java.util.List;

public interface IProductoService {
    ProductoResponseDto crearProducto(ProductoRequestDto productoNuevo);
    List<ProductoResponseDto> listarProductosActivos();
    List<ProductoResponseDto> listarTodosLosProductos();
    ProductoResponseDto buscarProductoPorId(Long codigo_producto);
    void cancelarProductoPorId(Long codigo_producto);
    ProductoResponseDto editarProductoPorId(Long id, ProductoRequestDto productoNuevo);

    List<ProductoResponseDto> listarProductosBajosStock(Double cantidad);

    List<Producto> validarYReducirStock(List<ItemVentaRequestsDto> listaItemVentaRequestsDto);
    void devolverStock(List<ItemVenta> listaItemVenta);
}