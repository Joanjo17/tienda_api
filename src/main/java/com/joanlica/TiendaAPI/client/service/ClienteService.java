package com.joanlica.TiendaAPI.client.service;

import com.joanlica.TiendaAPI.client.dto.ClienteRequestDto;
import com.joanlica.TiendaAPI.client.dto.ClienteResponseDto;
import com.joanlica.TiendaAPI.core.util.pages.dto.PageResponse;
import org.springframework.data.domain.Pageable;

public interface ClienteService {

    PageResponse<ClienteResponseDto> listarClientesActivos(Pageable pageable);

    PageResponse<ClienteResponseDto> listarTodosLosClientes(Pageable pageable);

    ClienteResponseDto obtenerPerfilPropio(String username);

    ClienteResponseDto buscarClientePorId(Long id);

    void cancelarClientePorId(Long id);

    ClienteResponseDto editarClientePorId(Long id, ClienteRequestDto clienteNuevo);

    ClienteResponseDto editarClientePropio(String username, ClienteRequestDto clienteNuevo);
}