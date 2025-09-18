package com.joanlica.TiendaAPI.mapper;

import com.joanlica.TiendaAPI.dto.venta.ItemVentaResponseDto;
import com.joanlica.TiendaAPI.model.ItemVenta;

import java.util.List;
import java.util.stream.Collectors;

public class ItemVentaMapper {

    public static ItemVentaResponseDto toDto(ItemVenta itemVenta) {
        return new ItemVentaResponseDto(
                itemVenta.getId(),
                itemVenta.getProducto() != null ? itemVenta.getProducto().getNombre() : "Producto Eliminado",
                itemVenta.getProducto() != null ? itemVenta.getProducto().getMarca() : "Marca Desconocida",
                itemVenta.getPrecioUnitario(),
                itemVenta.getCantidad());
    }

    public static List<ItemVentaResponseDto> toDtoList(List<ItemVenta> itemVentas) {
        return itemVentas.stream().map(ItemVentaMapper::toDto).collect(Collectors.toList());
    }
}