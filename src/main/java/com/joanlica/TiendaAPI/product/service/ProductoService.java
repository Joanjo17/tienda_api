package com.joanlica.TiendaAPI.product.service;

import com.joanlica.TiendaAPI.core.util.pages.dto.PageResponse;
import com.joanlica.TiendaAPI.order.dto.request.ItemVentaRequestsDto;
import com.joanlica.TiendaAPI.order.model.ItemVenta;
import com.joanlica.TiendaAPI.product.dto.ProductoRequestDto;
import com.joanlica.TiendaAPI.product.dto.ProductoResponseDto;
import com.joanlica.TiendaAPI.product.model.Producto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductoService {
    ProductoResponseDto crearProducto(ProductoRequestDto productoNuevo);

    PageResponse<ProductoResponseDto> listarProductosActivos(Pageable pageable);

    PageResponse<ProductoResponseDto> listarTodosLosProductos(Pageable pageable);

    ProductoResponseDto buscarProductoPorId(Long codigoProducto);

    void cancelarProductoPorId(Long codigoProducto);

    ProductoResponseDto editarProductoPorId(Long id, ProductoRequestDto productoNuevo);

    PageResponse<ProductoResponseDto> listarProductosBajosStock(Pageable pageable, Double cantidad);

    // Metodos para calculos necesarios para las funcionalidades de Ventas. No exponer en el controlador de Producto.
    List<Producto> validarYReducirStock(List<ItemVentaRequestsDto> listaItemVentaRequestsDto);

    void devolverStock(List<ItemVenta> listaItemVenta);
}