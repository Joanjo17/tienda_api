package com.joanlica.TiendaAPI.service;

import com.joanlica.TiendaAPI.dto.*;
import com.joanlica.TiendaAPI.dto.venta.ItemVentaRequestsDto;
import com.joanlica.TiendaAPI.dto.venta.ItemVentaResponseDto;
import com.joanlica.TiendaAPI.dto.venta.VentaRequestDto;
import com.joanlica.TiendaAPI.dto.venta.VentaResponseDto;
import com.joanlica.TiendaAPI.exception.ResourceNotFoundException;
import com.joanlica.TiendaAPI.mapper.ItemVentaMapper;
import com.joanlica.TiendaAPI.mapper.VentaMapper;
import com.joanlica.TiendaAPI.model.*;
import com.joanlica.TiendaAPI.repository.IClienteRepository;
import com.joanlica.TiendaAPI.repository.IVentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VentaService implements IVentaService{

    private final IVentaRepository ventaRepository;
    private final IClienteRepository clienteRepository;
    private final IProductoService productoService;


    public VentaService(IVentaRepository ventaRepository, IClienteRepository clienteRepository,
                        IProductoService productoService) {
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.productoService = productoService;
    }

    @Override
    @Transactional
    public VentaResponseDto crearVenta(VentaRequestDto ventaDto) {
        Cliente cliente = clienteRepository.findById(ventaDto.id_cliente())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id " + ventaDto.id_cliente()));

        // 1. Delegamos la validación y reducción de stock al ProductoService
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
    public List<VentaResponseDto> listarTodasLasVentas() {
        return VentaMapper.toDtoList(ventaRepository.findAll());
    }

    @Override
    public List<VentaResponseDto> listarVentasCanceladas() {
        return VentaMapper.toDtoList(ventaRepository.findAllByEstado(EstadoVenta.CANCELADA));
    }

    @Override
    public List<VentaResponseDto> listarVentasCompletadas() {
        return VentaMapper.toDtoList(ventaRepository.findAllByEstado(EstadoVenta.COMPLETADA));
    }

    @Override
    public VentaResponseDto buscarVentaPorId(Long id){
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
        List<Venta> ventasDiarias = ventaRepository.findAllByFechaVentaAndEstado(fecha_venta,EstadoVenta.COMPLETADA);
        Double montoTotal = ventasDiarias.stream().mapToDouble(Venta::getTotal).sum();
        Integer cantidad_ventas = ventasDiarias.size();
        return new VentasInfoDiariaResponseDto(montoTotal, cantidad_ventas);
    }

    @Override
    public VentaMayorResponseDto buscarMayorVenta() {
        List<Venta> todasLasVentas = ventaRepository.findAllByEstado(EstadoVenta.COMPLETADA);
        Venta ventaMasCara = todasLasVentas.stream()
                .max(Comparator.comparing(Venta::getTotal))
                .orElseThrow(()-> new ResourceNotFoundException("Venta no encontrada"));

        return new VentaMayorResponseDto(ventaMasCara.getCodigo_venta(),ventaMasCara.getTotal(),
                ventaMasCara.getListaItemVenta().stream().mapToDouble(ItemVenta::getCantidad).sum(),
        ventaMasCara.getUnCliente().getNombre(),ventaMasCara.getUnCliente().getApellido());
    }

    // --- Métodos privados

    private Venta buscarVentaEntityPorId(Long id){
        return ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrado con id " + id));
    }

    private List<ItemVenta> crearListaItems(List<ItemVentaRequestsDto> itemsDto, List<Producto> productosVendidos, Venta venta) {
        Map<Long, Producto> mapaProductos = productosVendidos.stream()
                .collect(Collectors.toMap(Producto::getCodigo_producto, p -> p));

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