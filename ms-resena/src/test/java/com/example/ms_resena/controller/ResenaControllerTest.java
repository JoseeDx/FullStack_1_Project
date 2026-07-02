package com.example.ms_resena.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.example.ms_resena.dto.ResenaDTO;
import com.example.ms_resena.service.ResenaService;
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
public class ResenaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ResenaService resenaService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private ResenaDTO resenaDTO;

    @BeforeEach
    void setUp() {
        resenaDTO = new ResenaDTO(
                1L,
                10L,
                5L,
                5,
                "Excelente producto, cumplió mis expectativas.",
                LocalDateTime.now()
        );

        ResenaController controller = new ResenaController(resenaService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testCrearResena() throws Exception {
        ResenaDTO nuevaResenaDTO = new ResenaDTO(null, 10L, 5L, 5,
                "Excelente producto, cumplió mis expectativas.", null);

        when(resenaService.crearResena(any(ResenaDTO.class))).thenReturn(resenaDTO);

        mockMvc.perform(post("/api/v1/resenas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevaResenaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idResena").value(1L))
                .andExpect(jsonPath("$.idProducto").value(10L))
                .andExpect(jsonPath("$.calificacion").value(5));
    }

    @Test
    public void testObtenerResenasPorProducto() throws Exception {
        when(resenaService.obtenerResenasPorProducto(10L)).thenReturn(List.of(resenaDTO));

        mockMvc.perform(get("/api/v1/resenas/producto/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idResena").value(1L))
                .andExpect(jsonPath("$[0].idProducto").value(10L))
                .andExpect(jsonPath("$[0].calificacion").value(5));
    }

    @Test
    public void testEliminarResena() throws Exception {
        doNothing().when(resenaService).eliminarResena(1L);

        mockMvc.perform(delete("/api/v1/resenas/1"))
                .andExpect(status().isNoContent());

        verify(resenaService, times(1)).eliminarResena(1L);
    }
}
