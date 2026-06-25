package com.tienda.ms_facturacion.service;

import com.tienda.ms_facturacion.client.PedidoClient;
import com.tienda.ms_facturacion.client.PedidoResponse;
import com.tienda.ms_facturacion.exception.BadRequestException;
import com.tienda.ms_facturacion.exception.ResourceNotFoundException;
import com.tienda.ms_facturacion.model.Factura;
import com.tienda.ms_facturacion.repository.FacturaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacturaServiceTest {

    private FacturaService facturaService;

    @Mock
    private FacturaRepository facturaRepository;

    @Mock
    private PedidoClient pedidoClient;

    @BeforeEach
    void setUp() {
        facturaService = new FacturaService(facturaRepository, pedidoClient);
    }

    @Test
    void testGuardar() {
        when(pedidoClient.obtenerPedido(1L)).thenReturn(new PedidoResponse(1L, "CONFIRMADO"));

        Factura factura = new Factura(null, 1L, "12345678-5", 10000, 0, 0, null, null);
        when(facturaRepository.save(any(Factura.class))).thenReturn(factura);

        Factura resultado = facturaService.guardar(factura);

        assertNotNull(resultado);
        assertEquals("12345678-5", resultado.getRut_cliente());
        verify(facturaRepository).save(any(Factura.class));
    }

    @Test
    void testGuardar_ConRutInvalido_DeberiaLanzarBadRequestException() {
        Factura factura = new Factura(null, 1L, "11111111-9", 10000, 0, 0, null, null);

        assertThrows(BadRequestException.class, () -> facturaService.guardar(factura));
        verify(facturaRepository, never()).save(any(Factura.class));
    }

    @Test
    void testGuardar_ConSubtotalCero_DeberiaLanzarBadRequestException() {
        Factura factura = new Factura(null, 1L, "12345678-5", 0, 0, 0, null, null);

        assertThrows(BadRequestException.class, () -> facturaService.guardar(factura));
    }

    @Test
    void testListar() {
        when(facturaRepository.findAll()).thenReturn(java.util.List.of(
                new Factura(1L, 1L, "12345678-5", 10000, 1900, 11900, "EMITIDA", null)));

        java.util.List<Factura> facturas = facturaService.listar();

        assertNotNull(facturas);
        assertEquals(1, facturas.size());
        verify(facturaRepository).findAll();
    }

    @Test
    void testObtenerPorId() {
        Long id = 1L;
        Factura factura = new Factura(id, 1L, "12345678-5", 10000, 1900, 11900, "EMITIDA", null);
        when(facturaRepository.findById(id)).thenReturn(Optional.of(factura));

        Factura resultado = facturaService.obtenerPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId_factura());
        verify(facturaRepository).findById(id);
    }

    @Test
    void testObtenerPorId_NoExistente_DeberiaLanzarResourceNotFoundException() {
        when(facturaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> facturaService.obtenerPorId(99L));
    }

    @Test
    void testEliminar() {
        Long id = 1L;
        when(facturaRepository.existsById(id)).thenReturn(true);

        facturaService.eliminar(id);

        verify(facturaRepository).deleteById(id);
    }

    @Test
    void testCalcularIVA() {
        int iva = facturaService.calcularIVA(10000);
        assertEquals(1900, iva);
        assertThrows(IllegalArgumentException.class, () -> facturaService.calcularIVA(-100));
    }

    @Test
    void testCalcularTotal() {
        int total = facturaService.calcularTotal(10000, 1900);
        assertEquals(11900, total);
        assertThrows(IllegalArgumentException.class, () -> facturaService.calcularTotal(-1, 100));
    }

    @Test
    void testValidarRut_RutValido() {
        assertTrue(facturaService.validarRut("12345678-5"));
    }

    @Test
    void testValidarRut_RutInvalido() {
        assertFalse(facturaService.validarRut("11111111-9"));
    }
}