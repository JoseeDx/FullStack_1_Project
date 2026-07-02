package com.example.ms_descuento.service;

import com.example.ms_descuento.dto.DescuentoDTO;
import com.example.ms_descuento.model.Descuento;
import com.example.ms_descuento.repository.DescuentoRepository;
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
class DescuentoServiceTest {

    @Mock
    private DescuentoRepository repository;

    @InjectMocks
    private DescuentoService descuentoService;

    private Descuento descuento;
    private DescuentoDTO descuentoDTO;

    @BeforeEach
    void setUp() {
        descuento = new Descuento(1, "DESC10", 10.0, LocalDateTime.now().plusDays(30), true);
        descuentoDTO = new DescuentoDTO(1, "DESC10", 10.0, LocalDateTime.now().plusDays(30), true);
    }

    @Test
    void calcularDescuento_ConCuponValido_DeberiaRetornarMontoConDescuento() {
        // Given
        when(repository.findByCodigoCupon("DESC10")).thenReturn(Optional.of(descuento));

        // When
        Double resultado = descuentoService.calcularDescuento("DESC10", 100.0);

        // Then
        assertEquals(90.0, resultado);
    }

    @Test
    void calcularDescuento_ConCuponInexistente_DeberiaLanzarRuntimeException() {
        // Given
        when(repository.findByCodigoCupon("NOEXISTE")).thenReturn(Optional.empty());

        // When y Then
        assertThrows(RuntimeException.class, () -> descuentoService.calcularDescuento("NOEXISTE", 100.0));
    }

    @Test
    void calcularDescuento_ConCuponInactivo_DeberiaLanzarRuntimeException() {
        // Given
        descuento.setActivo(false);
        when(repository.findByCodigoCupon("DESC10")).thenReturn(Optional.of(descuento));

        // When y Then
        assertThrows(RuntimeException.class, () -> descuentoService.calcularDescuento("DESC10", 100.0));
    }

    @Test
    void calcularDescuento_ConCuponExpirado_DeberiaLanzarRuntimeException() {
        // Given
        descuento.setFechaExpiracion(LocalDateTime.now().minusDays(1));
        when(repository.findByCodigoCupon("DESC10")).thenReturn(Optional.of(descuento));

        // When y Then
        assertThrows(RuntimeException.class, () -> descuentoService.calcularDescuento("DESC10", 100.0));
    }

    @Test
    void crear_ConDatosValidos_DeberiaGuardarYRetornarDTO() {
        // Given
        when(repository.save(any(Descuento.class))).thenReturn(descuento);

        // When
        DescuentoDTO resultado = descuentoService.crear(descuentoDTO);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdDescuento());
        verify(repository, times(1)).save(any(Descuento.class));
    }

    @Test
    void obtenerPorId_ConIdExistente_DeberiaRetornarDescuento() {
        // Given
        when(repository.findById(1)).thenReturn(Optional.of(descuento));

        // When
        DescuentoDTO resultado = descuentoService.obtenerPorId(1);

        // Then
        assertNotNull(resultado);
        assertEquals("DESC10", resultado.getCodigoCupon());
    }

    @Test
    void obtenerPorId_ConIdInexistente_DeberiaLanzarRuntimeException() {
        // Given
        when(repository.findById(99)).thenReturn(Optional.empty());

        // When y Then
        assertThrows(RuntimeException.class, () -> descuentoService.obtenerPorId(99));
    }

    @Test
    void obtenerTodos_DeberiaRetornarListaDeDescuentos() {
        // Given
        when(repository.findAll()).thenReturn(List.of(descuento));

        // When
        List<DescuentoDTO> resultado = descuentoService.obtenerTodos();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void actualizar_ConIdExistente_DeberiaActualizarCorrectamente() {
        // Given
        when(repository.findById(1)).thenReturn(Optional.of(descuento));
        when(repository.save(any(Descuento.class))).thenReturn(descuento);

        // When
        DescuentoDTO resultado = descuentoService.actualizar(1, descuentoDTO);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdDescuento());
        verify(repository, times(1)).save(any(Descuento.class));
    }

    @Test
    void actualizar_ConIdInexistente_DeberiaLanzarRuntimeException() {
        // Given
        when(repository.findById(99)).thenReturn(Optional.empty());

        // When y Then
        assertThrows(RuntimeException.class, () -> descuentoService.actualizar(99, descuentoDTO));
        verify(repository, never()).save(any(Descuento.class));
    }

    @Test
    void eliminar_DeberiaEliminarCorrectamente() {
        // When
        descuentoService.eliminar(1);

        // Then
        verify(repository, times(1)).deleteById(1);
    }
}
