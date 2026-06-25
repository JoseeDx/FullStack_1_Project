package com.tienda.ms_facturacion.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.tienda.ms_facturacion.model.Factura;
import com.tienda.ms_facturacion.dto.FacturaDTO;
import com.tienda.ms_facturacion.service.FacturaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class FacturaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FacturaService facturaService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Factura factura;
    private FacturaDTO facturaDTO;

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

        facturaDTO = new FacturaDTO(
                null,
                1L,
                "12345678-5",
                10000
        );

        FacturaController controller = new FacturaController(facturaService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testListarFacturas() throws Exception {
        when(facturaService.listar()).thenReturn(List.of(factura));

        mockMvc.perform(get("/api/v1/facturas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id_factura").value(1L))
                .andExpect(jsonPath("$[0].rut_cliente").value("12345678-5"))
                .andExpect(jsonPath("$[0].total").value(11900));
    }

    @Test
    public void testObtenerPorId() throws Exception {
        when(facturaService.obtenerPorId(1L)).thenReturn(factura);

        mockMvc.perform(get("/api/v1/facturas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_factura").value(1L))
                .andExpect(jsonPath("$.rut_cliente").value("12345678-5"));
    }

    @Test
    public void testGuardarFactura() throws Exception {
        when(facturaService.guardar(any(Factura.class))).thenReturn(factura);

        mockMvc.perform(post("/api/v1/facturas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(facturaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id_factura").value(1L))
                .andExpect(jsonPath("$.total").value(11900));
    }

    @Test
    public void testEliminarFactura() throws Exception {
        doNothing().when(facturaService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/facturas/1"))
                .andExpect(status().isNoContent());

        verify(facturaService, times(1)).eliminar(1L);
    }
}