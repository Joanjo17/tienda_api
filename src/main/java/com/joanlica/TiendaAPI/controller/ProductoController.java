package com.joanlica.TiendaAPI.controller;

import com.joanlica.TiendaAPI.dto.producto.ProductoRequestDto;
import com.joanlica.TiendaAPI.dto.producto.ProductoResponseDto;
import com.joanlica.TiendaAPI.service.IProductoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private IProductoService productoService;

    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    @Operation(summary = "Crear producto", description = "Crea un nuevo producto en la base de datos.")
    @PostMapping("/crear")
    public ResponseEntity<ProductoResponseDto> crearCliente(@Valid @RequestBody ProductoRequestDto productoNuevo){
        ProductoResponseDto producto = productoService.crearProducto(productoNuevo);
        URI location = URI.create("/productos/crear/"+producto.codigo_producto());
        return ResponseEntity.created(location).body(producto);
    }

    @Operation(summary = "Listar productos", description = "Obtener una lista con todos los productos de la base de datos.")
    @GetMapping("")
    public ResponseEntity<List<ProductoResponseDto>> listarProductos(){
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @Operation(summary = "Obtener producto", description = "Obtener un determinado producto a partir de su codigo_producto")
    @GetMapping("{codigo_producto}")
    public ResponseEntity<ProductoResponseDto> obtenerProducto(@PathVariable Long codigo_producto){
        return ResponseEntity.ok(productoService.buscarProductoPorId(codigo_producto));
    }

    @Operation(summary = "Eliminar producto", description = "Elimina un determinado producto a partir de su codigo_producto")
    @DeleteMapping("/eliminar/{codigo_producto}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long codigo_producto){
        productoService.eliminarProductoPorId(codigo_producto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Editar producto", description = "Edita la información de un determinado producto a partir de su codigo_producto.")
    @PutMapping("/editar/{codigo_producto}")
    public ResponseEntity<ProductoResponseDto> editarCliente(@PathVariable Long codigo_producto,
                                                             @Valid @RequestBody ProductoRequestDto productoNuevo){
        return ResponseEntity.ok(productoService.editarProductoPorId(codigo_producto, productoNuevo));
    }

    @Operation(summary = "Recuperar Productos escasos", description = "Obtenemos un listado con los productos los cuales tengan un stock menor a una cierta cantidad límite en la base de datos.")
    @GetMapping("/falta_stock")
    public ResponseEntity<List<ProductoResponseDto>> obtenerProductosBajoStock(){
        final Double cantidad_minima = 5d; //Representa el valor a partir del cual interpretamos falta de stock.
        return ResponseEntity.ok(productoService.listarProductosBajosStock(cantidad_minima));
    }
}