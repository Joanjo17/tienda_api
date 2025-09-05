package com.joanlica.TiendaAPI.service;

import com.joanlica.TiendaAPI.dto.*;
import com.joanlica.TiendaAPI.dto.venta.ItemVentaRequestsDto;
import com.joanlica.TiendaAPI.dto.venta.ItemVentaResponseDto;
import com.joanlica.TiendaAPI.dto.venta.VentaRequestDto;
import com.joanlica.TiendaAPI.dto.venta.VentaResponseDto;
import com.joanlica.TiendaAPI.exception.InsufficientStockException;
import com.joanlica.TiendaAPI.exception.ResourceNotFoundException;
import com.joanlica.TiendaAPI.mapper.ItemVentaMapper;
import com.joanlica.TiendaAPI.mapper.VentaMapper;
import com.joanlica.TiendaAPI.model.Cliente;
import com.joanlica.TiendaAPI.model.ItemVenta;
import com.joanlica.TiendaAPI.model.Producto;
import com.joanlica.TiendaAPI.model.Venta;
import com.joanlica.TiendaAPI.repository.IClienteRepository;
import com.joanlica.TiendaAPI.repository.IItemVentaRepository;
import com.joanlica.TiendaAPI.repository.IProductoRepository;
import com.joanlica.TiendaAPI.repository.IVentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class VentaService implements IVentaService{

    private final IVentaRepository ventaRepository;
    private final IClienteRepository clienteRepository;
    private final IProductoRepository productoRepository;


    public VentaService(IVentaRepository ventaRepository, IClienteRepository clienteRepository,
                        IProductoRepository productoRepository, IItemVentaRepository itemVentaRepository) {
        this.ventaRepository = ventaRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional
    public VentaResponseDto crearVenta(VentaRequestDto ventaDto) {
        Cliente cliente = clienteRepository.findById(ventaDto.id_cliente())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id " + ventaDto.id_cliente()));
        Venta nuevaVenta = new Venta();

        nuevaVenta.setUnCliente(cliente);
        nuevaVenta.setFechaVenta(LocalDate.now());

        Double totalFinal = 0.0;
        nuevaVenta.setTotal(totalFinal);

        List<ItemVenta> itemsVenta = new ArrayList<>();

        for(ItemVentaRequestsDto item : ventaDto.itemsVendidos()){
            Producto producto = productoRepository.findById(item.codigo_producto())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

            // Comprobamos el Stock antes de intentar descontarlo
            if (producto.getCantidadDisponible() < item.cantidad()) {
                throw new InsufficientStockException("Stock insuficiente para el producto " + producto.getNombre()+
                                            " con código "+producto.getCodigo_producto());
            }

            // Actualizamos el Stock y guardamos el producto.
            producto.setCantidadDisponible(producto.getCantidadDisponible()- item.cantidad());
            productoRepository.save(producto);

            //Creamos el ItemVenta y lo añadimos a la lista
            ItemVenta itemVenta = new ItemVenta();
            itemVenta.setProducto(producto);
            itemVenta.setCantidad(item.cantidad());
            itemVenta.setPrecioUnitario(producto.getCosto());
            itemVenta.setVenta(nuevaVenta); // Lo asociamos a la venta que estamos creando

            itemsVenta.add(itemVenta);

            // Sumamos el costo del Item al Total de la Venta
            totalFinal += item.cantidad() * producto.getCosto();
        }

        nuevaVenta.setTotal(totalFinal);
        nuevaVenta.setListaItemVenta(itemsVenta);

        ventaRepository.save(nuevaVenta);

        return VentaMapper.toDto(nuevaVenta);

    }

    @Override
    public List<VentaResponseDto> obtenerVentas() {
        return VentaMapper.toDtoList(ventaRepository.findAll());
    }

    @Override
    public Venta obtenerVentaEntityPorId(Long id){
        return ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrado con id " + id));
    }

    @Override
    public VentaResponseDto obtenerVentaPorId(Long id) {

        return VentaMapper.toDto(this.obtenerVentaEntityPorId(id));
    }

    @Override
    public void eliminarVenta(Long id) {
        this.obtenerVentaEntityPorId(id);
        ventaRepository.deleteById(id);
    }

    @Override
    public List<ItemVentaResponseDto> obtenerProductosVenta(Long codigo_venta) {
        Venta venta = this.obtenerVentaEntityPorId(codigo_venta);
        return ItemVentaMapper.toDtoList(venta.getListaItemVenta());
    }

    @Override
    public VentasInfoDiariaResponseDto obtenerVentasInfoDiaria(LocalDate fecha_venta) {
        List<Venta> ventasDiarias = ventaRepository.findAllByFechaVenta(fecha_venta);
        Double montoTotal = ventasDiarias.stream().mapToDouble(Venta::getTotal).sum();
        Integer cantidad_ventas = ventasDiarias.size();
        return new VentasInfoDiariaResponseDto(montoTotal, cantidad_ventas);
    }

    @Override
    public VentaMayorResponseDto obtenerMayorVenta() {
        List<Venta> todasLasVentas = ventaRepository.findAll();
        Venta ventaMasCara = todasLasVentas.stream()
                .max(Comparator.comparing(Venta::getTotal))
                .orElseThrow(()-> new ResourceNotFoundException("Venta no encontrado"));

        return new VentaMayorResponseDto(ventaMasCara.getCodigo_venta(),ventaMasCara.getTotal(),
                ventaMasCara.getListaItemVenta().stream().mapToDouble(ItemVenta::getCantidad).sum(),
        ventaMasCara.getUnCliente().getNombre(),ventaMasCara.getUnCliente().getApellido());
    }
}