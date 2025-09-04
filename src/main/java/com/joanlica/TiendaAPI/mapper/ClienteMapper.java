package com.joanlica.TiendaAPI.mapper;

import com.joanlica.TiendaAPI.dto.ClienteRequestDto;
import com.joanlica.TiendaAPI.dto.ClienteResponseDto;
import com.joanlica.TiendaAPI.model.Cliente;

import java.util.List;
import java.util.stream.Collectors;

public class ClienteMapper {

    public static Cliente toEntity(ClienteRequestDto clienteNuevo){
        Cliente cliente = new Cliente();
        cliente.setNombre(clienteNuevo.nombre());
        cliente.setApellido(clienteNuevo.apellido());
        cliente.setDni( clienteNuevo.dni());
        return cliente;
    }

    public static ClienteResponseDto toDto(Cliente cliente){
        return new ClienteResponseDto(cliente.getId_cliente(),
                cliente.getNombre(), cliente.getApellido(), cliente.getDni());
    }

    public static List<ClienteResponseDto> toDtoList(List<Cliente> clientes){
        return clientes.stream()
                .map(ClienteMapper::toDto)
                .collect(Collectors.toList());
    }
}