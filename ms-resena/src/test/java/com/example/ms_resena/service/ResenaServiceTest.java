package com.example.ms_resena.service;

import com.example.ms_resena.dto.ResenaDTO;
import com.example.ms_resena.exception.BadRequestException;
import com.example.ms_resena.exception.ResourceNotFoundException;
import com.example.ms_resena.model.Resena;
import com.example.ms_resena.repository.ResenaRepository;
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
class ResenaServiceTest {

    @Mock
    private ResenaRepository resenaRepository;

    @InjectMocks
    private ResenaService resenaService;

    private Resena resena;
    private ResenaDTO resenaDTO;

    @BeforeEach
    void setUp() {
        resena = new Resena();
        resena.setIdResena(1L);
        resena.setIdProducto(10L);
        resena.setIdCliente(5L);
        resena.setCalificacion(5);
        resena.setComentario("Excelente producto, cumplió mis expectativas.");
        resena.setFechaCreacion(LocalDateTime.now());

        resenaDTO = new ResenaDTO(null, 10L, 5L, 5,
                "Excelente producto, cumplió mis expectativas.", null);
    }

    @Test
    void crearResena_ConDatosValidos_DeberiaGuardarCorrectamente() {
        // Given
        when(resenaRepository.save(any(Resena.class))).thenReturn(resena);

        // When
        ResenaDTO resultado = resenaService.crearResena(resenaDTO);

        // Then
        assertNotNull(resultado);
        assertEquals(5, resultado.getCalificacion());
        verify(resenaRepository, times(1)).save(any(Resena.class));
    }

    @Test
    void crearResena_ConCalificacionNula_DeberiaLanzarBadRequestException() {
        // Given
        resenaDTO.setCalificacion(null);

        // When y Then
        assertThrows(BadRequestException.class, () -> resenaService.crearResena(resenaDTO));
        verify(resenaRepository, never()).save(any(Resena.class));
    }

    @Test
    void crearResena_ConCalificacionFueraDeRango_DeberiaLanzarBadRequestException() {
        // Given
        resenaDTO.setCalificacion(6);

        // When y Then
        assertThrows(BadRequestException.class, () -> resenaService.crearResena(resenaDTO));
        verify(resenaRepository, never()).save(any(Resena.class));
    }

    @Test
    void obtenerResenasPorProducto_ConResenasExistentes_DeberiaRetornarLista() {
        // Given
        when(resenaRepository.findByIdProducto(10L)).thenReturn(List.of(resena));

        // When
        List<ResenaDTO> resultado = resenaService.obtenerResenasPorProducto(10L);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(10L, resultado.get(0).getIdProducto());
        verify(resenaRepository, times(1)).findByIdProducto(10L);
    }

    @Test
    void obtenerResenasPorProducto_SinResenas_DeberiaRetornarListaVacia() {
        // Given
        when(resenaRepository.findByIdProducto(99L)).thenReturn(List.of());

        // When
        List<ResenaDTO> resultado = resenaService.obtenerResenasPorProducto(99L);

        // Then
        assertTrue(resultado.isEmpty());
    }

    @Test
    void eliminarResena_ConIdExistente_DeberiaEliminarCorrectamente() {
        // Given
        when(resenaRepository.findById(1L)).thenReturn(Optional.of(resena));

        // When
        resenaService.eliminarResena(1L);

        // Then
        verify(resenaRepository, times(1)).delete(resena);
    }

    @Test
    void eliminarResena_ConIdInexistente_DeberiaLanzarResourceNotFoundException() {
        // Given
        when(resenaRepository.findById(99L)).thenReturn(Optional.empty());

        // When y Then
        assertThrows(ResourceNotFoundException.class, () -> resenaService.eliminarResena(99L));
        verify(resenaRepository, never()).delete(any(Resena.class));
    }
}
