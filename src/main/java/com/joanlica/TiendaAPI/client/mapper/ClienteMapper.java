package com.joanlica.TiendaAPI.client.mapper;

import com.joanlica.TiendaAPI.client.dto.ClienteRequestDto;
import com.joanlica.TiendaAPI.client.dto.ClienteResponseDto;
import com.joanlica.TiendaAPI.client.model.Cliente;
import com.joanlica.TiendaAPI.order.dto.response.ClienteVentaInfoDto;

public class ClienteMapper {

    public static Cliente toEntity(ClienteRequestDto clienteNuevo) {
        Cliente cliente = new Cliente();
        cliente.setNombre(clienteNuevo.nombre());
        cliente.setApellido(clienteNuevo.apellido());
        cliente.setDni(clienteNuevo.dni());
        return cliente;
    }

    public static ClienteResponseDto toDto(Cliente cliente) {
        return new ClienteResponseDto(cliente.getIdCliente(),
                cliente.getNombre(), cliente.getApellido(), cliente.getDni());
    }

    public static ClienteVentaInfoDto toClienteVentaInfoDto(Cliente cliente) {
        return new ClienteVentaInfoDto(cliente.getIdCliente(),
                cliente.getNombre(), cliente.getApellido());
    }
}