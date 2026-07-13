package com.example.ms_resena.controller;

import com.example.ms_resena.assemblers.ResenaModelAssembler;
import com.example.ms_resena.dto.ResenaDTO;
import com.example.ms_resena.model.Resena;
import com.example.ms_resena.service.ResenaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResenaControllerV2Test {

    private MockMvc mockMvc;
    private ResenaService resenaService;
    private ResenaModelAssembler assembler;

    private Resena resenaEntidad;
    private ResenaDTO resenaDTO;

    @BeforeEach
    void setUp() {
        resenaService = mock(ResenaService.class);
        assembler = mock(ResenaModelAssembler.class);

        resenaEntidad = new Resena(1L, 100L, 200L, 5, "Excelente", LocalDateTime.now());
        resenaDTO = ResenaDTO.fromModel(resenaEntidad);

        ResenaControllerV2 controller = new ResenaControllerV2(resenaService, assembler);
        ReflectionTestUtils.setField(controller, "resenaService", resenaService);
        ReflectionTestUtils.setField(controller, "assembler", assembler);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("V2: Listar reseñas devuelve 200")
    void cuandoListarResenas_entoncesRetorna200() throws Exception {
        List<ResenaDTO> lista = Arrays.asList(resenaDTO);
        given(resenaService.listar()).willReturn(lista);
        given(assembler.toModel(any(ResenaDTO.class))).willReturn(EntityModel.of(resenaDTO));

        mockMvc.perform(get("/resenas/v2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("V2: Obtener reseña por ID devuelve 200 y payload")
    void dadoIdExistente_cuandoObtenerResenaPorId_entoncesRetorna200() throws Exception {
        given(resenaService.obtenerPorId(anyLong())).willReturn(resenaDTO);
        given(assembler.toModel(any(ResenaDTO.class))).willReturn(EntityModel.of(resenaDTO));

        mockMvc.perform(get("/resenas/v2/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idResena").value(resenaDTO.getIdResena()))
                .andExpect(jsonPath("$.calificacion").value(resenaDTO.getCalificacion()));
    }
}