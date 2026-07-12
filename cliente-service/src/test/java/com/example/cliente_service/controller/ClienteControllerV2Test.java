package com.example.cliente_service.controller;

import com.example.cliente_service.assemblers.ClienteModelAssembler;
import com.example.cliente_service.dto.ClienteDTO;
import com.example.cliente_service.model.Cliente;
import com.example.cliente_service.model.Roles;
import com.example.cliente_service.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ClienteControllerV2Test {

    private MockMvc mockMvc;

    // Dependencias a simular (Mock)
    private ClienteService clienteService;

    private ClienteModelAssembler assembler;

    private ObjectMapper objectMapper = new ObjectMapper();

    // Variables de prueba
    private Cliente clienteEntidad;
    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        // Preparamos los datos simulados (Modelo y DTO) antes de cada test
        Roles rol = new Roles();
        rol.setIdRol(1L);

        // Recordar que el servicio nos devuelve esta entidad pura
        clienteEntidad = new Cliente(1L, "Juan Perez", "juan.perez@example.com", rol);
        
        // Y el controlador internamente la convertirá a este DTO
        clienteDTO = ClienteDTO.fromModel(clienteEntidad);

        // Creamos mocks manualmente (evitamos depender de las anotaciones de Spring Boot en el classpath de tests)
        clienteService = mock(ClienteService.class);
        assembler = mock(ClienteModelAssembler.class);

        // Instanciamos el controller y le inyectamos los mocks mediante Reflection (como si fueran @Autowired)
        ClienteControllerV2 controller = new ClienteControllerV2();
        ReflectionTestUtils.setField(controller, "clienteService", clienteService);
        ReflectionTestUtils.setField(controller, "assembler", assembler);

        // Construimos MockMvc en modo standalone para probar solo el controlador
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Test V2: Listar todos los clientes (HATEOAS)")
    void cuandoListarTodos_entoncesRetorna200() throws Exception {
        // GIVEN (Dado)
        List<Cliente> listaClientes = Arrays.asList(clienteEntidad);
        // Simulamos el comportamiento del Service
        given(clienteService.listar()).willReturn(listaClientes);
        // Simulamos el comportamiento del Assembler para que no intente generar los links reales
        given(assembler.toModel(any(ClienteDTO.class))).willReturn(EntityModel.of(clienteDTO));

        // WHEN & THEN (Cuando & Entonces)
        mockMvc.perform(get("/api/v2/clientes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
                // Nota: Cuando se usa CollectionModel con Spring HATEOAS, la estructura del JSON devuelto 
                // depende de si está activo HAL (_embedded) o no (content). Solo comprobar el status 
                // ya garantiza que el flujo Controller -> DTO -> Assembler no arrojó excepciones.
    }

    @Test
    @DisplayName("Test V2: Obtener cliente por ID (HATEOAS)")
    void dadoIdExistente_cuandoObtenerClientePorId_entoncesRetorna200YClienteHateoas() throws Exception {
        // GIVEN (Dado)
        // 1. Simulamos que el service encuentra el cliente en base de datos
        given(clienteService.buscarPorId(anyLong())).willReturn(clienteEntidad);
        // 2. Simulamos que el assembler lo envuelve correctamente en el HATEOAS
        given(assembler.toModel(any(ClienteDTO.class))).willReturn(EntityModel.of(clienteDTO));

        // WHEN & THEN (Cuando & Entonces)
        mockMvc.perform(get("/api/v2/clientes/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                // Verificamos que los datos se convirtieron bien a JSON y llegaron al frontend
                .andExpect(jsonPath("$.id").value(clienteDTO.getId()))
                .andExpect(jsonPath("$.nombre").value(clienteDTO.getNombre()))
                .andExpect(jsonPath("$.correo").value(clienteDTO.getCorreo()));
    }
}