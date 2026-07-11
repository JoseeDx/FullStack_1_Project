package com.example.ms_pedido.service;

import com.example.ms_pedido.exception.ResourceNotFoundException;
import com.example.ms_pedido.model.Pedido;
import com.example.ms_pedido.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    private Pedido pedidoMock;

    @BeforeEach
    void setUp() {
        // Configuramos nuestro objeto falso guiándonos estrictamente por tu Modelo
        pedidoMock = new Pedido();
        // Asignamos usando Reflection o asumiendo tus setters
        // pedidoMock.setIdPedido(1L); // Nota: si tu @Id no tiene setter por ser IDENTITY, no te preocupes, Mockito lo maneja
        pedidoMock.setFechaPedido(LocalDateTime.now());
    }

    @Test
    void buscarPedidoPorId_Exito() {
        // GIVEN
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedidoMock));

        // WHEN (Ajusta 'buscarPorId' al nombre exacto de tu método en PedidoService)
        Pedido resultado = pedidoService.buscarPorId(1L);

        // THEN
        assertNotNull(resultado);
        verify(pedidoRepository, times(1)).findById(1L);
    }

    @Test
    void buscarPedidoPorId_NoEncontrado_LanzaExcepcion() {
        // GIVEN
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> {
            pedidoService.buscarPorId(99L);
        });
        verify(pedidoRepository, times(1)).findById(99L);
    }

    @Test
    void guardarPedido_Exito() {
        // GIVEN
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoMock);

        // WHEN (Ajusta 'crearPedido' al nombre exacto en tu Service)
        Pedido resultado = pedidoService.guardar(pedidoMock);

        // THEN
        assertNotNull(resultado);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }
}