package com.tienda.ms_carrito.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.tienda.ms_carrito.dto.CarritoItemDTO;
import com.tienda.ms_carrito.model.CarritoItem;
import com.tienda.ms_carrito.service.CarritoItemService;
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
public class CarritoItemControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CarritoItemService carritoItemService;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private CarritoItem carritoItem;
    private CarritoItemDTO carritoItemDTO;

    @BeforeEach
    void setUp() {
        LocalDateTime fecha = LocalDateTime.now();

        carritoItem = new CarritoItem(1, 1, 1, 29990.0, fecha, 2);

        carritoItemDTO = CarritoItemDTO.builder()
                .id_carrito(null)
                .id_cliente(1)
                .id_producto(1)
                .cantidad(2)
                .precio_unitario(29990.0)
                .fecha_agregado(fecha)
                .build();

        CarritoItemController controller = new CarritoItemController(carritoItemService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testFindAll() throws Exception {
        when(carritoItemService.findAll()).thenReturn(List.of(carritoItem));

        mockMvc.perform(get("/api/v1/carrito"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id_carrito").value(1))
                .andExpect(jsonPath("$[0].id_cliente").value(1))
                .andExpect(jsonPath("$[0].cantidad").value(2));
    }

    @Test
    public void testFindById() throws Exception {
        when(carritoItemService.findById(1)).thenReturn(carritoItem);

        mockMvc.perform(get("/api/v1/carrito/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_carrito").value(1))
                .andExpect(jsonPath("$.id_producto").value(1));
    }

    @Test
    public void testFindByIdCliente() throws Exception {
        when(carritoItemService.findByIdCliente(1)).thenReturn(List.of(carritoItem));

        mockMvc.perform(get("/api/v1/carrito/cliente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id_cliente").value(1));
    }

    @Test
    public void testGetTotalByIdCliente() throws Exception {
        when(carritoItemService.getTotalByIdCliente(1)).thenReturn(59980.0);

        mockMvc.perform(get("/api/v1/carrito/total/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("59980.0"));
    }

    @Test
    public void testSave() throws Exception {
        when(carritoItemService.save(any(CarritoItem.class))).thenReturn(carritoItem);

        mockMvc.perform(post("/api/v1/carrito")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carritoItemDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id_carrito").value(1));
    }

    @Test
    public void testUpdate() throws Exception {
        when(carritoItemService.actualizar(eq(1), any(CarritoItem.class))).thenReturn(carritoItem);

        mockMvc.perform(put("/api/v1/carrito/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(carritoItemDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_carrito").value(1));
    }

    @Test
    public void testDelete() throws Exception {
        doNothing().when(carritoItemService).delete(1);

        mockMvc.perform(delete("/api/v1/carrito/1"))
                .andExpect(status().isNoContent());

        verify(carritoItemService, times(1)).delete(1);
    }

    @Test
    public void testFindAll_SinResultados_DeberiaRetornarNoContent() throws Exception {
        when(carritoItemService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/carrito"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testFindByIdCliente_SinResultados_DeberiaRetornarNoContent() throws Exception {
        when(carritoItemService.findByIdCliente(99)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/carrito/cliente/99"))
                .andExpect(status().isNoContent());
    }
}
