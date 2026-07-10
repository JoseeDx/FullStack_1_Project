package com.tienda.ms_producto.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.tienda.ms_producto.assemblers.ProductoModelAssembler;
import com.tienda.ms_producto.model.Categoria;
import com.tienda.ms_producto.model.Producto;
import com.tienda.ms_producto.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(ProductoControllerV2.class)
@Import(ProductoModelAssembler.class)
public class ProductoControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        Categoria categoria = new Categoria(1, "Teclados", true);
        producto = new Producto(1, "Teclado Mecánico", "Teclado con switches red", 59990.0, categoria, true);
    }

    @Test
    public void testListarProductos() throws Exception {
        when(productoService.findAll()).thenReturn(List.of(producto));

        mockMvc.perform(get("/productos/v2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.productoList[0].id_producto").value(1))
                .andExpect(jsonPath("$._embedded.productoList[0].nombre_producto").value("Teclado Mecánico"))
                .andExpect(jsonPath("$._links.self").exists());

        verify(productoService, times(1)).findAll();
    }

    @Test
    public void testObtenerProducto() throws Exception {
        when(productoService.findById(1)).thenReturn(producto);

        mockMvc.perform(get("/productos/v2/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_producto").value(1))
                .andExpect(jsonPath("$.nombre_producto").value("Teclado Mecánico"))
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.productos").exists());

        verify(productoService, times(1)).findById(1);
    }
}
