package com.joanlica.TiendaAPI.product.service.implementation;

import com.joanlica.TiendaAPI.core.exception.InsufficientStockException;
import com.joanlica.TiendaAPI.core.exception.ProductNotFoundException;
import com.joanlica.TiendaAPI.core.util.pages.dto.PageResponse;
import com.joanlica.TiendaAPI.order.dto.request.ItemVentaRequestsDto;
import com.joanlica.TiendaAPI.order.model.ItemVenta;
import com.joanlica.TiendaAPI.product.dto.ProductoRequestDto;
import com.joanlica.TiendaAPI.product.dto.ProductoResponseDto;
import com.joanlica.TiendaAPI.product.mapper.ProductoMapper;
import com.joanlica.TiendaAPI.product.model.Producto;
import com.joanlica.TiendaAPI.product.repository.IProductoRepository;
import com.joanlica.TiendaAPI.product.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final IProductoRepository productoRepository;

    @Override
    public ProductoResponseDto crearProducto(ProductoRequestDto productoNuevo) {
        Producto producto = productoRepository.save(ProductoMapper.toEntity(productoNuevo));
        return ProductoMapper.toDto(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductoResponseDto> listarProductosActivos(Pageable pageable) {
        Page<ProductoResponseDto> page = productoRepository.findAll(pageable)
                .map(ProductoMapper::toDto);
        return PageResponse.from(page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductoResponseDto> listarTodosLosProductos(Pageable pageable) {
        Page<ProductoResponseDto> page = productoRepository.findAllIncludingInactive(pageable)
                .map(ProductoMapper::toDto);
        return PageResponse.from(page);
    }

    private Producto buscarProductoEntidadPorId(Long codigo_producto) {
        return productoRepository.findById(codigo_producto)
                .orElseThrow(() -> new ProductNotFoundException("No se encontró el producto con id " + codigo_producto));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDto buscarProductoPorId(Long codigo_producto) {
        Producto producto = this.buscarProductoEntidadPorId(codigo_producto);
        return ProductoMapper.toDto(producto);
    }

    @Override
    public void cancelarProductoPorId(Long codigo_producto) {
        // Primero comprobamos que esté presente y después lo eliminamos
        this.buscarProductoEntidadPorId(codigo_producto);

        // Soft Delete se encarga de cambiar el valor de `activo`.
        productoRepository.deleteById(codigo_producto);
    }

    @Override
    public ProductoResponseDto editarProductoPorId(Long codigo_producto, ProductoRequestDto productoNuevo) {
        Producto producto = this.buscarProductoEntidadPorId(codigo_producto);

        producto.setNombre(productoNuevo.nombre());
        producto.setMarca(productoNuevo.marca());
        producto.setCosto(productoNuevo.costo());
        producto.setCantidadDisponible(productoNuevo.cantidad_disponible());

        productoRepository.save(producto);

        return ProductoMapper.toDto(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProductoResponseDto> listarProductosBajosStock(Pageable pageable, Double cantidad) {
        Page<ProductoResponseDto> page = productoRepository.findByCantidadDisponibleLessThan(pageable, cantidad)
                .map(ProductoMapper::toDto);
        return PageResponse.from(page);
    }

    @Override
    public List<Producto> validarYReducirStock(List<ItemVentaRequestsDto> listaItemVentaRequestsDto) {
        List<Producto> productos = new ArrayList<>();
        for (ItemVentaRequestsDto item : listaItemVentaRequestsDto) {
            Producto producto = this.buscarProductoEntidadPorId(item.codigo_producto());
            if (producto.getCantidadDisponible() < item.cantidad()) {
                throw new InsufficientStockException("Stock insuficiente para el producto con id " + item.codigo_producto());
            }
            producto.setCantidadDisponible(producto.getCantidadDisponible() - item.cantidad());
            productos.add(producto);
        }
        productoRepository.saveAll(productos);
        return productos;
    }

    @Override
    public void devolverStock(List<ItemVenta> listaItemVenta) {
        List<Producto> productos = new ArrayList<>();
        for (ItemVenta item : listaItemVenta) {
            Producto producto = item.getProducto();
            producto.setCantidadDisponible(producto.getCantidadDisponible() + item.getCantidad());
            productos.add(producto);
        }
        productoRepository.saveAll(productos);
    }
}