package com.joanlica.TiendaAPI.repository;

import com.joanlica.TiendaAPI.model.Cliente;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public class ClienteRepositoryTest {

    @Autowired
    private IClienteRepository clienteRepository;

    @Test
    public void ClienteRepository_findAllClient_ReturnSavedClients(){
        //Arrange
        Cliente cliente = new Cliente();
        cliente.setNombre("Joan");
        cliente.setApellido("Lira");
        cliente.setDni("123456789");
        //Act
        Cliente clienteSaved = clienteRepository.save(cliente);

        //Assert
        Assertions.assertNotNull(clienteSaved.getId_cliente());
        Assertions.assertTrue(clienteSaved.getId_cliente() > 0);

    }
}