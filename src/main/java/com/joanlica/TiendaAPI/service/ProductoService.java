package com.joanlica.TiendaAPI.service;

import com.joanlica.TiendaAPI.dto.producto.ProductoRequestDto;
import com.joanlica.TiendaAPI.dto.producto.ProductoResponseDto;
import com.joanlica.TiendaAPI.exception.ResourceNotFoundException;
import com.joanlica.TiendaAPI.mapper.ProductoMapper;
import com.joanlica.TiendaAPI.model.ItemVenta;
import com.joanlica.TiendaAPI.model.Producto;
import com.joanlica.TiendaAPI.repository.IItemVentaRepository;
import com.joanlica.TiendaAPI.repository.IProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService implements IProductoService{

    private final IProductoRepository productoRepository;
    private final IItemVentaRepository itemVentaRepository;

    public ProductoService(IProductoRepository productoRepository, IItemVentaRepository itemVentaRepository) {
        this.productoRepository = productoRepository;
        this.itemVentaRepository = itemVentaRepository;
    }

    @Override
    public ProductoResponseDto crearProducto(ProductoRequestDto productoNuevo) {
        Producto producto = productoRepository.save(ProductoMapper.toEntity(productoNuevo));
        return ProductoMapper.toDto(producto);
    }

    @Override
    public List<ProductoResponseDto> listarProductos() {
        return ProductoMapper.toDtoList(productoRepository.findAll());
    }

    @Override
    public Producto buscarProductoEntidadPorId(Long codigo_producto) {
        return productoRepository.findById(codigo_producto)
                .orElseThrow(()->new ResourceNotFoundException("No se encontró el producto con id "+codigo_producto));
    }

    @Override
    public ProductoResponseDto buscarProductoPorId(Long codigo_producto) {
        Producto producto = this.buscarProductoEntidadPorId(codigo_producto);
        return ProductoMapper.toDto(producto);
    }

    @Override
    public void eliminarProductoPorId(Long codigo_producto) {
        // Primero comprobamos que esté presente y después lo eliminamos
        Producto producto = this.buscarProductoEntidadPorId(codigo_producto);

        for (ItemVenta item : producto.getListaItemVenta()) {
            item.setProducto(null);
            // Guardamos el ItemVenta para que la base de datos lo actualice.
            itemVentaRepository.save(item);
        }

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
}