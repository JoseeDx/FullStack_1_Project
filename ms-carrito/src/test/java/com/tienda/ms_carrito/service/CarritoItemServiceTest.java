package com.tienda.ms_carrito.service;

import com.tienda.ms_carrito.client.ClienteClient;
import com.tienda.ms_carrito.client.ProductoClient;
import com.tienda.ms_carrito.client.ProductoResponse;
import com.tienda.ms_carrito.exception.BadRequestException;
import com.tienda.ms_carrito.exception.ResourceNotFoundException;
import com.tienda.ms_carrito.model.CarritoItem;
import com.tienda.ms_carrito.repository.CarritoItemRepository;
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
class CarritoItemServiceTest {

    @Mock
    private CarritoItemRepository carritoItemRepository;

    @Mock
    private ClienteClient clienteClient;

    @Mock
    private ProductoClient productoClient;

    @InjectMocks
    private CarritoItemService carritoItemService;

    private CarritoItem carritoItem;
    private ProductoResponse productoResponse;

    @BeforeEach
    void setUp() {
        carritoItem = new CarritoItem();
        carritoItem.setId_carrito(1);
        carritoItem.setId_cliente(1);
        carritoItem.setId_producto(1);
        carritoItem.setCantidad(2);
        carritoItem.setPrecio_unitario(29990.0);
        carritoItem.setFecha_agregado(LocalDateTime.now());

        productoResponse = new ProductoResponse();
        productoResponse.setId_producto(1);
        productoResponse.setNombre_producto("Teclado Mecánico");
        productoResponse.setPrecio_producto(29990.0);
        productoResponse.setActivo(true);
    }

    @Test
    void save_ConDatosValidos_DeberiaGuardarCorrectamente() {
        when(clienteClient.existeCliente(1L)).thenReturn(true);
        when(productoClient.findById(1)).thenReturn(productoResponse);
        when(carritoItemRepository.save(any(CarritoItem.class))).thenReturn(carritoItem);

        CarritoItem resultado = carritoItemService.save(carritoItem);

        assertNotNull(resultado);
        assertEquals(29990.0, resultado.getPrecio_unitario());
        verify(clienteClient, times(1)).existeCliente(1L);
        verify(productoClient, times(1)).findById(1);
        verify(carritoItemRepository, times(1)).save(any(CarritoItem.class));
    }

    @Test
    void save_ConClienteNoExistente_DeberiaLanzarBadRequestException() {
        when(clienteClient.existeCliente(1L)).thenReturn(false);

        assertThrows(BadRequestException.class, () -> carritoItemService.save(carritoItem));
        verify(carritoItemRepository, never()).save(any(CarritoItem.class));
    }

    @Test
    void save_ConProductoNoExistente_DeberiaLanzarBadRequestException() {
        when(clienteClient.existeCliente(1L)).thenReturn(true);
        when(productoClient.findById(1)).thenReturn(null);

        assertThrows(BadRequestException.class, () -> carritoItemService.save(carritoItem));
        verify(carritoItemRepository, never()).save(any(CarritoItem.class));
    }

    @Test
    void save_ConProductoInactivo_DeberiaLanzarBadRequestException() {
        productoResponse.setActivo(false);
        when(clienteClient.existeCliente(1L)).thenReturn(true);
        when(productoClient.findById(1)).thenReturn(productoResponse);

        assertThrows(BadRequestException.class, () -> carritoItemService.save(carritoItem));
        verify(carritoItemRepository, never()).save(any(CarritoItem.class));
    }

    @Test
    void findById_ConIdExistente_DeberiaRetornarItem() {
        when(carritoItemRepository.findById(1)).thenReturn(Optional.of(carritoItem));

        CarritoItem resultado = carritoItemService.findById(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId_carrito());
    }

    @Test
    void findById_ConIdInexistente_DeberiaLanzarResourceNotFoundException() {
        when(carritoItemRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> carritoItemService.findById(99));
    }

    @Test
    void findByIdCliente_DeberiaRetornarListaDeItems() {
        when(carritoItemRepository.findByIdCliente(1)).thenReturn(List.of(carritoItem));

        List<CarritoItem> resultado = carritoItemService.findByIdCliente(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    void getTotalByIdCliente_DeberiaCalcularElTotalCorrectamente() {
        when(carritoItemRepository.findByIdCliente(1)).thenReturn(List.of(carritoItem));

        Double total = carritoItemService.getTotalByIdCliente(1);

        assertEquals(59980.0, total);
    }

    @Test
    void actualizar_ConDatosValidos_DeberiaActualizarCorrectamente() {
        when(carritoItemRepository.findById(1)).thenReturn(Optional.of(carritoItem));
        when(clienteClient.existeCliente(1L)).thenReturn(true);
        when(productoClient.findById(1)).thenReturn(productoResponse);
        when(carritoItemRepository.save(any(CarritoItem.class))).thenReturn(carritoItem);

        CarritoItem datosActualizados = new CarritoItem(null, 1, 1, null, LocalDateTime.now(), 5);
        CarritoItem resultado = carritoItemService.actualizar(1, datosActualizados);

        assertNotNull(resultado);
        verify(carritoItemRepository).save(any(CarritoItem.class));
    }

    @Test
    void actualizar_ConIdInexistente_DeberiaLanzarResourceNotFoundException() {
        when(carritoItemRepository.findById(99)).thenReturn(Optional.empty());

        CarritoItem datosActualizados = new CarritoItem(null, 1, 1, null, LocalDateTime.now(), 5);
        assertThrows(ResourceNotFoundException.class, () -> carritoItemService.actualizar(99, datosActualizados));
    }

    @Test
    void delete_DeberiaEliminarElItem() {
        doNothing().when(carritoItemRepository).deleteById(1);

        carritoItemService.delete(1);

        verify(carritoItemRepository, times(1)).deleteById(1);
    }
}
