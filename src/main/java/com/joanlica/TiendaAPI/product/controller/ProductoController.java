package com.joanlica.TiendaAPI.product.controller;

import com.joanlica.TiendaAPI.core.util.pages.dto.PageResponse;
import com.joanlica.TiendaAPI.product.dto.ProductoRequestDto;
import com.joanlica.TiendaAPI.product.dto.ProductoResponseDto;
import com.joanlica.TiendaAPI.product.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos", description = "Endpoints para la gestión del inventario de productos.")
public class ProductoController {

    private final ProductoService productoService;

    @Operation(
            summary = "Crear un nuevo producto (ADMIN)",
            description = "Crea un nuevo producto en la base de datos. Requiere rol de ADMIN.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Datos del nuevo producto",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductoRequestDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Producto creado exitosamente",
                            content = @Content(schema = @Schema(implementation = ProductoResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado (requiere ROLE_ADMIN)")
            }
    )
    @SecurityRequirement(name = "bearerAuth") // Añade el candado en Swagger
    @PostMapping("")
    public ResponseEntity<ProductoResponseDto> crearProducto(
            @Valid @org.springframework.web.bind.annotation.RequestBody ProductoRequestDto productoNuevo) {

        ProductoResponseDto producto = productoService.crearProducto(productoNuevo);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}") // El {id} se reemplaza con el 'codigo_producto'
                .buildAndExpand(producto.codigo_producto())
                .toUri();
        return ResponseEntity.created(location).body(producto);
    }

    @Operation(
            summary = "Listar productos activos (Público)",
            description = "Obtener una lista paginada con todos los productos 'activos' de la base de datos." +
                    " Este endpoint es público.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de productos paginada"
                            // No hay 401/403 porque es público
                    )
            }
    )
    @GetMapping("")
    public ResponseEntity<PageResponse<ProductoResponseDto>> listarProductosActivos(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(productoService.listarProductosActivos(pageable));
    }

    @Operation(
            summary = "Listar todos los productos (ADMIN)",
            description = "Obtener una lista paginada con todos los productos, " +
                    "tanto activos como inactivos. Requiere rol de ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de todos los productos paginada"),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado (requiere ROLE_ADMIN)")
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/all")
    public ResponseEntity<PageResponse<ProductoResponseDto>> listarTodosLosProductos(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(productoService.listarTodosLosProductos(pageable));
    }

    @Operation(
            summary = "Obtener un producto por ID (Público)",
            description = "Obtener un determinado producto a partir de su ID (codigo_producto)." +
                    " Este endpoint es público.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Producto encontrado"),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
            }
    )
    @GetMapping("/{codigo_producto}")
    public ResponseEntity<ProductoResponseDto> obtenerProducto(@PathVariable Long codigo_producto) {
        return ResponseEntity.ok(productoService.buscarProductoPorId(codigo_producto));
    }

    @Operation(
            summary = "Eliminar producto (Soft Delete) (ADMIN)",
            description = "Elimina (borrado lógico) un producto a partir de su ID. Requiere rol de ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Producto eliminado (marcado como inactivo)"),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado (requiere ROLE_ADMIN)")
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{codigo_producto}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long codigo_producto) {
        productoService.cancelarProductoPorId(codigo_producto);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Editar producto (ADMIN)",
            description = "Edita la información de un producto existente. Requiere rol de ADMIN.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Nuevos datos para el producto",
                    content = @Content(schema = @Schema(implementation = ProductoRequestDto.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Producto actualizado"),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
                    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado (requiere ROLE_ADMIN)")
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{codigo_producto}")
    public ResponseEntity<ProductoResponseDto> editarProducto(@PathVariable Long codigo_producto,
                                                              @Valid @RequestBody ProductoRequestDto productoNuevo) {
        return ResponseEntity.ok(productoService.editarProductoPorId(codigo_producto, productoNuevo));
    }

    @Operation(
            summary = "Recuperar productos con bajo stock (ADMIN)",
            description = "Obtiene un listado paginado de productos 'ACTIVOS' cuyo stock es " +
                    "menor a una cantidad límite. Requiere rol de ADMIN.",
            // Documentamos el @RequestParam
            parameters = {
                    @Parameter(
                            name = "cantidad",
                            description = "Cantidad límite para considerar bajo stock.",
                            example = "10"
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de productos con bajo stock"),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado (requiere ROLE_ADMIN)")
            }
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/falta_stock")
    public ResponseEntity<PageResponse<ProductoResponseDto>> obtenerProductosBajoStock(
            @PageableDefault Pageable pageable,
            @RequestParam(name = "cantidad", defaultValue = "5") Double cantidad
    ) {
        return ResponseEntity.ok(productoService.listarProductosBajosStock(pageable, cantidad));
    }
}