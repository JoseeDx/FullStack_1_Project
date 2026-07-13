package com.example.ms_descuento.controller;

import com.example.ms_descuento.assemblers.DescuentoModelAssemblerV2;
import com.example.ms_descuento.dto.DescuentoDTO;
import com.example.ms_descuento.service.DescuentoService;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DescuentoControllerV2Test {

    private MockMvc mockMvc;
    private DescuentoService service;
    private DescuentoModelAssemblerV2 assembler;

    private DescuentoDTO descuentoDTO;

    @BeforeEach
    void setUp() {
        service = mock(DescuentoService.class);
        assembler = mock(DescuentoModelAssemblerV2.class);

        // Datos de prueba
        descuentoDTO = new DescuentoDTO(1, "PROMO10", 10.0, LocalDateTime.now().plusDays(5), true);

        // Instanciamos controller e inyectamos mocks
        DescuentoControllerV2 controller = new DescuentoControllerV2();
        ReflectionTestUtils.setField(controller, "service", service);
        ReflectionTestUtils.setField(controller, "assemblerV2", assembler);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("V2: Obtener descuento por ID devuelve 200 y payload HATEOAS")
    void dadoIdExistente_cuandoObtenerPorId_entoncesRetorna200() throws Exception {
        given(service.obtenerPorId(anyInt())).willReturn(descuentoDTO);
        given(assembler.toModel(any(DescuentoDTO.class))).willReturn(EntityModel.of(descuentoDTO));

        mockMvc.perform(get("/api/v2/descuentos/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDescuento").value(descuentoDTO.getIdDescuento()))
                .andExpect(jsonPath("$.codigoCupon").value(descuentoDTO.getCodigoCupon()));
    }

    @Test
    @DisplayName("V2: Listar todos los descuentos devuelve 200")
    void cuandoListarTodos_entoncesRetorna200() throws Exception {
        List<DescuentoDTO> lista = Arrays.asList(descuentoDTO);
        given(service.obtenerTodos()).willReturn(lista);
        given(assembler.toModel(any(DescuentoDTO.class))).willReturn(EntityModel.of(descuentoDTO));

        mockMvc.perform(get("/api/v2/descuentos").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("V2: Aplicar descuento devuelve total con descuento")
    void dadoCodigoYTotal_cuandoAplicarDescuento_entoncesRetornaTotalConDescuento() throws Exception {
        given(service.calcularDescuento("PROMO10", 200.0)).willReturn(180.0);

        mockMvc.perform(get("/api/v2/descuentos/aplicar")
                        .param("codigo", "PROMO10")
                        .param("total", "200.0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalConDescuento").value(180.0));
    }
}
