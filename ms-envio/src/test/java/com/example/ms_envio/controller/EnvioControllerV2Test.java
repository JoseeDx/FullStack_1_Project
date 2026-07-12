package com.example.ms_envio.controller;

import com.example.ms_envio.assemblers.EnvioModelAssemblerV2;
import com.example.ms_envio.dto.EnvioDTO;
import com.example.ms_envio.service.EnvioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnvioControllerV2.class)
public class EnvioControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnvioService service;

    // ¡Importante! Mockeamos el assembler de la V2 para que el test levante correctamente
    @MockBean
    private EnvioModelAssemblerV2 assemblerV2;

    @Autowired
    private ObjectMapper objectMapper;

    private EnvioDTO envioDTO;

    @BeforeEach
    void setUp() {
        // Preparamos datos genéricos antes de cada prueba
        envioDTO = new EnvioDTO(1, 100, "Calle Falsa 123", "Santiago", "PREPARANDO", LocalDateTime.now());
    }

    @Test
    @DisplayName("Test V2: Crear envío exitosamente")
    void dadoEnvioValido_cuandoCrear_entoncesRetorna201() throws Exception {
        // Given
        given(service.crear(any(EnvioDTO.class))).willReturn(envioDTO);

        // When & Then
        mockMvc.perform(post("/api/v2/envios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(envioDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idEnvio").value(envioDTO.getIdEnvio()))
                .andExpect(jsonPath("$.estado").value(envioDTO.getEstado()));
    }

    @Test
    @DisplayName("Test V2: Obtener envío por ID")
    void dadoIdExistente_cuandoObtenerPorId_entoncesRetorna200() throws Exception {
        // Given
        given(service.obtenerPorId(anyInt())).willReturn(envioDTO);
        // Simulamos la conversión de DTO a EntityModel que hace el AssemblerV2
        given(assemblerV2.toModel(any(EnvioDTO.class))).willReturn(EntityModel.of(envioDTO));

        // When & Then
        mockMvc.perform(get("/api/v2/envios/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEnvio").value(envioDTO.getIdEnvio()))
                .andExpect(jsonPath("$.ciudad").value(envioDTO.getCiudad()));
    }

    @Test
    @DisplayName("Test V2: Obtener todos los envíos")
    void cuandoObtenerTodos_entoncesRetornaListaY200() throws Exception {
        // Given
        List<EnvioDTO> envios = Arrays.asList(envioDTO);
        given(service.obtenerTodos()).willReturn(envios);
        given(assemblerV2.toModel(any(EnvioDTO.class))).willReturn(EntityModel.of(envioDTO));

        // When & Then
        mockMvc.perform(get("/api/v2/envios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idEnvio").value(envioDTO.getIdEnvio()));
    }

    @Test
    @DisplayName("Test V2: Actualizar el estado de un envío a EN_RUTA")
    void dadoEstadoValido_cuandoActualizarEstado_entoncesRetorna200() throws Exception {
        // Given
        envioDTO.setEstado("EN_RUTA");
        given(service.actualizarEstado(anyInt(), anyString())).willReturn(envioDTO);

        // When & Then
        mockMvc.perform(patch("/api/v2/envios/{id}/estado", 1)
                .param("estado", "EN_RUTA")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_RUTA"));
    }
}