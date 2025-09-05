package com.joanlica.TiendaAPI.service;

import com.joanlica.TiendaAPI.dto.*;
import com.joanlica.TiendaAPI.dto.producto.ProductoResponseDto;
import com.joanlica.TiendaAPI.dto.venta.ItemVentaResponseDto;
import com.joanlica.TiendaAPI.dto.venta.VentaRequestDto;
import com.joanlica.TiendaAPI.dto.venta.VentaResponseDto;
import com.joanlica.TiendaAPI.model.Venta;

import java.time.LocalDate;
import java.util.List;

public interface IVentaService {
    VentaResponseDto crearVenta(VentaRequestDto ventaDto);
    List<VentaResponseDto> obtenerVentas();
    Venta obtenerVentaEntityPorId(Long codigo_venta);
    VentaResponseDto obtenerVentaPorId(Long codigo_venta);
    void eliminarVenta(Long codigo_venta);

    List<ItemVentaResponseDto> obtenerProductosVenta(Long codigo_venta);
    VentasInfoDiariaResponseDto obtenerVentasInfoDiaria(LocalDate fecha_venta);
    VentaMayorResponseDto obtenerMayorVenta();
}