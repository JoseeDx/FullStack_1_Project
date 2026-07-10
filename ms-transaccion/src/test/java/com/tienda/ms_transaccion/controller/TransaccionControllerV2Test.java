package com.tienda.ms_transaccion.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.tienda.ms_transaccion.assemblers.TransaccionModelAssembler;
import com.tienda.ms_transaccion.model.Transaccion;
import com.tienda.ms_transaccion.service.TransaccionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(TransaccionControllerV2.class)
@Import(TransaccionModelAssembler.class)
public class TransaccionControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransaccionService transaccionService;

    private Transaccion transaccion;

    @BeforeEach
    void setUp() {
        transaccion = new Transaccion(1, 1, 1, "Tarjeta de crédito", 59990.0, "PENDIENTE", LocalDateTime.now());
    }

    @Test
    public void testListarTransacciones() throws Exception {
        when(transaccionService.findAll()).thenReturn(List.of(transaccion));

        mockMvc.perform(get("/transacciones/v2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.transaccionList[0].id_transaccion").value(1))
                .andExpect(jsonPath("$._embedded.transaccionList[0].estado_pago").value("PENDIENTE"))
                .andExpect(jsonPath("$._links.self").exists());

        verify(transaccionService, times(1)).findAll();
    }

    @Test
    public void testObtenerTransaccion() throws Exception {
        when(transaccionService.findById(1)).thenReturn(transaccion);

        mockMvc.perform(get("/transacciones/v2/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_transaccion").value(1))
                .andExpect(jsonPath("$.estado_pago").value("PENDIENTE"))
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.transacciones").exists());

        verify(transaccionService, times(1)).findById(1);
    }
}
