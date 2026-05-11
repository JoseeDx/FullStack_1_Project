package com.tienda.ms_producto.controller;

import com.tienda.ms_producto.model.Categoria;
import com.tienda.ms_producto.service.CategoriaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<Categoria>> findAll(){
        List<Categoria> categorias = categoriaService.findAll();
        if (categorias.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(categorias);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> findById(@PathVariable Integer id) {
        Categoria categoria = categoriaService.findById(id);
        return ResponseEntity.ok(categoria);
    }

    @PostMapping
    public ResponseEntity<Categoria> save(@RequestBody Categoria categoria) {
        return new ResponseEntity<>(categoriaService.save(categoria), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> update(@PathVariable Integer id, @RequestBody Categoria categoria) {
        Categoria existing = categoriaService.findById(id);
        existing.setNombre_categoria(categoria.getNombre_categoria());
        return ResponseEntity.ok(categoriaService.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/activar")
    public ResponseEntity<Categoria> activar(@PathVariable Integer id) {
        Categoria existing = categoriaService.findById(id);
        existing.setActivo(true);
        return ResponseEntity.ok(categoriaService.save(existing));
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Categoria> desactivar(@PathVariable Integer id) {
        Categoria existing = categoriaService.findById(id);
        existing.setActivo(false);
        return ResponseEntity.ok(categoriaService.save(existing));
    }

}

