package com.tienda.ms_producto.service;

import com.tienda.ms_producto.exception.BadRequestException;
import com.tienda.ms_producto.exception.ResourceNotFoundException;
import com.tienda.ms_producto.model.Categoria;
import com.tienda.ms_producto.model.Producto;
import com.tienda.ms_producto.repository.CategoriaRepository;
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
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria(1, "Teclados", true);
    }

    @Test
    void findAll_DeberiaRetornarListaDeCategorias() {
        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));

        List<Categoria> resultado = categoriaService.findAll();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
    }

    @Test
    void findById_ConIdExistente_DeberiaRetornarCategoria() {
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));

        Categoria resultado = categoriaService.findById(1);

        assertNotNull(resultado);
        assertEquals("Teclados", resultado.getNombre_categoria());
    }

    @Test
    void findById_ConIdInexistente_DeberiaLanzarResourceNotFoundException() {
        when(categoriaRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoriaService.findById(99));
    }

    @Test
    void save_DeberiaGuardarCategoriaCorrectamente() {
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        Categoria resultado = categoriaService.save(categoria);

        assertNotNull(resultado);
        assertEquals("Teclados", resultado.getNombre_categoria());
    }

    @Test
    void actualizar_ConIdExistente_DeberiaActualizarCategoria() {
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        Categoria datosActualizados = new Categoria(null, "Mouses", true);
        Categoria resultado = categoriaService.actualizar(1, datosActualizados);

        assertNotNull(resultado);
        assertEquals("Mouses", resultado.getNombre_categoria());
    }

    @Test
    void actualizar_ConIdInexistente_DeberiaLanzarResourceNotFoundException() {
        when(categoriaRepository.findById(99)).thenReturn(Optional.empty());

        Categoria datosActualizados = new Categoria(null, "Mouses", true);
        assertThrows(ResourceNotFoundException.class, () -> categoriaService.actualizar(99, datosActualizados));
    }

    @Test
    void activar_DeberiaActivarCategoria() {
        categoria.setActivo(false);
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        Categoria resultado = categoriaService.activar(1);

        assertNotNull(resultado);
        assertTrue(resultado.getActivo());
    }

    @Test
    void delete_DeberiaEliminarCategoria() {
        doNothing().when(categoriaRepository).deleteById(1);

        categoriaService.delete(1);

        verify(categoriaRepository, times(1)).deleteById(1);
    }

    @Test
    void desactivar_ConProductosActivos_DeberiaLanzarBadRequestException() {
        when(productoRepository.findByCategoriaAndActivoTrue(1)).thenReturn(List.of(new Producto()));

        assertThrows(BadRequestException.class, () -> categoriaService.desactivar(1));
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }

    @Test
    void desactivar_SinProductosActivos_DeberiaDesactivarCategoria() {
        when(productoRepository.findByCategoriaAndActivoTrue(1)).thenReturn(List.of());
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        Categoria resultado = categoriaService.desactivar(1);

        assertNotNull(resultado);
        verify(categoriaRepository).save(any(Categoria.class));
    }

    @Test
    void findAll_ConErrorEnRepositorio_DeberiaLanzarRuntimeException() {
        when(categoriaRepository.findAll()).thenThrow(new RuntimeException("Error de BD"));

        assertThrows(RuntimeException.class, () -> categoriaService.findAll());
    }

    @Test
    void save_ConErrorEnRepositorio_DeberiaLanzarRuntimeException() {
        when(categoriaRepository.save(any(Categoria.class))).thenThrow(new RuntimeException("Error de BD"));

        assertThrows(RuntimeException.class, () -> categoriaService.save(categoria));
    }

    @Test
    void delete_ConProductosAsociados_DeberiaLanzarBadRequestException() {
        doThrow(new RuntimeException("foreign key constraint fails")).when(categoriaRepository).deleteById(1);

        assertThrows(BadRequestException.class, () -> categoriaService.delete(1));
    }

    @Test
    void delete_ConErrorGenerico_DeberiaLanzarRuntimeException() {
        doThrow(new RuntimeException("Error de BD")).when(categoriaRepository).deleteById(1);

        assertThrows(RuntimeException.class, () -> categoriaService.delete(1));
    }

    @Test
    void desactivar_ConIdInexistente_DeberiaLanzarRuntimeException() {
        when(productoRepository.findByCategoriaAndActivoTrue(99)).thenReturn(List.of());
        when(categoriaRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> categoriaService.desactivar(99));
        verify(categoriaRepository, never()).save(any(Categoria.class));
    }
}
