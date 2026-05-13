package com.tienda.ms_producto.controller;

import com.tienda.ms_producto.model.Producto;
import com.tienda.ms_producto.service.ProductoService;

import jakarta.validation.Valid;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private static Logger log = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<Producto>> findAll(){
        log.info("Obteniendo todos los productos.");
        List<Producto> productos = productoService.findAll();
        if (productos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> findById(@PathVariable Integer id) {
        log.info("Obteniendo producto con ID: {}",id)
        Producto producto = productoService.findById(id);
        return ResponseEntity.ok(producto);
    }

    @PostMapping
    public ResponseEntity<Producto> save(@Valid @RequestBody Producto producto) {
        log.info("Creando nuevo producto: {}", producto.getNombre_producto());
        return new ResponseEntity<>(productoService.save(producto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> update(@PathVariable Integer id, @Valid @RequestBody Producto producto) {
        log.info("Actualizando producto con ID: {}", id);
        Producto existing = productoService.findById(id);
        existing.setNombre_producto(producto.getNombre_producto());
        return ResponseEntity.ok(productoService.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("Eliminando producto con ID: {}", id)
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/activar")
    public ResponseEntity<Producto> activar(@PathVariable Integer id) {
        log.info("Activando producto con ID: {}", id)
        Producto existing = productoService.findById(id);
        existing.setActivo(true);
        return ResponseEntity.ok(productoService.save(existing));
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Producto> desactivar(@PathVariable Integer id) {
        lig.info("Desactivando producto con ID: {}", id)
        Producto existing = productoService.findById(id);
        existing.setActivo(false);
        return ResponseEntity.ok(productoService.save(existing));
    }
}
