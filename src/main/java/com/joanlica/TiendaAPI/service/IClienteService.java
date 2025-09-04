package com.joanlica.TiendaAPI.service;

import com.joanlica.TiendaAPI.dto.ClienteRequestDto;
import com.joanlica.TiendaAPI.dto.ClienteResponseDto;
import com.joanlica.TiendaAPI.model.Cliente;

import java.util.List;

public interface IClienteService {
    ClienteResponseDto crearCliente(ClienteRequestDto clienteNuevo);
    List<ClienteResponseDto> listarClientes();
    Cliente buscarClienteEntidadPorId(Long id);
    ClienteResponseDto buscarClientePorId(Long id);
    void eliminarClientePorId(Long id);
    ClienteResponseDto editarClientePorId(Long id, ClienteRequestDto clienteNuevo);
}