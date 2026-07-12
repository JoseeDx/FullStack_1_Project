package com.example.ms_pedido.controller;

import com.example.ms_pedido.assemblers.PedidoModelAssembler;
import com.example.ms_pedido.dto.PedidoDTO;
import com.example.ms_pedido.model.Pedido;
import com.example.ms_pedido.service.PedidoService;
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

public class PedidoControllerV2Test {

    private MockMvc mockMvc;

    private PedidoService pedidoService;
    private PedidoModelAssembler assembler;

    private Pedido pedidoEntidad;
    private PedidoDTO pedidoDTO;

    @BeforeEach
    void setUp() {
        pedidoService = mock(PedidoService.class);
        assembler = mock(PedidoModelAssembler.class);

        // Datos de prueba
        pedidoEntidad = new Pedido(1L, LocalDateTime.now());
        pedidoDTO = PedidoDTO.fromModel(pedidoEntidad);

        // Instanciamos controller e inyectamos mocks
        PedidoControllerV2 controller = new PedidoControllerV2();
        ReflectionTestUtils.setField(controller, "service", pedidoService);
        ReflectionTestUtils.setField(controller, "assembler", assembler);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("V2: Listar todos los pedidos devuelve 200")
    void cuandoListarTodos_entoncesRetorna200() throws Exception {
        List<Pedido> lista = Arrays.asList(pedidoEntidad);
        given(pedidoService.listarTodos()).willReturn(lista);
        given(assembler.toModel(any(PedidoDTO.class))).willReturn(EntityModel.of(pedidoDTO));

        mockMvc.perform(get("/api/v2/pedidos").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("V2: Obtener pedido por ID devuelve 200 y payload")
    void dadoIdExistente_cuandoObtenerPorId_entoncesRetorna200() throws Exception {
        given(pedidoService.buscarPorId(anyLong())).willReturn(pedidoEntidad);
        given(assembler.toModel(any(PedidoDTO.class))).willReturn(EntityModel.of(pedidoDTO));

        mockMvc.perform(get("/api/v2/pedidos/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPedido").value(pedidoDTO.getIdPedido()));
    }
}
