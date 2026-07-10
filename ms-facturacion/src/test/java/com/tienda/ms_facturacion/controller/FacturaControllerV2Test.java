package com.tienda.ms_facturacion.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.tienda.ms_facturacion.assemblers.FacturaModelAssembler;
import com.tienda.ms_facturacion.model.Factura;
import com.tienda.ms_facturacion.service.FacturaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(FacturaControllerV2.class)
@Import(FacturaModelAssembler.class)
public class FacturaControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FacturaService facturaService;

    private Factura factura;

    @BeforeEach
    void setUp() {
        factura = new Factura(
                1L,
                1L,
                "12345678-5",
                10000,
                1900,
                11900,
                "EMITIDA",
                null
        );
    }

    @Test
    public void testListarFacturas() throws Exception {
        when(facturaService.listar()).thenReturn(List.of(factura));

        mockMvc.perform(get("/facturas/v2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.facturaList[0].id_factura").value(1L))
                .andExpect(jsonPath("$._embedded.facturaList[0].rut_cliente").value("12345678-5"))
                .andExpect(jsonPath("$._links.self").exists());

        verify(facturaService, times(1)).listar();
    }

    @Test
    public void testObtenerFactura() throws Exception {
        when(facturaService.obtenerPorId(1L)).thenReturn(factura);

        mockMvc.perform(get("/facturas/v2/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_factura").value(1L))
                .andExpect(jsonPath("$.rut_cliente").value("12345678-5"))
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.facturas").exists());

        verify(facturaService, times(1)).obtenerPorId(1L);
    }
}
