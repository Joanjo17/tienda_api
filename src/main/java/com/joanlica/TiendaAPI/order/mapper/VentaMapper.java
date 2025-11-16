package com.joanlica.TiendaAPI.order.mapper;

import com.joanlica.TiendaAPI.client.mapper.ClienteMapper;
import com.joanlica.TiendaAPI.order.dto.response.VentaResponseDto;
import com.joanlica.TiendaAPI.order.model.Venta;

public class VentaMapper {

    public static VentaResponseDto toDto(Venta venta) {
        return new VentaResponseDto(venta.getCodigoVenta(),
                venta.getFechaVenta(), venta.getTotal(),
                ItemVentaMapper.toDtoList(venta.getListaItemVenta()),
                ClienteMapper.toClienteVentaInfoDto(venta.getUnCliente()),
                venta.getEstado().getEstado());
    }
}