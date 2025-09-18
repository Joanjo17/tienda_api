package com.joanlica.TiendaAPI.service;

import com.joanlica.TiendaAPI.dto.*;
import com.joanlica.TiendaAPI.dto.venta.ItemVentaResponseDto;
import com.joanlica.TiendaAPI.dto.venta.VentaRequestDto;
import com.joanlica.TiendaAPI.dto.venta.VentaResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface IVentaService {
    VentaResponseDto crearVenta(VentaRequestDto ventaDto);
    List<VentaResponseDto> listarTodasLasVentas();
    List<VentaResponseDto> listarVentasCanceladas();
    List<VentaResponseDto> listarVentasCompletadas();

    VentaResponseDto buscarVentaPorId(Long id);
    void cancelarVenta(Long codigo_venta);

    List<ItemVentaResponseDto> listarProductosVenta(Long codigo_venta);
    VentasInfoDiariaResponseDto buscarVentasInfoDiaria(LocalDate fecha_venta);
    VentaMayorResponseDto buscarMayorVenta();
}