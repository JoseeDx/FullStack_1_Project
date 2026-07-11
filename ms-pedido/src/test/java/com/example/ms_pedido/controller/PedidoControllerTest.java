package com.example.ms_pedido.controller;

import com.example.ms_pedido.dto.PedidoDTO;
import com.example.ms_pedido.exception.GlobalExceptionHandler;
import com.example.ms_pedido.exception.ResourceNotFoundException;
import com.example.ms_pedido.model.Pedido;
import com.example.ms_pedido.service.PedidoService;
import com.example.ms_pedido.assemblers.PedidoModelAssembler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // Para manejar LocalDateTime
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PedidoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PedidoService pedidoService;

    @Mock
    private PedidoModelAssembler pedidoModelAssembler; // Mockeamos el Assembler

    @InjectMocks
    private PedidoController pedidoController;

    private ObjectMapper objectMapper;
    private Pedido pedidoMock;
    private PedidoDTO pedidoDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(pedidoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
                
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Obligatorio para serializar LocalDateTime

        pedidoMock = new Pedido();
        pedidoMock.setFechaPedido(LocalDateTime.now());

        pedidoDTO = new PedidoDTO();
        // pedidoDTO.setIdPedido(1L);
        pedidoDTO.setFechaPedido(LocalDateTime.now());
    }

    @Test
    void obtenerPedidoPorId_RetornaStatus200() throws Exception {
        // GIVEN
        when(pedidoService.buscarPorId(1L)).thenReturn(pedidoMock);

        // WHEN & THEN (Asegúrate de que la ruta /api/v1/pedidos sea la correcta)
        mockMvc.perform(get("/api/v1/pedidos/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerPedidoPorId_NoExiste_RetornaStatus404() throws Exception {
        // GIVEN
        when(pedidoService.buscarPorId(anyLong()))
                .thenThrow(new ResourceNotFoundException("Pedido no encontrado"));

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/pedidos/{id}", 99L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearPedido_RetornaStatus201() throws Exception {
        // GIVEN
        when(pedidoService.guardar(any(Pedido.class))).thenReturn(pedidoMock);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pedidoDTO)))
                .andExpect(status().isCreated()); // Asumiendo que tu Controller devuelve HTTP 201 Created
    }

    @Test
    void eliminarPedido_RetornaStatus204() throws Exception {
        // WHEN & THEN
        mockMvc.perform(delete("/api/v1/pedidos/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}