package com.tienda.ms_producto.service;

import com.tienda.ms_producto.exception.ResourceNotFoundException;
import com.tienda.ms_producto.model.Categoria;
import com.tienda.ms_producto.model.Producto;
import com.tienda.ms_producto.repository.ProductoRepository;
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
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        Categoria categoria = new Categoria(1, "Teclados", true);
        producto = new Producto(1, "Teclado Mecánico", "Teclado con switches red", 59990.0, categoria, true);
    }

    @Test
    void findAll_DeberiaRetornarListaDeProductos() {
        when(productoRepository.findAll()).thenReturn(List.of(producto));

        List<Producto> resultado = productoService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(productoRepository).findAll();
    }

    @Test
    void findById_ConIdExistente_DeberiaRetornarProducto() {
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));

        Producto resultado = productoService.findById(1);

        assertNotNull(resultado);
        assertEquals("Teclado Mecánico", resultado.getNombre_producto());
    }

    @Test
    void findById_ConIdInexistente_DeberiaLanzarResourceNotFoundException() {
        when(productoRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productoService.findById(99));
    }

    @Test
    void save_DeberiaGuardarProductoCorrectamente() {
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto resultado = productoService.save(producto);

        assertNotNull(resultado);
        assertEquals(59990.0, resultado.getPrecio_producto());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void actualizar_ConIdExistente_DeberiaActualizarProducto() {
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Categoria nuevaCategoria = new Categoria(2, "Mouses", true);
        Producto datosActualizados = new Producto(null, "Mouse Gamer", "Mouse con sensor óptico", 39990.0, nuevaCategoria, true);
        Producto resultado = productoService.actualizar(1, datosActualizados);

        assertNotNull(resultado);
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void actualizar_ConIdInexistente_DeberiaLanzarResourceNotFoundException() {
        when(productoRepository.findById(99)).thenReturn(Optional.empty());

        Producto datosActualizados = new Producto(null, "Mouse Gamer", "Descripcion", 39990.0, null, true);
        assertThrows(ResourceNotFoundException.class, () -> productoService.actualizar(99, datosActualizados));
    }

    @Test
    void activar_DeberiaActivarProducto() {
        producto.setActivo(false);
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto resultado = productoService.activar(1);

        assertNotNull(resultado);
        assertTrue(resultado.getActivo());
    }

    @Test
    void desactivar_DeberiaDesactivarProducto() {
        when(productoRepository.findById(1)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        productoService.desactivar(1);

        assertFalse(producto.getActivo());
    }

    @Test
    void delete_DeberiaEliminarProducto() {
        doNothing().when(productoRepository).deleteById(1);

        productoService.delete(1);

        verify(productoRepository, times(1)).deleteById(1);
    }

    @Test
    void findAll_ConErrorEnRepositorio_DeberiaLanzarRuntimeException() {
        when(productoRepository.findAll()).thenThrow(new RuntimeException("Error de BD"));

        assertThrows(RuntimeException.class, () -> productoService.findAll());
    }

    @Test
    void save_ConErrorEnRepositorio_DeberiaLanzarRuntimeException() {
        when(productoRepository.save(any(Producto.class))).thenThrow(new RuntimeException("Error de BD"));

        assertThrows(RuntimeException.class, () -> productoService.save(producto));
    }

    @Test
    void delete_ConErrorEnRepositorio_DeberiaLanzarRuntimeException() {
        doThrow(new RuntimeException("Error de BD")).when(productoRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> productoService.delete(1));
    }
}
