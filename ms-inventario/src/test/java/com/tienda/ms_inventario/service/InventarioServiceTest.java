package com.tienda.ms_inventario.service;

import com.tienda.ms_inventario.client.ProductoClient;
import com.tienda.ms_inventario.client.ProductoResponse;
import com.tienda.ms_inventario.exception.BadRequestException;
import com.tienda.ms_inventario.exception.ResourceNotFoundException;
import com.tienda.ms_inventario.model.Inventario;
import com.tienda.ms_inventario.repository.InventarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private ProductoClient productoClient;

    @InjectMocks
    private InventarioService inventarioService;

    private Inventario inventario;
    private ProductoResponse productoDTO;

    @BeforeEach
    void setUp() {
        inventario = new Inventario();
        inventario.setId_inventario(1L);
        inventario.setId_producto(1);
        inventario.setStock_actual(50);
        inventario.setStock_minimo(10);
        inventario.setStock_maximo(100);

        productoDTO = new ProductoResponse();
        productoDTO.setId_producto(1);
        productoDTO.setNombre_producto("Teclado Mecánico");
        productoDTO.setActivo(true);
    }

    @Test
    void guardar_ConDatosValidos_DeberiaGuardarCorrectamente() {
        // Given
        when(productoClient.obtenerProducto(1)).thenReturn(productoDTO);
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);

        // When
        Inventario resultado = inventarioService.guardar(inventario);

        // Then
        assertNotNull(resultado);
        assertEquals(50, resultado.getStock_actual());
        verify(productoClient, times(1)).obtenerProducto(1);
        verify(inventarioRepository, times(1)).save(any(Inventario.class));
    }

    @Test
    void guardar_ConStockActualNegativo_DeberiaLanzarBadRequestException() {
        // Given
        inventario.setStock_actual(-5);

        // When y Then
        assertThrows(BadRequestException.class, () -> inventarioService.guardar(inventario));
        verify(inventarioRepository, never()).save(any(Inventario.class));
        verify(productoClient, never()).obtenerProducto(any());
    }

    @Test
    void guardar_ConStockMaximoMenorAlMinimo_DeberiaLanzarBadRequestException() {
        // Given
        inventario.setStock_maximo(5);
        inventario.setStock_minimo(10);

        // When y Then
        assertThrows(BadRequestException.class, () -> inventarioService.guardar(inventario));
    }

    @Test
    void guardar_ConProductoNoExistente_DeberiaLanzarBadRequestException() {
        // Given
        when(productoClient.obtenerProducto(1)).thenThrow(new RuntimeException("Producto no encontrado"));

        // When y Then
        assertThrows(BadRequestException.class, () -> inventarioService.guardar(inventario));
        verify(inventarioRepository, never()).save(any(Inventario.class));
    }

    @Test
    void obtenerPorId_ConIdExistente_DeberiaRetornarInventario() {
        // Given
        when(inventarioRepository.findById(1L)).thenReturn(java.util.Optional.of(inventario));

        // When
        Inventario resultado = inventarioService.obtenerPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId_inventario());
    }

    @Test
    void obtenerPorId_ConIdInexistente_DeberiaLanzarResourceNotFoundException() {
        // Given
        when(inventarioRepository.findById(99L)).thenReturn(java.util.Optional.empty());

        // When y Then
        assertThrows(ResourceNotFoundException.class, () -> inventarioService.obtenerPorId(99L));
    }

    @Test
    void hayStockSuficiente_ConCantidadMenorAlStock_DeberiaRetornarTrue() {
        // Given
        when(inventarioRepository.findByIdProducto(1)).thenReturn(java.util.Optional.of(inventario));

        // When
        boolean resultado = inventarioService.hayStockSuficiente(1, 20);

        // Then
        assertTrue(resultado);
    }

    @Test
    void hayStockSuficiente_ConCantidadCeroONegativa_DeberiaLanzarBadRequestException() {
        // When y Then
        assertThrows(BadRequestException.class, () -> inventarioService.hayStockSuficiente(1, 0));
    }

    @Test
    void hayStockSuficiente_ConCantidadMayorAlStock_DeberiaRetornarFalse() {
        when(inventarioRepository.findByIdProducto(1)).thenReturn(java.util.Optional.of(inventario));

        boolean resultado = inventarioService.hayStockSuficiente(1, 100);

        assertFalse(resultado);
    }

    @Test
    void listar_DeberiaRetornarListaDeInventarios() {
        when(inventarioRepository.findAll()).thenReturn(java.util.List.of(inventario));

        java.util.List<Inventario> resultado = inventarioService.listar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(inventarioRepository).findAll();
    }

    @Test
    void obtenerPorIdProducto_ConIdExistente_DeberiaRetornarInventario() {
        when(inventarioRepository.findByIdProducto(1)).thenReturn(java.util.Optional.of(inventario));

        Inventario resultado = inventarioService.obtenerPorIdProducto(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId_producto());
    }

    @Test
    void obtenerPorIdProducto_ConIdInexistente_DeberiaLanzarResourceNotFoundException() {
        when(inventarioRepository.findByIdProducto(99)).thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inventarioService.obtenerPorIdProducto(99));
    }

    @Test
    void obtenerBajoStock_DeberiaRetornarListaDeInventariosBajoStock() {
        when(inventarioRepository.findProductosBajoStock()).thenReturn(java.util.List.of(inventario));

        java.util.List<Inventario> resultado = inventarioService.obtenerBajoStock();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(inventarioRepository).findProductosBajoStock();
    }

    @Test
    void actualizar_ConDatosValidos_DeberiaActualizarCorrectamente() {
        when(inventarioRepository.findById(1L)).thenReturn(java.util.Optional.of(inventario));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);

        Inventario datosActualizados = new Inventario(null, 1, 80, 10, 100, null);
        Inventario resultado = inventarioService.actualizar(1L, datosActualizados);

        assertNotNull(resultado);
        assertEquals(80, inventario.getStock_actual());
        verify(inventarioRepository).save(inventario);
    }

    @Test
    void actualizar_ConIdInexistente_DeberiaLanzarResourceNotFoundException() {
        when(inventarioRepository.findById(99L)).thenReturn(java.util.Optional.empty());

        Inventario datosActualizados = new Inventario(null, 1, 80, 10, 100, null);
        assertThrows(ResourceNotFoundException.class, () -> inventarioService.actualizar(99L, datosActualizados));
        verify(inventarioRepository, never()).save(any(Inventario.class));
    }

    @Test
    void actualizar_ConStockActualNegativo_DeberiaLanzarBadRequestException() {
        when(inventarioRepository.findById(1L)).thenReturn(java.util.Optional.of(inventario));

        Inventario datosActualizados = new Inventario(null, 1, -5, 10, 100, null);
        assertThrows(BadRequestException.class, () -> inventarioService.actualizar(1L, datosActualizados));
        verify(inventarioRepository, never()).save(any(Inventario.class));
    }

    @Test
    void actualizar_ConStockMaximoMenorAlMinimo_DeberiaLanzarBadRequestException() {
        when(inventarioRepository.findById(1L)).thenReturn(java.util.Optional.of(inventario));

        Inventario datosActualizados = new Inventario(null, 1, 50, 10, 5, null);
        assertThrows(BadRequestException.class, () -> inventarioService.actualizar(1L, datosActualizados));
        verify(inventarioRepository, never()).save(any(Inventario.class));
    }

    @Test
    void eliminar_ConIdExistente_DeberiaEliminarInventario() {
        when(inventarioRepository.existsById(1L)).thenReturn(true);

        inventarioService.eliminar(1L);

        verify(inventarioRepository).deleteById(1L);
    }

    @Test
    void eliminar_ConIdInexistente_DeberiaLanzarResourceNotFoundException() {
        when(inventarioRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> inventarioService.eliminar(99L));
        verify(inventarioRepository, never()).deleteById(anyLong());
    }
}