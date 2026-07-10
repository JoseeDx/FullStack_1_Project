package com.tienda.ms_transaccion.service;

import com.tienda.ms_transaccion.client.PedidoClient;
import com.tienda.ms_transaccion.client.PedidoResponse;
import com.tienda.ms_transaccion.exception.BadRequestException;
import com.tienda.ms_transaccion.exception.ResourceNotFoundException;
import com.tienda.ms_transaccion.model.Transaccion;
import com.tienda.ms_transaccion.repository.TransaccionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransaccionServiceTest {

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private PedidoClient pedidoClient;

    @InjectMocks
    private TransaccionService transaccionService;

    private Transaccion transaccion;

    @BeforeEach
    void setUp() {
        transaccion = new Transaccion(1, 1, 1, "Tarjeta de crédito", 59990.0, "PENDIENTE", LocalDateTime.now());
    }

    @Test
    void save_ConDatosValidos_DeberiaGuardarCorrectamente() {
        when(pedidoClient.findById(1L)).thenReturn(new PedidoResponse(1L, LocalDateTime.now()));
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccion);

        Transaccion resultado = transaccionService.save(transaccion);

        assertNotNull(resultado);
        assertEquals("PENDIENTE", resultado.getEstado_pago());
        verify(pedidoClient, times(1)).findById(1L);
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));
    }

    @Test
    void save_ConPedidoNoExistente_DeberiaLanzarBadRequestException() {
        when(pedidoClient.findById(1L)).thenReturn(null);

        assertThrows(BadRequestException.class, () -> transaccionService.save(transaccion));
        verify(transaccionRepository, never()).save(any(Transaccion.class));
    }

    @Test
    void findById_ConIdExistente_DeberiaRetornarTransaccion() {
        when(transaccionRepository.findById(1)).thenReturn(Optional.of(transaccion));

        Transaccion resultado = transaccionService.findById(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId_transaccion());
    }

    @Test
    void findById_ConIdInexistente_DeberiaLanzarResourceNotFoundException() {
        when(transaccionRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transaccionService.findById(99));
    }

    @Test
    void findAll_DeberiaRetornarListaDeTransacciones() {
        when(transaccionRepository.findAll()).thenReturn(List.of(transaccion));

        List<Transaccion> resultado = transaccionService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    void actualizar_ConDatosValidos_DeberiaActualizarCorrectamente() {
        when(transaccionRepository.findById(1)).thenReturn(Optional.of(transaccion));
        when(pedidoClient.findById(1L)).thenReturn(new PedidoResponse(1L, LocalDateTime.now()));
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccion);

        Transaccion datosActualizados = new Transaccion(null, 1, 1, "Transferencia bancaria", 99990.0, "COMPLETADA", LocalDateTime.now());
        Transaccion resultado = transaccionService.actualizar(1, datosActualizados);

        assertNotNull(resultado);
        verify(transaccionRepository).save(any(Transaccion.class));
    }

    @Test
    void actualizar_ConIdInexistente_DeberiaLanzarResourceNotFoundException() {
        when(transaccionRepository.findById(99)).thenReturn(Optional.empty());

        Transaccion datosActualizados = new Transaccion(null, 1, 1, "Transferencia bancaria", 99990.0, "COMPLETADA", LocalDateTime.now());
        assertThrows(ResourceNotFoundException.class, () -> transaccionService.actualizar(99, datosActualizados));
    }

    @Test
    void delete_DeberiaEliminarTransaccion() {
        doNothing().when(transaccionRepository).deleteById(1);

        transaccionService.delete(1);

        verify(transaccionRepository, times(1)).deleteById(1);
    }

    @Test
    void updateEstado_ConEstadoValido_DeberiaActualizarCorrectamente() {
        when(transaccionRepository.findById(1)).thenReturn(Optional.of(transaccion));
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccion);

        Transaccion resultado = transaccionService.updateEstado(1, "COMPLETADA");

        assertNotNull(resultado);
        verify(transaccionRepository).save(any(Transaccion.class));
    }

    @Test
    void updateEstado_ConEstadoInvalido_DeberiaLanzarBadRequestException() {
        assertThrows(BadRequestException.class, () -> transaccionService.updateEstado(1, "INVALIDO"));
        verify(transaccionRepository, never()).save(any(Transaccion.class));
    }

    @Test
    void updateEstado_ConIdInexistente_DeberiaLanzarResourceNotFoundException() {
        when(transaccionRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> transaccionService.updateEstado(99, "COMPLETADA"));
        verify(transaccionRepository, never()).save(any(Transaccion.class));
    }

    @Test
    void findAll_ConErrorEnRepositorio_DeberiaLanzarRuntimeException() {
        when(transaccionRepository.findAll()).thenThrow(new RuntimeException("Error de BD"));

        assertThrows(RuntimeException.class, () -> transaccionService.findAll());
    }

    @Test
    void save_ConErrorEnRepositorio_DeberiaLanzarRuntimeException() {
        when(pedidoClient.findById(1L)).thenReturn(new PedidoResponse(1L, LocalDateTime.now()));
        when(transaccionRepository.save(any(Transaccion.class))).thenThrow(new RuntimeException("Error de BD"));

        assertThrows(RuntimeException.class, () -> transaccionService.save(transaccion));
    }

    @Test
    void delete_ConErrorEnRepositorio_DeberiaLanzarRuntimeException() {
        doThrow(new RuntimeException("Error de BD")).when(transaccionRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> transaccionService.delete(1));
    }
}
