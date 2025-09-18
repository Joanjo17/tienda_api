package com.joanlica.TiendaAPI.controller;

import com.joanlica.TiendaAPI.dto.*;
import com.joanlica.TiendaAPI.dto.venta.ItemVentaResponseDto;
import com.joanlica.TiendaAPI.dto.venta.VentaRequestDto;
import com.joanlica.TiendaAPI.dto.venta.VentaResponseDto;
import com.joanlica.TiendaAPI.model.EstadoVenta;
import com.joanlica.TiendaAPI.service.IVentaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    private final IVentaService ventaService;

    public VentaController(IVentaService ventaService) {
        this.ventaService = ventaService;
    }

    @Operation(summary = "Crear venta", description = "Crea una nueva venta con sus ítems asociados.")
    @PostMapping("")
    public ResponseEntity<VentaResponseDto> crearVenta(@Valid @RequestBody VentaRequestDto ventaNuevo){
        VentaResponseDto venta = ventaService.crearVenta(ventaNuevo);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(venta.codigo_venta())
                .toUri();
        return ResponseEntity.created(location).body(venta);
    }

    @Operation(summary = "Listar ventas",
            description = "Obtener una lista con todas las ventas de la base de datos." +
                    "Se puede filtrar por el estado de venta pasandole el parámetro 'estado'.")
    @GetMapping
    public ResponseEntity<List<VentaResponseDto>> listarVentas(
            @RequestParam(required = false) String estado) {

        // Si no se ha indicado ningún estado, listamos todas las ventas.
        if (estado == null || estado.isEmpty()) {
            return ResponseEntity.ok(ventaService.listarTodasLasVentas());
        }

        // Validamos el estado
        try {
            EstadoVenta estadoVenta = EstadoVenta.valueOf(estado.toUpperCase());

            // Devolver el listado de ventas según el estado indicado
            if (estadoVenta == EstadoVenta.COMPLETADA) {
                return ResponseEntity.ok(ventaService.listarVentasCompletadas());
            } else if (estadoVenta == EstadoVenta.CANCELADA) {
                return ResponseEntity.ok(ventaService.listarVentasCanceladas());
            }

        } catch (IllegalArgumentException e) {
            // Si el 'estado' es inválido devolvemos un BAD REQUEST
            return ResponseEntity.badRequest().build();
        }

        // Para algún 'estado' nuevo no tratado
        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "Obtener venta por código",
            description = "Obtener una determinada venta, sin tener en cuenta el valor de su estado, a partir de su codigo_venta.")
    @GetMapping("/{codigo_venta}")
    public ResponseEntity<VentaResponseDto> buscarVentaPorCodigo(@PathVariable Long codigo_venta){
        return ResponseEntity.ok(ventaService.buscarVentaPorId(codigo_venta));
    }

    @Operation(summary = "Eliminar venta",
            description = "Elimina una determinada venta a partir de su codigo_venta. Esto elimina también todos los ítems asociados.")
    @DeleteMapping("/{codigo_venta}")
    public ResponseEntity<Void> eliminarVenta(@PathVariable Long codigo_venta){
        ventaService.cancelarVenta(codigo_venta);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Obtener productos de una venta",
            description = "Obtiene una lista de productos que forman parte de una venta específica.")
    @GetMapping("/{codigo_venta}/productos")
    public ResponseEntity<List<ItemVentaResponseDto>> obtenerProductosDeVenta(@PathVariable Long codigo_venta){
        return ResponseEntity.ok(ventaService.listarProductosVenta(codigo_venta));
    }

    @Operation(summary = "Información de ventas diarias",
            description = "Obtiene el monto total y la cantidad de ventas para un día específico.")
    @GetMapping("/diario/{fecha_venta}")
    public ResponseEntity<VentasInfoDiariaResponseDto> obtenerVentasInfoDiaria(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_venta){
        return ResponseEntity.ok(ventaService.buscarVentasInfoDiaria(fecha_venta));
    }

    @Operation(summary = "Obtener la mayor venta",
            description = "Obtiene los detalles de la venta con el total más alto.")
    @GetMapping("/mayor_venta")
    public ResponseEntity<VentaMayorResponseDto> buscarMayorVenta(){
        return ResponseEntity.ok(ventaService.buscarMayorVenta());
    }
}