package com.example.ms_resena.controller;

import com.example.ms_resena.dto.ResenaDTO;
import com.example.ms_resena.service.ResenaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resenas")
@RequiredArgsConstructor
@Tag(name = "Reseñas", description = "Endpoints para la gestión de valoraciones y comentarios de productos")
public class ResenaController {

    private final ResenaService resenaService;

    @PostMapping
    @Operation(summary = "Crear una nueva reseña", description = "Permite a un cliente calificar (1 a 5 estrellas) y comentar un producto que ha comprado.")
    public ResponseEntity<ResenaDTO> crearResena(@Valid @RequestBody ResenaDTO resenaDTO) {
        ResenaDTO nuevaResena = resenaService.crearResena(resenaDTO);
        return new ResponseEntity<>(nuevaResena, HttpStatus.CREATED);
    }

    @GetMapping("/producto/{idProducto}")
    @Operation(summary = "Obtener reseñas por producto", description = "Devuelve una lista de todas las valoraciones asociadas a un ID de producto específico.")
    public ResponseEntity<List<ResenaDTO>> obtenerResenasPorProducto(@PathVariable Long idProducto) {
        List<ResenaDTO> resenas = resenaService.obtenerResenasPorProducto(idProducto);
        return ResponseEntity.ok(resenas);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar reseña", description = "Borra una reseña de la base de datos mediante su ID (útil para moderación).")
    public ResponseEntity<Void> eliminarResena(@PathVariable Long id) {
        resenaService.eliminarResena(id);
        return ResponseEntity.noContent().build(); // Retorna un código HTTP 204
    }
}