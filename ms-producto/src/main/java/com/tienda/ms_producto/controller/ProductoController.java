package com.tienda.ms_producto.controller;

import com.tienda.ms_producto.dto.ProductoDTO;
import com.tienda.ms_producto.model.Categoria;
import com.tienda.ms_producto.model.Producto;
import com.tienda.ms_producto.service.ProductoService;

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
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private static Logger log = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> findAll(){
        log.info("Obteniendo todos los productos.");
        List<ProductoDTO> productos = productoService.findAll()
                .stream()
                .map(ProductoDTO::fromModel)
                .toList();
        if (productos.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> findById(@PathVariable Integer id) {
        log.info("Obteniendo producto con ID: {}",id);
        Producto producto = productoService.findById(id);
        return ResponseEntity.ok(ProductoDTO.fromModel(producto));
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> save(@Valid @RequestBody ProductoDTO dto) {
        log.info("Creando nuevo producto: {}", dto.getNombre_producto());
        Producto saved = productoService.save(dto.toModel());
        return new ResponseEntity<>(ProductoDTO.fromModel(saved), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> update(@PathVariable Integer id, @Valid @RequestBody ProductoDTO dto) {
        log.info("Actualizando producto con ID: {}", id);
        Producto existing = productoService.findById(id);
        existing.setNombre_producto(dto.getNombre_producto());
        existing.setDescripcion_producto(dto.getDescripcion_producto());
        existing.setPrecio_producto(dto.getPrecio_producto());
        Categoria cat = new Categoria();
        cat.setId_categoria(dto.getId_categoria());
        existing.setCategoria(cat);
        return ResponseEntity.ok(ProductoDTO.fromModel(productoService.save(existing)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("Eliminando producto con ID: {}", id);
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<ProductoDTO> activar(@PathVariable Integer id) {
        log.info("Activando producto con ID: {}", id);
        Producto existing = productoService.findById(id);
        existing.setActivo(true);
        return ResponseEntity.ok(ProductoDTO.fromModel(productoService.save(existing)));
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<ProductoDTO> desactivar(@PathVariable Integer id) {
        log.info("Desactivando producto con ID: {}", id);
        Producto existing = productoService.findById(id);
        existing.setActivo(false);
        return ResponseEntity.ok(ProductoDTO.fromModel(productoService.save(existing)));
    }
}
