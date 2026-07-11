package com.example.cliente_service.controller;

import com.example.cliente_service.dto.ClienteDTO;
import com.example.cliente_service.exception.GlobalExceptionHandler;
import com.example.cliente_service.exception.ResourceNotFoundException;
import com.example.cliente_service.model.Cliente;
import com.example.cliente_service.service.ClienteService;
import com.example.cliente_service.assemblers.ClienteModelAssembler; // Importamos tu Assembler
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ClienteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClienteService clienteService;

    @Mock
    private ClienteModelAssembler clienteModelAssembler; // ¡Obligatorio mockearlo para que no sea null!

    @InjectMocks
    private ClienteController clienteController;

    private ObjectMapper objectMapper;
    private Cliente clienteMock;
    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        // 1. Instanciamos la Entidad Cliente (Lo que devuelve el Service)
        clienteMock = new Cliente();
        clienteMock.setId(1L); // Ajusta según tus getters/setters si cambian
        clienteMock.setNombre("Juan Perez");
        clienteMock.setCorreo("juan@test.com");

        // 2. Instanciamos el DTO (Lo que devuelve el Assembler/Controller)
        clienteDTO = new ClienteDTO();
        clienteDTO.setId(1L);
        clienteDTO.setNombre("Juan Perez");
        clienteDTO.setCorreo("juan@test.com");
    }

    @Test
    void obtenerClientePorId_RetornaStatus200() throws Exception {
        // GIVEN: El servicio devuelve la Entidad, y el Assembler la convierte a DTO
        when(clienteService.buscarPorId(1L)).thenReturn(clienteMock);
        
        // Si tu controlador usa assembler.toModel(cliente) descomenta esta línea:
        // when(clienteModelAssembler.toModel(clienteMock)).thenReturn(org.springframework.hateoas.EntityModel.of(clienteDTO));

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/clientes/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void obtenerClientePorId_NoExiste_RetornaStatus404() throws Exception {
        // GIVEN
        when(clienteService.buscarPorId(anyLong()))
                .thenThrow(new ResourceNotFoundException("Cliente no encontrado"));

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/clientes/{id}", 99L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void guardarCliente_RetornaStatus201() throws Exception {
        // GIVEN: Simulamos que el servicio guarda el cliente con éxito
        when(clienteService.guardar(any(Cliente.class))).thenReturn(clienteMock);

        // WHEN & THEN: ¡Aquí usamos el objectMapper para pasar el DTO a JSON string!
        mockMvc.perform(post("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteDTO))) // Soluciona la advertencia amarilla
                .andExpect(status().isCreated());
    }

    @Test
    void eliminarCliente_RetornaStatus204() throws Exception {
        // WHEN & THEN
        mockMvc.perform(delete("/api/v1/clientes/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}