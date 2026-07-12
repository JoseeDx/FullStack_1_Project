package com.example.ms_envio.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.ms_envio.dto.EnvioDTO;
import com.example.ms_envio.model.Envio;
import com.example.ms_envio.repository.EnvioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class EnvioServiceTest {

    @Mock
    private EnvioRepository repository;

    @InjectMocks
    private EnvioService service;

    private Envio envioEntidad;
    private EnvioDTO envioDTO;

    @BeforeEach
    void setUp() {
        // Inicializamos datos simulados antes de cada prueba
        envioEntidad = new Envio(1, 100, "Avenida Siempreviva 742", "Springfield", "PREPARANDO", null);
        envioDTO = new EnvioDTO(null, 100, "Avenida Siempreviva 742", "Springfield", null, null);
    }

    @Test
    void crearEnvio_Exito() {
        // GIVEN (Dado)
        when(repository.save(any(Envio.class))).thenReturn(envioEntidad);

        // WHEN (Cuando)
        EnvioDTO resultado = service.crear(envioDTO);

        // THEN (Entonces)
        assertNotNull(resultado);
        assertEquals(1, resultado.getIdEnvio());
        assertEquals("PREPARANDO", resultado.getEstado());
        verify(repository, times(1)).save(any(Envio.class));
    }

    @Test
    void obtenerPorId_Exito() {
        // GIVEN
        when(repository.findById(1)).thenReturn(Optional.of(envioEntidad));

        // WHEN
        EnvioDTO resultado = service.obtenerPorId(1);

        // THEN
        assertNotNull(resultado);
        assertEquals(100, resultado.getIdPedido());
        verify(repository, times(1)).findById(1);
    }

    @Test
    void obtenerPorId_NoEncontrado_LanzaExcepcion() {
        // GIVEN
        when(repository.findById(99)).thenReturn(Optional.empty());

        // WHEN / THEN
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.obtenerPorId(99);
        });
        assertEquals("Envío no encontrado", exception.getMessage());
        verify(repository, times(1)).findById(99);
    }

    @Test
    void obtenerTodos_Exito() {
        // GIVEN
        when(repository.findAll()).thenReturn(Arrays.asList(envioEntidad));

        // WHEN
        List<EnvioDTO> resultados = service.obtenerTodos();

        // THEN
        assertFalse(resultados.isEmpty());
        assertEquals(1, resultados.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void actualizarEstado_A_EnRuta_AsignaFecha() {
        // GIVEN: Simulamos que el envío existe
        when(repository.findById(1)).thenReturn(Optional.of(envioEntidad));
        // Simulamos el objeto actualizado que guardará el repositorio
        Envio envioActualizado = new Envio(1, 100, "Avenida Siempreviva 742", "Springfield", "EN_RUTA", LocalDateTime.now());
        when(repository.save(any(Envio.class))).thenReturn(envioActualizado);

        // WHEN
        EnvioDTO resultado = service.actualizarEstado(1, "EN_RUTA");

        // THEN
        assertEquals("EN_RUTA", resultado.getEstado());
        assertNotNull(resultado.getFechaDespacho()); // La regla de negocio indica que ya no debe ser nulo
        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(any(Envio.class));
    }
}