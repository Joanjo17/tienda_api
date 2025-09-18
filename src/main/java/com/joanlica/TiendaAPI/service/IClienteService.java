package com.joanlica.TiendaAPI.service;

import com.joanlica.TiendaAPI.dto.cliente.ClienteRequestDto;
import com.joanlica.TiendaAPI.dto.cliente.ClienteResponseDto;

import java.util.List;

public interface IClienteService {
    ClienteResponseDto crearCliente(ClienteRequestDto clienteNuevo);
    List<ClienteResponseDto> listarClientesActivos();
    List<ClienteResponseDto> listarTodosLosClientes();
    ClienteResponseDto buscarClientePorId(Long id);
    void cancelarClientePorId(Long id);
    ClienteResponseDto editarClientePorId(Long id, ClienteRequestDto clienteNuevo);
}