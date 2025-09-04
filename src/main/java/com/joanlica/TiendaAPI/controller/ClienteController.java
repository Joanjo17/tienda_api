package com.joanlica.TiendaAPI.controller;

import com.joanlica.TiendaAPI.dto.ClienteRequestDto;
import com.joanlica.TiendaAPI.dto.ClienteResponseDto;
import com.joanlica.TiendaAPI.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(summary = "Crear cliente", description = "Crea un nuevo cliente en la base de datos.")
    @PostMapping("/crear")
    public ResponseEntity<ClienteResponseDto> crearCliente(@Valid @RequestBody ClienteRequestDto clienteNuevo){
        ClienteResponseDto cliente = clienteService.crearCliente(clienteNuevo);
        URI location = URI.create("/clientes/crear/"+cliente.id_cliente());
        return ResponseEntity.created(location).body(cliente);
    }

    @Operation(summary = "Listar clientes", description = "Obtener una lista con todos los clientes de la base de datos.")
    @GetMapping("")
    public ResponseEntity<List<ClienteResponseDto>> listarClientes(){
        return ResponseEntity.ok(clienteService.listarClientes());
    }

    @Operation(summary = "Obtener cliente", description = "Obtener un determinado cliente a partir de su id_cliente")
    @GetMapping("{id_cliente}")
    public ResponseEntity<ClienteResponseDto> obtenerCliente(@PathVariable Long id_cliente){
        return ResponseEntity.ok(clienteService.buscarClientePorId(id_cliente));
    }

    @Operation(summary = "Eliminar cliente", description = "Elimina un determinado cliente a partir de su id_cliente")
    @DeleteMapping("/eliminar/{id_cliente}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id_cliente){
        clienteService.eliminarClientePorId(id_cliente);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Editar cliente", description = "Edita la información de un determinado cliente a partir de su id_cliente.")
    @PutMapping("/editar/{id_cliente}")
    public ResponseEntity<ClienteResponseDto> editarCliente(@PathVariable Long id_cliente,
                                                            @Valid @RequestBody ClienteRequestDto clienteNuevo){
        return ResponseEntity.ok(clienteService.editarClientePorId(id_cliente, clienteNuevo));
    }
}