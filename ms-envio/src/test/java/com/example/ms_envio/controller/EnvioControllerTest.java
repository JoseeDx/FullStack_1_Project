package com.example.ms_envio.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.ms_envio.assemblers.EnvioModelAssembler;
import com.example.ms_envio.dto.EnvioDTO;
import com.example.ms_envio.service.EnvioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EnvioController.class)
class EnvioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnvioService service;

    @MockBean
    private EnvioModelAssembler assembler;

    @Autowired
    private ObjectMapper objectMapper;

    private EnvioDTO envioDTO;

    @BeforeEach
    void setUp() {
        envioDTO = new EnvioDTO(1, 100, "Avenida Siempreviva 742", "Springfield", "PREPARANDO", null);
    }

    @Test
    void crearEnvio_RetornaCreated() throws Exception {
        // GIVEN
        when(service.crear(any(EnvioDTO.class))).thenReturn(envioDTO);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/envios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envioDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idEnvio").value(1))
                .andExpect(jsonPath("$.estado").value("PREPARANDO"));
    }

    @Test
    void obtenerPorId_RetornaOk() throws Exception {
        // GIVEN
        when(service.obtenerPorId(1)).thenReturn(envioDTO);
        when(assembler.toModel(any(EnvioDTO.class))).thenReturn(EntityModel.of(envioDTO));

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/envios/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEnvio").value(1));
    }

    @Test
    void actualizarEstado_RetornaOk() throws Exception {
        // GIVEN
        EnvioDTO envioActualizado = new EnvioDTO(1, 100, "Avenida Siempreviva 742", "Springfield", "ENTREGADO", null);
        when(service.actualizarEstado(eq(1), anyString())).thenReturn(envioActualizado);

        // WHEN & THEN
        mockMvc.perform(patch("/api/v1/envios/1/estado")
                .param("estado", "ENTREGADO")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ENTREGADO"));
    }
}