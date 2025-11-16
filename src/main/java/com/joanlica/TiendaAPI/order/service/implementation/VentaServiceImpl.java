package com.joanlica.TiendaAPI.order.service.implementation;

import com.joanlica.TiendaAPI.client.model.Cliente;
import com.joanlica.TiendaAPI.client.repository.ClienteRepository;
import com.joanlica.TiendaAPI.core.exception.ClientNotFoundException;
import com.joanlica.TiendaAPI.core.exception.VentaNotFoundException;
import com.joanlica.TiendaAPI.core.util.pages.dto.PageResponse;
import com.joanlica.TiendaAPI.order.dto.request.ItemVentaRequestsDto;
import com.joanlica.TiendaAPI.order.dto.request.VentaRequestDto;
import com.joanlica.TiendaAPI.order.dto.response.ItemVentaResponseDto;
import com.joanlica.TiendaAPI.order.dto.response.VentaMayorResponseDto;
import com.joanlica.TiendaAPI.order.dto.response.VentaResponseDto;
import com.joanlica.TiendaAPI.order.dto.response.VentasInfoDiariaResponseDto;
import com.joanlica.TiendaAPI.order.mapper.ItemVentaMapper;
import com.joanlica.TiendaAPI.order.mapper.VentaMapper;
import com.joanlica.TiendaAPI.order.model.ItemVenta;
import com.joanlica.TiendaAPI.order.model.Venta;
import com.joanlica.TiendaAPI.order.repository.VentaRepository;
import com.joanlica.TiendaAPI.order.service.VentaService;
import com.joanlica.TiendaAPI.order.util.EstadoVenta;
import com.joanlica.TiendaAPI.product.model.Producto;
import com.joanlica.TiendaAPI.product.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoService productoService;

    @Override
    @Transactional
    public VentaResponseDto crearVenta(VentaRequestDto ventaDto) {
        Cliente cliente = clienteRepository.findById(ventaDto.id_cliente())
                .orElseThrow(() -> new ClientNotFoundException("Cliente no encontrado con id " + ventaDto.id_cliente()));

        // 1. Delegamos la validación y reducción de stock al ProductoServiceImpl
        List<Producto> productosVendidos = productoService.validarYReducirStock(ventaDto.itemsVendidos());

        Venta nuevaVenta = new Venta();
        nuevaVenta.setUnCliente(cliente);
        nuevaVenta.setFechaVenta(LocalDate.now());
        nuevaVenta.setEstado(EstadoVenta.COMPLETADA);

        List<ItemVenta> itemsVenta = crearListaItems(ventaDto.itemsVendidos(), productosVendidos, nuevaVenta);
        nuevaVenta.setListaItemVenta(itemsVenta);

        Double totalFinal = calcularTotal(itemsVenta);
        nuevaVenta.setTotal(totalFinal);

        ventaRepository.save(nuevaVenta);

        return VentaMapper.toDto(nuevaVenta);
    }

    @Override
    public PageResponse<VentaResponseDto> listarTodasLasVentas(Pageable pageable) {
        Page<VentaResponseDto> page = ventaRepository.findAll(pageable)
                .map(VentaMapper::toDto);
        return PageResponse.from(page);
    }

    @Override
    public PageResponse<VentaResponseDto> listarVentasCanceladas(Pageable pageable) {
        Page<VentaResponseDto> page = ventaRepository.findAllByEstado(pageable, EstadoVenta.CANCELADA)
                .map(VentaMapper::toDto);
        return PageResponse.from(page);
    }

    @Override
    public PageResponse<VentaResponseDto> listarVentasCompletadas(Pageable pageable) {
        Page<VentaResponseDto> page = ventaRepository.findAllByEstado(pageable, EstadoVenta.COMPLETADA)
                .map(VentaMapper::toDto);
        return PageResponse.from(page);
    }

    @Override
    public VentaResponseDto buscarVentaPorId(Long id) {
        return VentaMapper.toDto(this.buscarVentaEntityPorId(id));
    }

    @Override
    @Transactional
    public void cancelarVenta(Long codigo_venta) {
        Venta venta = buscarVentaEntityPorId(codigo_venta);

        if (venta.getEstado() == EstadoVenta.CANCELADA) {
            throw new IllegalStateException("La venta ya ha sido cancelada.");
        }

        // 2. Devolvemos el stock al inventario
        productoService.devolverStock(venta.getListaItemVenta());

        venta.setEstado(EstadoVenta.CANCELADA);
        ventaRepository.save(venta);
    }

    @Override
    public List<ItemVentaResponseDto> listarProductosVenta(Long codigo_venta) {
        Venta venta = this.buscarVentaEntityPorId(codigo_venta);
        return ItemVentaMapper.toDtoList(venta.getListaItemVenta());
    }

    @Override
    public VentasInfoDiariaResponseDto buscarVentasInfoDiaria(LocalDate fecha_venta) {
        List<Venta> ventasDiarias = ventaRepository.findAllByFechaVentaAndEstado(fecha_venta, EstadoVenta.COMPLETADA);
        Double montoTotal = ventasDiarias.stream().mapToDouble(Venta::getTotal).sum();
        Integer cantidad_ventas = ventasDiarias.size();
        return new VentasInfoDiariaResponseDto(montoTotal, cantidad_ventas);
    }

    @Override
    public VentaMayorResponseDto buscarMayorVenta() {
        List<Venta> todasLasVentas = ventaRepository.findAllByEstado(EstadoVenta.COMPLETADA);
        Venta ventaMasCara = todasLasVentas.stream()
                .max(Comparator.comparing(Venta::getTotal))
                .orElseThrow(() -> new VentaNotFoundException("No se ha encontrado ninguna venta."));

        return new VentaMayorResponseDto(ventaMasCara.getCodigoVenta(), ventaMasCara.getTotal(),
                ventaMasCara.getListaItemVenta().stream().mapToDouble(ItemVenta::getCantidad).sum(),
                ventaMasCara.getUnCliente().getNombre(), ventaMasCara.getUnCliente().getApellido());
    }

    // --- Métodos privados

    private Venta buscarVentaEntityPorId(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new VentaNotFoundException("Venta no encontrado con id " + id));
    }

    private List<ItemVenta> crearListaItems(List<ItemVentaRequestsDto> itemsDto, List<Producto> productosVendidos, Venta venta) {
        Map<Long, Producto> mapaProductos = productosVendidos.stream()
                .collect(Collectors.toMap(Producto::getCodigoProducto, p -> p));

        return itemsDto.stream().map(itemDto -> {
            Producto producto = mapaProductos.get(itemDto.codigo_producto());

            ItemVenta itemVenta = new ItemVenta();
            itemVenta.setProducto(producto);
            itemVenta.setCantidad(itemDto.cantidad());
            itemVenta.setPrecioUnitario(producto.getCosto());
            itemVenta.setVenta(venta);

            return itemVenta;
        }).collect(Collectors.toList());
    }

    private Double calcularTotal(List<ItemVenta> items) {
        return items.stream()
                .mapToDouble(item -> item.getCantidad() * item.getPrecioUnitario())
                .sum();
    }
}