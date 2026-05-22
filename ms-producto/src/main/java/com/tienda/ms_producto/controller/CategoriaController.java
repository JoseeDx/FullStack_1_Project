package com.tienda.ms_producto.controller;

import com.tienda.ms_producto.dto.CategoriaDTO;
import com.tienda.ms_producto.model.Categoria;
import com.tienda.ms_producto.service.CategoriaService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    private static Logger log = LoggerFactory.getLogger(CategoriaController.class);

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> findAll(){
        log.info("Obteniendo todas las categorias");
        List<CategoriaDTO> categorias = categoriaService.findAll()
                .stream()
                .map(CategoriaDTO::fromModel)
                .toList();
        if (categorias.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categorias);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> findById(@PathVariable Integer id) {
        log.info("Obteniendo categoria con ID: {}", id);
        Categoria categoria = categoriaService.findById(id);
        return ResponseEntity.ok(CategoriaDTO.fromModel(categoria));
    }

    @PostMapping
    public ResponseEntity<CategoriaDTO> save(@Valid @RequestBody CategoriaDTO dto) {
        log.info("Creando nueva categoria: {}", dto.getNombre_categoria());
        Categoria saved = categoriaService.save(dto.toModel());
        return new ResponseEntity<>(CategoriaDTO.fromModel(saved), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> update(@PathVariable Integer id, @Valid @RequestBody CategoriaDTO dto) {
        log.info("Actualizando categoria con id: {}", id);
        Categoria existing = categoriaService.findById(id);
        existing.setNombre_categoria(dto.getNombre_categoria());
        return ResponseEntity.ok(CategoriaDTO.fromModel(categoriaService.save(existing)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("Eliminando categoria con id: {}", id);
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<CategoriaDTO> activar(@PathVariable Integer id) {
        log.info("Activando categoria con id: {}", id);
        Categoria existing = categoriaService.findById(id);
        existing.setActivo(true);
        return ResponseEntity.ok(CategoriaDTO.fromModel(categoriaService.save(existing)));
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<CategoriaDTO> desactivar(@PathVariable Integer id) {
        log.info("Desactivando categoria con id: {}", id);
        Categoria existing = categoriaService.findById(id);
        existing.setActivo(false);
        return ResponseEntity.ok(CategoriaDTO.fromModel(categoriaService.save(existing)));
    }

}

