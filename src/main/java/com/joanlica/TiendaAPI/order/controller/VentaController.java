package com.joanlica.TiendaAPI.order.controller;

import com.joanlica.TiendaAPI.core.util.pages.dto.PageResponse;
import com.joanlica.TiendaAPI.order.dto.request.VentaRequestDto;
import com.joanlica.TiendaAPI.order.dto.response.ItemVentaResponseDto;
import com.joanlica.TiendaAPI.order.dto.response.VentaMayorResponseDto;
import com.joanlica.TiendaAPI.order.dto.response.VentaResponseDto;
import com.joanlica.TiendaAPI.order.dto.response.VentasInfoDiariaResponseDto;
import com.joanlica.TiendaAPI.order.service.VentaService;
import com.joanlica.TiendaAPI.order.util.EstadoVenta;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ventas")
@Tag(name = "4. Ventas", description = "Endpoints para crear y consultar ventas.")
@SecurityRequirement(name = "bearerAuth") // Todas las rutas de ventas requieren autenticación
public class VentaController {

    private final VentaService ventaService;

    @Operation(
            summary = "Crear una nueva venta (Autenticado)",
            description = "Crea una nueva venta con sus ítems asociados. Requiere estar autenticado.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Datos de la nueva venta, incluyendo lista de ítems y ID del cliente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = VentaRequestDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Venta creada exitosamente",
                            content = @Content(schema = @Schema(implementation = VentaResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos (DTO, falta de stock, etc.)"),
                    @ApiResponse(responseCode = "404", description = "No se encontró el Cliente o algún Producto"),
                    @ApiResponse(responseCode = "401", description = "No autenticado")
            }
    )
    @PostMapping("")
    public ResponseEntity<VentaResponseDto> crearVenta(
            @Valid @org.springframework.web.bind.annotation.RequestBody VentaRequestDto ventaNuevo) {

        VentaResponseDto venta = ventaService.crearVenta(ventaNuevo);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(venta.codigo_venta())
                .toUri();
        return ResponseEntity.created(location).body(venta);
    }

    @Operation(
            summary = "Listar ventas (ADMIN)",
            description = "Obtener una lista paginada con todas las ventas. " +
                    "Se puede filtrar por estado (COMPLETADA, CANCELADA). Requiere rol de ADMIN.",
            parameters = {
                    @Parameter(
                            name = "estado",
                            description = "Estado por el cual filtrar (COMPLETADA o CANCELADA). Si se omite, trae todas.",
                            example = "COMPLETADA"
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de ventas paginada"),
                    @ApiResponse(responseCode = "400", description = "El parámetro 'estado' es inválido"),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado (requiere ROLE_ADMIN)")
            }
    )
    @GetMapping
    public ResponseEntity<PageResponse<VentaResponseDto>> listarVentas(
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) String estado) {

        if (estado == null || estado.isEmpty()) {
            return ResponseEntity.ok(ventaService.listarTodasLasVentas(pageable));
        }
        try {
            EstadoVenta estadoVenta = EstadoVenta.valueOf(estado.toUpperCase());
            if (estadoVenta == EstadoVenta.COMPLETADA) {
                return ResponseEntity.ok(ventaService.listarVentasCompletadas(pageable));
            } else if (estadoVenta == EstadoVenta.CANCELADA) {
                return ResponseEntity.ok(ventaService.listarVentasCanceladas(pageable));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @Operation(
            summary = "Obtener venta por código (ADMIN)",
            description = "Obtener una venta específica por su ID. Requiere rol de ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Venta encontrada"),
                    @ApiResponse(responseCode = "404", description = "Venta no encontrada"),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado (requiere ROLE_ADMIN)")
            }
    )
    @GetMapping("/{codigo_venta}")
    public ResponseEntity<VentaResponseDto> buscarVentaPorCodigo(@PathVariable Long codigo_venta) {
        return ResponseEntity.ok(ventaService.buscarVentaPorId(codigo_venta));
    }

    @Operation(
            summary = "Cancelar una venta (ADMIN)",
            description = "Marca una venta como 'CANCELADA'. Requiere rol de ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Venta cancelada exitosamente"),
                    @ApiResponse(responseCode = "404", description = "Venta no encontrada"),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado (requiere ROLE_ADMIN)")
            }
    )
    @DeleteMapping("/{codigo_venta}")
    public ResponseEntity<Void> eliminarVenta(@PathVariable Long codigo_venta) {
        ventaService.cancelarVenta(codigo_venta);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Obtener ítems de una venta (ADMIN)",
            description = "Obtiene la lista de productos/ítems de una venta específica. Requiere rol de ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de ítems de la venta"),
                    @ApiResponse(responseCode = "404", description = "Venta no encontrada"),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado (requiere ROLE_ADMIN)")
            }
    )
    @GetMapping("/{codigo_venta}/productos")
    public ResponseEntity<List<ItemVentaResponseDto>> obtenerProductosDeVenta(@PathVariable Long codigo_venta) {
        return ResponseEntity.ok(ventaService.listarProductosVenta(codigo_venta));
    }

    @Operation(
            summary = "Reporte de ventas diarias (ADMIN)",
            description = "Obtiene el monto total y la cantidad de ventas 'COMPLETADAS' " +
                    "para una fecha específica. Requiere rol de ADMIN.",
            parameters = {
                    @Parameter(
                            name = "fecha_venta",
                            description = "Fecha a consultar (formato AAAA-MM-DD).",
                            example = "2025-10-25"
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reporte diario obtenido"),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado (requiere ROLE_ADMIN)")
            }
    )
    @GetMapping("/diario/{fecha_venta}")
    public ResponseEntity<VentasInfoDiariaResponseDto> obtenerVentasInfoDiaria(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_venta) {
        return ResponseEntity.ok(ventaService.buscarVentasInfoDiaria(fecha_venta));
    }

    @Operation(
            summary = "Obtener la venta con mayor importe (ADMIN)",
            description = "Obtiene los detalles de la venta 'COMPLETADA' con el importe total más alto. " +
                    "Requiere rol de ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Venta con mayor importe encontrada"),
                    @ApiResponse(responseCode = "404", description = "No se encontraron ventas completadas"),
                    @ApiResponse(responseCode = "401", description = "No autenticado"),
                    @ApiResponse(responseCode = "403", description = "Acceso denegado (requiere ROLE_ADMIN)")
            }
    )
    @GetMapping("/mayor_venta")
    public ResponseEntity<VentaMayorResponseDto> buscarMayorVenta() {
        VentaMayorResponseDto ventaMayorResponseDto = ventaService.buscarMayorVenta();
        return ResponseEntity.ok(ventaMayorResponseDto);
    }
}