package com.tienda.ms_producto.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.tienda.ms_producto.dto.CategoriaDTO;
import com.tienda.ms_producto.model.Categoria;
import com.tienda.ms_producto.service.CategoriaService;
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
public class CategoriaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoriaService categoriaService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Categoria categoria;
    private CategoriaDTO categoriaDTO;

    @BeforeEach
    void setUp() {
        categoria = new Categoria(1, "Teclados", true);

        categoriaDTO = CategoriaDTO.builder()
                .id_categoria(null)
                .nombre_categoria("Teclados")
                .build();

        CategoriaController controller = new CategoriaController(categoriaService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testFindAll() throws Exception {
        when(categoriaService.findAll()).thenReturn(List.of(categoria));

        mockMvc.perform(get("/api/v1/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id_categoria").value(1))
                .andExpect(jsonPath("$[0].nombre_categoria").value("Teclados"));
    }

    @Test
    public void testFindById() throws Exception {
        when(categoriaService.findById(1)).thenReturn(categoria);

        mockMvc.perform(get("/api/v1/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_categoria").value(1));
    }

    @Test
    public void testSave() throws Exception {
        when(categoriaService.save(any(Categoria.class))).thenReturn(categoria);

        mockMvc.perform(post("/api/v1/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoriaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id_categoria").value(1));
    }

    @Test
    public void testDelete() throws Exception {
        doNothing().when(categoriaService).delete(1);

        mockMvc.perform(delete("/api/v1/categorias/1"))
                .andExpect(status().isNoContent());

        verify(categoriaService, times(1)).delete(1);
    }

    @Test
    public void testDesactivar() throws Exception {
        Categoria desactivada = new Categoria(1, "Teclados", false);
        when(categoriaService.desactivar(1)).thenReturn(desactivada);

        mockMvc.perform(patch("/api/v1/categorias/1/desactivar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(false));
    }

    @Test
    public void testActivar() throws Exception {
        when(categoriaService.activar(1)).thenReturn(categoria);

        mockMvc.perform(patch("/api/v1/categorias/1/activar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(true));
    }

    @Test
    public void testUpdate() throws Exception {
        when(categoriaService.actualizar(eq(1), any(Categoria.class))).thenReturn(categoria);

        mockMvc.perform(put("/api/v1/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoriaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_categoria").value(1));
    }

    @Test
    public void testFindAll_SinResultados_DeberiaRetornarNoContent() throws Exception {
        when(categoriaService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/categorias"))
                .andExpect(status().isNoContent());
    }
}
