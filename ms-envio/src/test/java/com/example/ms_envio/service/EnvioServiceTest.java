package com.example.ms_envio.service;

import com.example.ms_envio.dto.EnvioDTO;
import com.example.ms_envio.model.Envio;
import com.example.ms_envio.repository.EnvioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnvioServiceTest {

    @Mock
    private EnvioRepository repository;

    @InjectMocks
    private EnvioService envioService;

    private Envio envio;
    private EnvioDTO envioDTO;

    @BeforeEach
    void setUp() {
        envio = new Envio(1, 100, "Av. Libertador 1234, Depto 5B", "Santiago", "PREPARANDO", null);
        envioDTO = new EnvioDTO(1, 100, "Av. Libertador 1234, Depto 5B", "Santiago", "PREPARANDO", null);
    }

    @Test
    void crear_ConDatosValidos_DeberiaGuardarYRetornarDTO() {
        // Given
        when(repository.save(any(Envio.class))).thenReturn(envio);

        // When
        EnvioDTO resultado = envioService.crear(envioDTO);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdEnvio());
        assertEquals("PREPARANDO", resultado.getEstado());
        verify(repository, times(1)).save(any(Envio.class));
    }

    @Test
    void obtenerPorId_ConIdExistente_DeberiaRetornarEnvio() {
        // Given
        when(repository.findById(1)).thenReturn(Optional.of(envio));

        // When
        EnvioDTO resultado = envioService.obtenerPorId(1);

        // Then
        assertNotNull(resultado);
        assertEquals("Santiago", resultado.getCiudad());
    }

    @Test
    void obtenerPorId_ConIdInexistente_DeberiaLanzarRuntimeException() {
        // Given
        when(repository.findById(99)).thenReturn(Optional.empty());

        // When y Then
        assertThrows(RuntimeException.class, () -> envioService.obtenerPorId(99));
    }

    @Test
    void obtenerTodos_DeberiaRetornarListaDeEnvios() {
        // Given
        when(repository.findAll()).thenReturn(List.of(envio));

        // When
        List<EnvioDTO> resultado = envioService.obtenerTodos();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void actualizarEstado_ConIdExistente_DeberiaActualizarEstadoCorrectamente() {
        // Given
        when(repository.findById(1)).thenReturn(Optional.of(envio));
        when(repository.save(any(Envio.class))).thenReturn(envio);

        // When
        EnvioDTO resultado = envioService.actualizarEstado(1, "ENTREGADO");

        // Then
        assertNotNull(resultado);
        assertEquals("ENTREGADO", resultado.getEstado());
        verify(repository, times(1)).save(any(Envio.class));
    }

    @Test
    void actualizarEstado_ConIdInexistente_DeberiaLanzarRuntimeException() {
        // Given
        when(repository.findById(99)).thenReturn(Optional.empty());

        // When y Then
        assertThrows(RuntimeException.class, () -> envioService.actualizarEstado(99, "EN_RUTA"));
        verify(repository, never()).save(any(Envio.class));
    }

    @Test
    void actualizarEstado_ConNuevoEstadoEnRuta_DeberiaSetearFechaDespacho() {
        // Given
        when(repository.findById(1)).thenReturn(Optional.of(envio));
        when(repository.save(any(Envio.class))).thenReturn(envio);

        // When
        EnvioDTO resultado = envioService.actualizarEstado(1, "EN_RUTA");

        // Then
        assertEquals("EN_RUTA", resultado.getEstado());
        assertNotNull(resultado.getFechaDespacho());
    }
}
