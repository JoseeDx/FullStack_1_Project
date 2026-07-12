package com.tienda.ms_producto.controller;

import com.tienda.ms_producto.dto.ProductoDTO;
import com.tienda.ms_producto.model.Producto;
import com.tienda.ms_producto.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Productos", description = "Gestión del catálogo de productos")
public class ProductoController {

    //Para registrar eventos
    private static Logger log = LoggerFactory.getLogger(ProductoController.class);

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping//Trae todos los productos de la base de datos
    @Operation(summary = "Listar todos los productos", description = "Devuelve el catálogo completo de productos.")
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
    @Operation(summary = "Obtener un producto por ID", description = "Devuelve el detalle de un producto específico.")
    public ResponseEntity<ProductoDTO> findById(@PathVariable Integer id) {//Saca el id de la url
        log.info("Obteniendo producto con ID: {}",id);
        Producto producto = productoService.findById(id);//le pide al service que busque
        return ResponseEntity.ok(ProductoDTO.fromModel(producto));//ResponseEntity.ok metodo estatico para construir una respuesta http exitosa
        //Convierte en dto y devuelve 200
    }

    //Crea un nuevo producto
    @PostMapping
    @Operation(summary = "Crear un producto", description = "Registra un nuevo producto en el catálogo.")
    public ResponseEntity<ProductoDTO> save(@Valid @RequestBody ProductoDTO dto) {//json recibido a dto y activa las validaciones dto
        log.info("Creando nuevo producto: {}", dto.getNombre_producto());
        Producto saved = productoService.save(dto.toModel());//dto a entidad para guardarlo
        return new ResponseEntity<>(ProductoDTO.fromModel(saved), HttpStatus.CREATED);
        //convierte la entidad guardada a dto y retorna 201
    }

    //Actualiza los campos de un producto
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un producto", description = "Modifica los datos de un producto existente.")
    public ResponseEntity<ProductoDTO> update(@PathVariable Integer id, @Valid @RequestBody ProductoDTO dto) {
        log.info("Actualizando producto con ID: {}", id);
        return ResponseEntity.ok(ProductoDTO.fromModel(productoService.actualizar(id, dto.toModel())));
        //el service se encarga de buscar, mezclar los campos y guardar
    }

    //Elimina producto por su id
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto", description = "Elimina un producto por su ID.")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {//void pq no retorna nada
        log.info("Eliminando producto con ID: {}", id);
        productoService.delete(id);//le pide al service que lo borre
        return ResponseEntity.noContent().build();//retorna 204, eliminado sin contenido
    }

    //Activar producto por su id
    @PatchMapping("/{id}/activar")
    @Operation(summary = "Activar un producto", description = "Marca un producto como activo/disponible en el catálogo.")
    public ResponseEntity<ProductoDTO> activar(@PathVariable Integer id) {
        log.info("Activando producto con ID: {}", id);
        return ResponseEntity.ok(ProductoDTO.fromModel(productoService.activar(id)));
    }

    //Desactivar producto por su id
    @PatchMapping("/{id}/desactivar")
    @Operation(summary = "Desactivar un producto", description = "Marca un producto como inactivo/no disponible en el catálogo.")
    public ResponseEntity<ProductoDTO> desactivar(@PathVariable Integer id) {
        log.info("Desactivando producto con ID: {}", id);
        return ResponseEntity.ok(ProductoDTO.fromModel(productoService.desactivar(id)));
    }
}
