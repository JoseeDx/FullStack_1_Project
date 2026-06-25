package com.tienda.ms_inventario.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.tienda.ms_inventario.model.Inventario;
import com.tienda.ms_inventario.dto.InventarioDTO;
import com.tienda.ms_inventario.service.InventarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class InventarioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InventarioService inventarioService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Inventario inventario;
    private InventarioDTO inventarioDTO;

    @BeforeEach
    void setUp() {
        inventario = new Inventario(
                1L,
                1,
                50,
                10,
                100,
                LocalDateTime.now()
        );

        inventarioDTO = new InventarioDTO(
                null,
                1,
                50,
                10,
                100
        );

        InventarioController controller = new InventarioController(inventarioService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testListarInventario() throws Exception {
        when(inventarioService.listar()).thenReturn(List.of(inventario));

        mockMvc.perform(get("/api/v1/inventario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id_inventario").value(1L))
                .andExpect(jsonPath("$[0].id_producto").value(1))
                .andExpect(jsonPath("$[0].stock_actual").value(50));
    }

    @Test
    public void testObtenerPorId() throws Exception {
        when(inventarioService.obtenerPorId(1L)).thenReturn(inventario);

        mockMvc.perform(get("/api/v1/inventario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_inventario").value(1L))
                .andExpect(jsonPath("$.stock_actual").value(50));
    }

    @Test
    public void testGuardarInventario() throws Exception {
        when(inventarioService.guardar(any(Inventario.class))).thenReturn(inventario);

        mockMvc.perform(post("/api/v1/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventarioDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id_inventario").value(1L))
                .andExpect(jsonPath("$.stock_actual").value(50));
    }

    @Test
    public void testEliminarInventario() throws Exception {
        doNothing().when(inventarioService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/inventario/1"))
                .andExpect(status().isNoContent());

        verify(inventarioService, times(1)).eliminar(1L);
    }

    @Test
    public void testActualizarInventario() throws Exception {
        when(inventarioService.actualizar(eq(1L), any(Inventario.class))).thenReturn(inventario);

        mockMvc.perform(put("/api/v1/inventario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inventarioDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock_actual").value(50));
    }

    @Test
    public void testObtenerBajoStock() throws Exception {
        when(inventarioService.obtenerBajoStock()).thenReturn(List.of(inventario));

        mockMvc.perform(get("/api/v1/inventario/bajo-stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id_inventario").value(1L));
    }
}