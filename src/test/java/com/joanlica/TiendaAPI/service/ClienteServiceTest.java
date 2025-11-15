package com.joanlica.TiendaAPI.service;

import com.joanlica.TiendaAPI.dto.cliente.ClienteRequestDto;
import com.joanlica.TiendaAPI.dto.cliente.ClienteResponseDto;
import com.joanlica.TiendaAPI.model.Cliente;
import com.joanlica.TiendaAPI.repository.IClienteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private IClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void crearClienteQueNoExiste() {
        ClienteRequestDto request = new ClienteRequestDto("Joan",
                "Lira","12345678A");
        Cliente clienteRequest = new Cliente(1L,request.nombre(),request.apellido(),request.dni());
        Mockito.when(clienteRepository.existePorDni("12345678A"))
                .thenReturn(false);
        Mockito.when(clienteRepository.save(Mockito.any(Cliente.class))).thenReturn(clienteRequest);

        final ClienteResponseDto response = clienteService.crearCliente(request);

        Assertions.assertNotNull(response,"La respuesta no debe ser nula.");
        Assertions.assertEquals(response.nombre(), clienteRequest.getNombre());
        Assertions.assertEquals(response.apellido(), clienteRequest.getApellido());
        Assertions.assertEquals(response.dni(), clienteRequest.getDni());

    }

}