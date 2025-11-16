package com.joanlica.TiendaAPI.order.service;

import com.joanlica.TiendaAPI.core.util.pages.dto.PageResponse;
import com.joanlica.TiendaAPI.order.dto.request.VentaRequestDto;
import com.joanlica.TiendaAPI.order.dto.response.ItemVentaResponseDto;
import com.joanlica.TiendaAPI.order.dto.response.VentaMayorResponseDto;
import com.joanlica.TiendaAPI.order.dto.response.VentaResponseDto;
import com.joanlica.TiendaAPI.order.dto.response.VentasInfoDiariaResponseDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface VentaService {
    VentaResponseDto crearVenta(VentaRequestDto ventaDto);

    PageResponse<VentaResponseDto> listarTodasLasVentas(Pageable pageable);

    PageResponse<VentaResponseDto> listarVentasCanceladas(Pageable pageable);

    PageResponse<VentaResponseDto> listarVentasCompletadas(Pageable pageable);

    VentaResponseDto buscarVentaPorId(Long id);

    void cancelarVenta(Long codigoVenta);

    List<ItemVentaResponseDto> listarProductosVenta(Long codigoVenta);

    VentasInfoDiariaResponseDto buscarVentasInfoDiaria(LocalDate fechaVenta);

    VentaMayorResponseDto buscarMayorVenta();
}