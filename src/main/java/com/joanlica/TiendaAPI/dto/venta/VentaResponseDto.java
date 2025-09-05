package com.joanlica.TiendaAPI.dto.venta;

import java.time.LocalDate;
import java.util.List;

public record VentaResponseDto(
         Long codigo_venta,
         LocalDate fechaVenta,
         Double precioTotal,
         List<ItemVentaResponseDto> listaItemVenta,
         Object cliente
){}