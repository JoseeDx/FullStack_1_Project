package com.example.ms_envio.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.example.ms_envio.assemblers.EnvioModelAssembler;
import com.example.ms_envio.dto.EnvioDTO;
import com.example.ms_envio.service.EnvioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class EnvioControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EnvioService envioService;

    @Mock
    private EnvioModelAssembler assembler;

    @InjectMocks
    private EnvioController envioController;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private EnvioDTO envioDTO;

    @BeforeEach
    void setUp() {
        envioDTO = new EnvioDTO(
                1,
                100,
                "Av. Libertador 1234, Depto 5B",
                "Santiago",
                "PREPARANDO",
                null
        );

        mockMvc = MockMvcBuilders.standaloneSetup(envioController).build();
    }

    @Test
    public void testCrearEnvio() throws Exception {
        when(envioService.crear(any(EnvioDTO.class))).thenReturn(envioDTO);

        mockMvc.perform(post("/api/v1/envios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(envioDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idEnvio").value(1))
                .andExpect(jsonPath("$.ciudad").value("Santiago"))
                .andExpect(jsonPath("$.estado").value("PREPARANDO"));
    }

    @Test
    public void testObtenerPorId() throws Exception {
        when(envioService.obtenerPorId(1)).thenReturn(envioDTO);
        when(assembler.toModel(envioDTO)).thenReturn(EntityModel.of(envioDTO));

        mockMvc.perform(get("/api/v1/envios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEnvio").value(1))
                .andExpect(jsonPath("$.direccion").value("Av. Libertador 1234, Depto 5B"));
    }

    @Test
    public void testObtenerTodos() throws Exception {
        when(envioService.obtenerTodos()).thenReturn(List.of(envioDTO));
        when(assembler.toModel(envioDTO)).thenReturn(EntityModel.of(envioDTO));

        mockMvc.perform(get("/api/v1/envios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idEnvio").value(1))
                .andExpect(jsonPath("$[0].ciudad").value("Santiago"));
    }

    @Test
    public void testActualizarEstado() throws Exception {
        EnvioDTO envioEnRuta = new EnvioDTO(1, 100, "Av. Libertador 1234, Depto 5B", "Santiago", "EN_RUTA", null);
        when(envioService.actualizarEstado(1, "EN_RUTA")).thenReturn(envioEnRuta);

        mockMvc.perform(patch("/api/v1/envios/1/estado")
                        .param("estado", "EN_RUTA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_RUTA"));
    }
}
