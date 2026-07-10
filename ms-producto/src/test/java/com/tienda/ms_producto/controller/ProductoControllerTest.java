package com.tienda.ms_producto.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.tienda.ms_producto.dto.ProductoDTO;
import com.tienda.ms_producto.model.Categoria;
import com.tienda.ms_producto.model.Producto;
import com.tienda.ms_producto.service.ProductoService;
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
public class ProductoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductoService productoService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Producto producto;
    private ProductoDTO productoDTO;

    @BeforeEach
    void setUp() {
        Categoria categoria = new Categoria(1, "Teclados", true);
        producto = new Producto(1, "Teclado Mecánico", "Teclado con switches red", 59990.0, categoria, true);

        productoDTO = ProductoDTO.builder()
                .id_producto(null)
                .nombre_producto("Teclado Mecánico")
                .descripcion_producto("Teclado con switches red")
                .precio_producto(59990.0)
                .id_categoria(1)
                .build();

        ProductoController controller = new ProductoController(productoService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testFindAll() throws Exception {
        when(productoService.findAll()).thenReturn(List.of(producto));

        mockMvc.perform(get("/api/v1/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id_producto").value(1))
                .andExpect(jsonPath("$[0].nombre_producto").value("Teclado Mecánico"));
    }

    @Test
    public void testFindById() throws Exception {
        when(productoService.findById(1)).thenReturn(producto);

        mockMvc.perform(get("/api/v1/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_producto").value(1));
    }

    @Test
    public void testSave() throws Exception {
        when(productoService.save(any(Producto.class))).thenReturn(producto);

        mockMvc.perform(post("/api/v1/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id_producto").value(1));
    }

    @Test
    public void testDelete() throws Exception {
        doNothing().when(productoService).delete(1);

        mockMvc.perform(delete("/api/v1/productos/1"))
                .andExpect(status().isNoContent());

        verify(productoService, times(1)).delete(1);
    }

    @Test
    public void testActivar() throws Exception {
        when(productoService.activar(1)).thenReturn(producto);

        mockMvc.perform(patch("/api/v1/productos/1/activar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(true));
    }

    @Test
    public void testDesactivar() throws Exception {
        producto.setActivo(false);
        when(productoService.desactivar(1)).thenReturn(producto);

        mockMvc.perform(patch("/api/v1/productos/1/desactivar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(false));
    }

    @Test
    public void testUpdate() throws Exception {
        when(productoService.actualizar(eq(1), any(Producto.class))).thenReturn(producto);

        mockMvc.perform(put("/api/v1/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_producto").value(1));
    }

    @Test
    public void testFindAll_SinResultados_DeberiaRetornarNoContent() throws Exception {
        when(productoService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/productos"))
                .andExpect(status().isNoContent());
    }
}
