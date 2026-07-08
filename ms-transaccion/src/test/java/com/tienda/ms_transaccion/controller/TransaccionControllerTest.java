package com.tienda.ms_transaccion.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.tienda.ms_transaccion.dto.TransaccionDTO;
import com.tienda.ms_transaccion.model.Transaccion;
import com.tienda.ms_transaccion.service.TransaccionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
public class TransaccionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransaccionService transaccionService;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private Transaccion transaccion;
    private TransaccionDTO transaccionDTO;

    @BeforeEach
    void setUp() {
        LocalDateTime fecha = LocalDateTime.now();

        transaccion = new Transaccion(1, 1, 1, "Tarjeta de crédito", 59990.0, "PENDIENTE", fecha);

        transaccionDTO = TransaccionDTO.builder()
                .id_transaccion(null)
                .id_pedido(1)
                .id_cliente(1)
                .metodo_pago("Tarjeta de crédito")
                .monto_pago(59990.0)
                .estado_pago("PENDIENTE")
                .fecha_transaccion(fecha)
                .build();

        TransaccionController controller = new TransaccionController(transaccionService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testFindAll() throws Exception {
        when(transaccionService.findAll()).thenReturn(List.of(transaccion));

        mockMvc.perform(get("/api/v1/transacciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id_transaccion").value(1))
                .andExpect(jsonPath("$[0].estado_pago").value("PENDIENTE"));
    }

    @Test
    public void testFindById() throws Exception {
        when(transaccionService.findById(1)).thenReturn(transaccion);

        mockMvc.perform(get("/api/v1/transacciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_transaccion").value(1));
    }

    @Test
    public void testSave() throws Exception {
        when(transaccionService.save(any(Transaccion.class))).thenReturn(transaccion);

        mockMvc.perform(post("/api/v1/transacciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaccionDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id_transaccion").value(1));
    }

    @Test
    public void testUpdate() throws Exception {
        when(transaccionService.actualizar(eq(1), any(Transaccion.class))).thenReturn(transaccion);

        mockMvc.perform(put("/api/v1/transacciones/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaccionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_transaccion").value(1));
    }

    @Test
    public void testDelete() throws Exception {
        doNothing().when(transaccionService).delete(1);

        mockMvc.perform(delete("/api/v1/transacciones/1"))
                .andExpect(status().isNoContent());

        verify(transaccionService, times(1)).delete(1);
    }

    @Test
    public void testUpdateEstado() throws Exception {
        Transaccion actualizada = new Transaccion(1, 1, 1, "Tarjeta de crédito", 59990.0, "COMPLETADA", LocalDateTime.now());
        when(transaccionService.updateEstado(eq(1), eq("COMPLETADA"))).thenReturn(actualizada);

        mockMvc.perform(patch("/api/v1/transacciones/1/estado")
                        .param("estado", "COMPLETADA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado_pago").value("COMPLETADA"));
    }
}
