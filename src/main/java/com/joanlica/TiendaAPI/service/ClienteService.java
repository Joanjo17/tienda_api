package com.joanlica.TiendaAPI.service;

import com.joanlica.TiendaAPI.dto.ClienteRequestDto;
import com.joanlica.TiendaAPI.dto.ClienteResponseDto;
import com.joanlica.TiendaAPI.exception.ResourceNotFoundException;
import com.joanlica.TiendaAPI.mapper.ClienteMapper;
import com.joanlica.TiendaAPI.model.Cliente;
import com.joanlica.TiendaAPI.repository.IClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService implements IClienteService{

    private final IClienteRepository clienteRepository;

    public ClienteService(IClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public ClienteResponseDto crearCliente(ClienteRequestDto clienteNuevo) {
        Cliente cliente = clienteRepository.save(ClienteMapper.toEntity(clienteNuevo));
        return ClienteMapper.toDto(cliente);
    }

    @Override
    public List<ClienteResponseDto> listarClientes() {
        return ClienteMapper.toDtoList(clienteRepository.findAll());
    }

    @Override
    public Cliente buscarClienteEntidadPorId(Long id){
        return clienteRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No se encontró el cliente con el id "+id));
    }

    @Override
    public ClienteResponseDto buscarClientePorId(Long id) {
        Cliente cliente = this.buscarClienteEntidadPorId(id);
        return ClienteMapper.toDto(cliente);
    }

    @Override
    public void eliminarClientePorId(Long id) {
        // Primero comprobamos que exista, si no existe se informa, en caso que exista, se elimina.
        this.buscarClienteEntidadPorId(id);
        clienteRepository.deleteById(id);
    }

    @Override
    public ClienteResponseDto editarClientePorId(Long id, ClienteRequestDto clienteNuevo) {
        Cliente cliente = this.buscarClienteEntidadPorId(id);

        cliente.setNombre(clienteNuevo.nombre());
        cliente.setApellido(clienteNuevo.apellido());
        cliente.setDni(clienteNuevo.dni());

        clienteRepository.save(cliente);
        return ClienteMapper.toDto(cliente);
    }
}