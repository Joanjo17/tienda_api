package com.joanlica.TiendaAPI.client.service.implementation;

import com.joanlica.TiendaAPI.client.dto.ClienteRequestDto;
import com.joanlica.TiendaAPI.client.dto.ClienteResponseDto;
import com.joanlica.TiendaAPI.client.mapper.ClienteMapper;
import com.joanlica.TiendaAPI.client.model.Cliente;
import com.joanlica.TiendaAPI.client.repository.ClienteRepository;
import com.joanlica.TiendaAPI.client.service.ClienteService;
import com.joanlica.TiendaAPI.core.exception.ClientNotFoundException;
import com.joanlica.TiendaAPI.core.util.pages.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ClienteResponseDto> listarClientesActivos(Pageable pageable) {
        Page<ClienteResponseDto> page = clienteRepository.findAll(pageable)
                .map(ClienteMapper::toDto);
        return PageResponse.from(page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ClienteResponseDto> listarTodosLosClientes(Pageable pageable) {
        Page<ClienteResponseDto> page = clienteRepository.findAllIncludingInactive(pageable)
                .map(ClienteMapper::toDto);
        return PageResponse.from(page);
    }

    private Cliente buscarClienteEntidadPorId(Long id) {
        // Solo para Clientes Activos.
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("No se encontró el cliente con el id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDto obtenerPerfilPropio(String username) {
        Cliente cliente = buscarClientePorUsername(username);
        return ClienteMapper.toDto(cliente);
    }

    private Cliente buscarClientePorUsername(String username) {
        return clienteRepository.findClienteByUser_Username(username)
                .orElseThrow(() -> new ClientNotFoundException("El cliente no existe"));
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponseDto buscarClientePorId(Long id) {
        Cliente cliente = this.buscarClienteEntidadPorId(id);
        return ClienteMapper.toDto(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public void cancelarClientePorId(Long id) {
        // Primero comprobamos que exista.
        this.buscarClienteEntidadPorId(id);

        // El deleteById es interceptado por @SoftDelete.
        // Cambia la columna 'activo' a 'false' en vez de borrar la fila.
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

    @Override
    public ClienteResponseDto editarClientePropio(String username, ClienteRequestDto clienteNuevo) {
        Cliente cliente = buscarClientePorUsername(username);

        cliente.setNombre(clienteNuevo.nombre());
        cliente.setApellido(clienteNuevo.apellido());
        cliente.setDni(clienteNuevo.dni());

        clienteRepository.save(cliente);
        return ClienteMapper.toDto(cliente);
    }
}