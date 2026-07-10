package com.tienda.ms_carrito.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.tienda.ms_carrito.assemblers.CarritoItemModelAssembler;
import com.tienda.ms_carrito.model.CarritoItem;
import com.tienda.ms_carrito.service.CarritoItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(CarritoItemControllerV2.class)
@Import(CarritoItemModelAssembler.class)
public class CarritoItemControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarritoItemService carritoItemService;

    private CarritoItem carritoItem;

    @BeforeEach
    void setUp() {
        carritoItem = new CarritoItem(1, 1, 1, 29990.0, LocalDateTime.now(), 2);
    }

    @Test
    public void testListarCarrito() throws Exception {
        when(carritoItemService.findAll()).thenReturn(List.of(carritoItem));

        mockMvc.perform(get("/carrito/v2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.carritoItemList[0].id_carrito").value(1))
                .andExpect(jsonPath("$._embedded.carritoItemList[0].id_cliente").value(1))
                .andExpect(jsonPath("$._links.self").exists());

        verify(carritoItemService, times(1)).findAll();
    }

    @Test
    public void testObtenerCarritoItem() throws Exception {
        when(carritoItemService.findById(1)).thenReturn(carritoItem);

        mockMvc.perform(get("/carrito/v2/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_carrito").value(1))
                .andExpect(jsonPath("$.id_producto").value(1))
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.carrito").exists());

        verify(carritoItemService, times(1)).findById(1);
    }
}
