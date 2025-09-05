package com.joanlica.TiendaAPI.mapper;

import com.joanlica.TiendaAPI.dto.venta.ClienteVentaInfoDto;
import com.joanlica.TiendaAPI.dto.venta.VentaResponseDto;
import com.joanlica.TiendaAPI.model.Venta;

import java.util.List;
import java.util.stream.Collectors;

public class VentaMapper {

    public static VentaResponseDto toDto(Venta venta) {
        return new VentaResponseDto(venta.getCodigo_venta(),
                venta.getFechaVenta(),venta.getTotal(),
                ItemVentaMapper.toDtoList(venta.getListaItemVenta()),
                                venta.getUnCliente() != null? ClienteMapper.toClienteVentaDto(
                                        venta.getUnCliente()) : "Cliente no disponible");
    }

    public static List<VentaResponseDto> toDtoList(List<Venta> ventas) {
        return ventas.stream().map(VentaMapper::toDto).collect(Collectors.toList());
    }
}