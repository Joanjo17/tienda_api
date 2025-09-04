package com.joanlica.TiendaAPI.service;

import com.joanlica.TiendaAPI.dto.ProductoRequestDto;
import com.joanlica.TiendaAPI.dto.ProductoResponseDto;
import com.joanlica.TiendaAPI.exception.ResourceNotFoundException;
import com.joanlica.TiendaAPI.mapper.ProductoMapper;
import com.joanlica.TiendaAPI.model.Producto;
import com.joanlica.TiendaAPI.repository.IProductoRepository;
import org.springframework.stereotype.Service;

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
    public List<ProductoResponseDto> listarProductos() {
        return ProductoMapper.toDtoList(productoRepository.findAll());
    }

    @Override
    public Producto buscarClienteEntidadPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("No se encontró el producto con id "+id));
    }

    @Override
    public ProductoResponseDto buscarProductoPorId(Long id) {
        Producto producto = this.buscarClienteEntidadPorId(id);
        return ProductoMapper.toDto(producto);
    }

    @Override
    public void eliminarProductoPorId(Long id) {
        // Primero comprobamos que esté presente y después lo eliminamos
        this.buscarClienteEntidadPorId(id);
        productoRepository.deleteById(id);
    }

    @Override
    public ProductoResponseDto editarProductoPorId(Long id, ProductoRequestDto productoNuevo) {
        Producto producto = this.buscarClienteEntidadPorId(id);

        producto.setNombre(productoNuevo.nombre());
        producto.setMarca(productoNuevo.marca());
        producto.setCosto(productoNuevo.costo());
        producto.setCantidad_disponible(productoNuevo.cantidad_disponible());

        productoRepository.save(producto);

        return ProductoMapper.toDto(producto);
    }

    @Override
    public List<ProductoResponseDto> listarProductosBajosStock(Double cantidad) {
        return ProductoMapper.toDtoList(productoRepository.findByCantidad_disponibleLessThan(cantidad));
    }
}