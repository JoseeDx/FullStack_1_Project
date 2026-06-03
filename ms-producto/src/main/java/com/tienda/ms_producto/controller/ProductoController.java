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

@RestController//controlador rest todas las respuestas se convierten a json
@RequestMapping("/api/v1/productos")
public class ProductoController {

    //Para registrar eventos
    private static Logger log = LoggerFactory.getLogger(ProductoController.class);

    @Autowired//Inyecta productoservice
    private ProductoService productoService;

    @GetMapping//Trae todos los productos de la base de datos
    public ResponseEntity<List<ProductoDTO>> findAll(){
        log.info("Obteniendo todos los productos.");
        List<ProductoDTO> productos = productoService.findAll() //Le pide a service que lo haga
                .stream()//convierte la lista en flujo
                .map(ProductoDTO::fromModel)//cada entidad a dto
                .toList();//resultado a lista
        if (productos.isEmpty()){//si esta vacio
            return ResponseEntity.noContent().build();//retorna 204
        }
        return ResponseEntity.ok(productos);//Si hay productos retorna la lista 200
    }

    //Busca un producto por su id
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> findById(@PathVariable Integer id) {//Saca el id de la url
        log.info("Obteniendo producto con ID: {}",id);
        Producto producto = productoService.findById(id);//le pide al service que busque
        return ResponseEntity.ok(ProductoDTO.fromModel(producto));//ResponseEntity.ok metodo estatico para construir una respuesta http exitosa
        //Convierte en dto y devuelve 200
    }

    //Crea un nuevo producto
    @PostMapping
    public ResponseEntity<ProductoDTO> save(@Valid @RequestBody ProductoDTO dto) {//json recibido a dto y activa las validaciones dto
        log.info("Creando nuevo producto: {}", dto.getNombre_producto());
        Producto saved = productoService.save(dto.toModel());//dto a entidad para guardarlo
        return new ResponseEntity<>(ProductoDTO.fromModel(saved), HttpStatus.CREATED);
        //convierte la entidad guardada a dto y retorna 201
    }

    //Actualiza los campos de un producto 
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> update(@PathVariable Integer id, @Valid @RequestBody ProductoDTO dto) {
        log.info("Actualizando producto con ID: {}", id);
        Producto existing = productoService.findById(id);//verifica si existe por la id
        //Actualiza los campos con los datos nuevos 
        existing.setNombre_producto(dto.getNombre_producto());
        existing.setDescripcion_producto(dto.getDescripcion_producto());
        existing.setPrecio_producto(dto.getPrecio_producto());
        //crea un objeto categoria con solo el id, solo necesita eso
        Categoria cat = new Categoria();
        cat.setId_categoria(dto.getId_categoria());
        existing.setCategoria(cat);
        return ResponseEntity.ok(ProductoDTO.fromModel(productoService.save(existing)));
        //guarda los cambios y retorna el producto actualizado como dto 200
    }

    //Elimina producto por su id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {//void pq no retorna nada
        log.info("Eliminando producto con ID: {}", id);
        productoService.delete(id);//le pide al service que lo borre 
        return ResponseEntity.noContent().build();//retorna 204, eliminado sin contenido
    }

    //Activar producto por su id
    @PatchMapping("/{id}/activar")
    public ResponseEntity<ProductoDTO> activar(@PathVariable Integer id) {
        log.info("Activando producto con ID: {}", id);
        Producto existing = productoService.findById(id);//verifica si el producto existe
        existing.setActivo(true);//cambia solo el campo activo a true
        return ResponseEntity.ok(ProductoDTO.fromModel(productoService.save(existing)));
        //guarda el cambio y retorna el producto actualizado 200
    }

    //Desactivar producto por su id
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<ProductoDTO> desactivar(@PathVariable Integer id) {
        log.info("Desactivando producto con ID: {}", id);
        Producto existing = productoService.findById(id);//verifica si existe
        existing.setActivo(false);//cambia solo el campo activo a false
        return ResponseEntity.ok(ProductoDTO.fromModel(productoService.save(existing)));
        //guarda el cambio y retorna el producto actualizado 200
    }
}
