package com.joanlica.TiendaAPI.controller;

import com.joanlica.TiendaAPI.dto.*;
import com.joanlica.TiendaAPI.dto.producto.ProductoResponseDto;
import com.joanlica.TiendaAPI.dto.venta.ItemVentaResponseDto;
import com.joanlica.TiendaAPI.dto.venta.VentaRequestDto;
import com.joanlica.TiendaAPI.dto.venta.VentaResponseDto;
import com.joanlica.TiendaAPI.service.IVentaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/crear")
    public ResponseEntity<VentaResponseDto> crearVenta(@Valid @RequestBody VentaRequestDto ventaNuevo){
        VentaResponseDto venta = ventaService.crearVenta(ventaNuevo);
        URI location = URI.create("/ventas/"+venta.codigo_venta());
        return ResponseEntity.created(location).body(venta);
    }

    @Operation(summary = "Listar ventas", description = "Obtener una lista con todas las ventas de la base de datos.")
    @GetMapping("")
    public ResponseEntity<List<VentaResponseDto>> listarVentas(){
        return ResponseEntity.ok(ventaService.obtenerVentas());
    }

    @Operation(summary = "Obtener venta por código", description = "Obtener una determinada venta a partir de su codigo_venta.")
    @GetMapping("/{codigo_venta}")
    public ResponseEntity<VentaResponseDto> obtenerVentaPorId(@PathVariable Long codigo_venta){
        return ResponseEntity.ok(ventaService.obtenerVentaPorId(codigo_venta));
    }

    @Operation(summary = "Eliminar venta", description = "Elimina una determinada venta a partir de su codigo_venta. Esto elimina también todos los ítems asociados.")
    @DeleteMapping("/eliminar/{codigo_venta}")
    public ResponseEntity<Void> eliminarVenta(@PathVariable Long codigo_venta){
        ventaService.eliminarVenta(codigo_venta);
        return ResponseEntity.noContent().build();
    }

    // PUT no incluido para Ventas, para mantener la integridad de datos.

    @Operation(summary = "Obtener productos de una venta", description = "Obtiene una lista de productos que forman parte de una venta específica.")
    @GetMapping("/productos/{codigo_venta}")
    public ResponseEntity<List<ItemVentaResponseDto>> obtenerProductosDeVenta(@PathVariable Long codigo_venta){
        return ResponseEntity.ok(ventaService.obtenerProductosVenta(codigo_venta));
    }

    @Operation(summary = "Información de ventas diarias", description = "Obtiene el monto total y la cantidad de ventas para un día específico.")
    @GetMapping("/diario/{fecha_venta}")
    public ResponseEntity<VentasInfoDiariaResponseDto> obtenerVentasInfoDiaria(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha_venta){
        return ResponseEntity.ok(ventaService.obtenerVentasInfoDiaria(fecha_venta));
    }

    @Operation(summary = "Obtener la mayor venta", description = "Obtiene los detalles de la venta con el total más alto.")
    @GetMapping("/mayor_venta")
    public ResponseEntity<VentaMayorResponseDto> obtenerMayorVenta(){
        return ResponseEntity.ok(ventaService.obtenerMayorVenta());
    }


}