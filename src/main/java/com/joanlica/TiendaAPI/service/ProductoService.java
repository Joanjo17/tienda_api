package com.joanlica.TiendaAPI.service;

import com.joanlica.TiendaAPI.dto.producto.ProductoRequestDto;
import com.joanlica.TiendaAPI.dto.producto.ProductoResponseDto;
import com.joanlica.TiendaAPI.dto.venta.ItemVentaRequestsDto;
import com.joanlica.TiendaAPI.exception.InsufficientStockException;
import com.joanlica.TiendaAPI.exception.ResourceNotFoundException;
import com.joanlica.TiendaAPI.mapper.ProductoMapper;
import com.joanlica.TiendaAPI.model.ItemVenta;
import com.joanlica.TiendaAPI.model.Producto;
import com.joanlica.TiendaAPI.repository.IProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductoService implements IProductoService{

    private final IProductoRepository productoRepository;

    public ProductoService(IProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public ProductoResponseDto crearProducto(ProductoRequestDto productoNuevo) {
        Producto producto = productoRepository.save(ProductoMapper.toEntity(productoNuevo));
        return ProductoMapper.toDto(producto);
    }

    @Override
    public List<ProductoResponseDto> listarProductosActivos() {
        return ProductoMapper.toDtoList(productoRepository.findAll());
    }

    private Producto buscarProductoEntidadPorId(Long codigo_producto) {
        return productoRepository.findById(codigo_producto)
                .orElseThrow(()->new ResourceNotFoundException("No se encontró el producto con id "+codigo_producto));
    }

    @Override
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
    public List<ProductoResponseDto> listarProductosBajosStock(Double cantidad) {
        return ProductoMapper.toDtoList(productoRepository.findByCantidadDisponibleLessThan(cantidad));
    }

    @Override
    @Transactional
    public List<Producto> validarYReducirStock(List<ItemVentaRequestsDto> listaItemVentaRequestsDto) {
        List<Producto> productos = new ArrayList<>();
        for(ItemVentaRequestsDto item : listaItemVentaRequestsDto){
            Producto producto = this.buscarProductoEntidadPorId(item.codigo_producto());
            if(producto.getCantidadDisponible() < item.cantidad()) {
                throw new InsufficientStockException("Stock insuficiente para el producto con id "+item.codigo_producto());
            }
            producto.setCantidadDisponible(producto.getCantidadDisponible() - item.cantidad());
            productos.add(producto);
        }
        productoRepository.saveAll(productos);
        return productos;
    }

    @Override
    @Transactional
    public void devolverStock(List<ItemVenta> listaItemVenta) {
        List<Producto> productos = new ArrayList<>();
        for (ItemVenta item : listaItemVenta) {
            Producto producto = item.getProducto();
            producto.setCantidadDisponible(producto.getCantidadDisponible() + item.getCantidad());
            productos.add(producto);
        }
        productoRepository.saveAll(productos);
    }

    @Override
    public List<ProductoResponseDto> listarTodosLosProductos() {
        return ProductoMapper.toDtoList(productoRepository.findAllIncludingInactive());
    }
}