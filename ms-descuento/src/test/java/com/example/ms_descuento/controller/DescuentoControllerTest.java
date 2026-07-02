package com.example.ms_descuento.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.example.ms_descuento.assemblers.DescuentoModelAssembler;
import com.example.ms_descuento.dto.DescuentoDTO;
import com.example.ms_descuento.service.DescuentoService;
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

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class DescuentoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DescuentoService descuentoService;

    @Mock
    private DescuentoModelAssembler assembler;

    @InjectMocks
    private DescuentoController descuentoController;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private DescuentoDTO descuentoDTO;

    @BeforeEach
    void setUp() {
        descuentoDTO = new DescuentoDTO(
                1,
                "DESC10",
                10.0,
                LocalDateTime.now().plusDays(30),
                true
        );

        mockMvc = MockMvcBuilders.standaloneSetup(descuentoController).build();
    }

    @Test
    public void testCrearDescuento() throws Exception {
        when(descuentoService.crear(any(DescuentoDTO.class))).thenReturn(descuentoDTO);

        mockMvc.perform(post("/api/v1/descuentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(descuentoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idDescuento").value(1))
                .andExpect(jsonPath("$.codigoCupon").value("DESC10"))
                .andExpect(jsonPath("$.porcentaje").value(10.0));
    }

    @Test
    public void testObtenerPorId() throws Exception {
        when(descuentoService.obtenerPorId(1)).thenReturn(descuentoDTO);
        when(assembler.toModel(descuentoDTO)).thenReturn(EntityModel.of(descuentoDTO));

        mockMvc.perform(get("/api/v1/descuentos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDescuento").value(1))
                .andExpect(jsonPath("$.codigoCupon").value("DESC10"));
    }

    @Test
    public void testObtenerTodos() throws Exception {
        when(descuentoService.obtenerTodos()).thenReturn(List.of(descuentoDTO));
        when(assembler.toModel(descuentoDTO)).thenReturn(EntityModel.of(descuentoDTO));

        mockMvc.perform(get("/api/v1/descuentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idDescuento").value(1))
                .andExpect(jsonPath("$[0].codigoCupon").value("DESC10"));
    }

    @Test
    public void testActualizarDescuento() throws Exception {
        when(descuentoService.actualizar(eq(1), any(DescuentoDTO.class))).thenReturn(descuentoDTO);

        mockMvc.perform(put("/api/v1/descuentos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(descuentoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigoCupon").value("DESC10"));
    }

    @Test
    public void testEliminarDescuento() throws Exception {
        doNothing().when(descuentoService).eliminar(1);

        mockMvc.perform(delete("/api/v1/descuentos/1"))
                .andExpect(status().isNoContent());

        verify(descuentoService, times(1)).eliminar(1);
    }

    @Test
    public void testAplicarDescuento() throws Exception {
        when(descuentoService.calcularDescuento("DESC10", 100.0)).thenReturn(90.0);

        mockMvc.perform(get("/api/v1/descuentos/aplicar")
                        .param("codigo", "DESC10")
                        .param("total", "100.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalConDescuento").value(90.0));
    }
}
