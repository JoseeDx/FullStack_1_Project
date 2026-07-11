package com.example.cliente_service.service;

import com.example.cliente_service.exception.ResourceNotFoundException;
import com.example.cliente_service.model.Cliente;
import com.example.cliente_service.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente clienteMock;

    @BeforeEach
    void setUp() {
        // Configuramos un cliente falso para no tocar la BD real
        clienteMock = new Cliente();
        // Asumiendo métodos estándar, ajusta si tus setters difieren
        // clienteMock.setId(1L);
        // clienteMock.setNombre("Juan Perez");
        // clienteMock.setEmail("juan@test.com");
    }

    @Test
    void buscarClientePorId_Exito() {
        // GIVEN (Dado que el cliente existe en la BD simulada)
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteMock));

        // WHEN (Cuando llamamos al servicio)
        Cliente resultado = clienteService.buscarPorId(1L); // O como se llame tu método (ej. buscarPorId)

        // THEN (Entonces obtenemos el cliente correctamente)
        assertNotNull(resultado);
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void buscarClientePorId_NoEncontrado_LanzaExcepcion() {
        // GIVEN (Dado que la BD responde que no existe)
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN & THEN (Cuando intentamos buscar, Entonces lanza error 404)
        assertThrows(ResourceNotFoundException.class, () -> {
            clienteService.buscarPorId(99L);
        });
        verify(clienteRepository, times(1)).findById(99L);
    }

    @Test
    void guardarCliente_Exito() {
        // GIVEN (Dado que queremos guardar un cliente)
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteMock);

        // WHEN (Cuando lo guardamos)
        Cliente resultado = clienteService.guardar(clienteMock); // O guardarCliente(clienteMock)

        // THEN (Entonces se devuelve el objeto guardado)
        assertNotNull(resultado);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }
}