package com.tienda.ms_producto.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.tienda.ms_producto.assemblers.CategoriaModelAssembler;
import com.tienda.ms_producto.model.Categoria;
import com.tienda.ms_producto.service.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(CategoriaControllerV2.class)
@Import(CategoriaModelAssembler.class)
public class CategoriaControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoriaService categoriaService;

    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria(1, "Teclados", true);
    }

    @Test
    public void testListarCategorias() throws Exception {
        when(categoriaService.findAll()).thenReturn(List.of(categoria));

        mockMvc.perform(get("/categorias/v2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.categoriaList[0].id_categoria").value(1))
                .andExpect(jsonPath("$._embedded.categoriaList[0].nombre_categoria").value("Teclados"))
                .andExpect(jsonPath("$._links.self").exists());

        verify(categoriaService, times(1)).findAll();
    }

    @Test
    public void testObtenerCategoria() throws Exception {
        when(categoriaService.findById(1)).thenReturn(categoria);

        mockMvc.perform(get("/categorias/v2/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_categoria").value(1))
                .andExpect(jsonPath("$.nombre_categoria").value("Teclados"))
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.categorias").exists());

        verify(categoriaService, times(1)).findById(1);
    }
}
