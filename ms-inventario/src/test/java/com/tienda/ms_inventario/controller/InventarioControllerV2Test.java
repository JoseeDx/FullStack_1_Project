package com.tienda.ms_inventario.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.tienda.ms_inventario.assemblers.InventarioModelAssembler;
import com.tienda.ms_inventario.model.Inventario;
import com.tienda.ms_inventario.service.InventarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(InventarioControllerV2.class)
@Import(InventarioModelAssembler.class)
public class InventarioControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventarioService inventarioService;

    private Inventario inventario;

    @BeforeEach
    void setUp() {
        inventario = new Inventario(1L, 1, 50, 10, 100, LocalDateTime.now());
    }

    @Test
    public void testListarInventario() throws Exception {
        when(inventarioService.listar()).thenReturn(List.of(inventario));

        mockMvc.perform(get("/inventario/v2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.inventarioList[0].id_inventario").value(1L))
                .andExpect(jsonPath("$._embedded.inventarioList[0].stock_actual").value(50))
                .andExpect(jsonPath("$._links.self").exists());

        verify(inventarioService, times(1)).listar();
    }

    @Test
    public void testObtenerInventario() throws Exception {
        when(inventarioService.obtenerPorId(1L)).thenReturn(inventario);

        mockMvc.perform(get("/inventario/v2/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_inventario").value(1L))
                .andExpect(jsonPath("$.stock_actual").value(50))
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.inventario").exists());

        verify(inventarioService, times(1)).obtenerPorId(1L);
    }
}
