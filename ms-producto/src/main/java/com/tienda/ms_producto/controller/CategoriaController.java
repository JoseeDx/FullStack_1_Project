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

@RestController//Clase rest rescibe todas las respuesta en json
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    private static Logger log = LoggerFactory.getLogger(CategoriaController.class);

    @Autowired//Inyecta service
    private CategoriaService categoriaService;

    @GetMapping//Lista todas las categorias
    public ResponseEntity<List<CategoriaDTO>> findAll(){
        log.info("GET /categorias - Listando todas las categorias");
        List<CategoriaDTO> categorias = categoriaService.findAll()//Le pide a service que lo haga
                .stream()//flujo
                .map(CategoriaDTO::fromModel)//entidades a dto
                .toList();//resultado a lista
        if (categorias.isEmpty()){//si esta vacio
            return ResponseEntity.noContent().build();
            //retorna 204
        }
        return ResponseEntity.ok(categorias); //si hay productos retorna la lista 200
    }

    //Busca categoria por id 
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaDTO> findById(@PathVariable Integer id) { //saca la id de la url
        log.info("GET /categorias/{} - Buscando categoria", id);
        Categoria categoria = categoriaService.findById(id); //service busca 
        return ResponseEntity.ok(CategoriaDTO.fromModel(categoria));//ResponseEntity.ok metodo para contruir una respuesta http exitosa
        //Convierte en dto y retorna 200
    }

    //Crea una nueva categoria
    @PostMapping
    public ResponseEntity<CategoriaDTO> save(@Valid @RequestBody CategoriaDTO dto) {//json recibido a dto y activa las validaciones dto
        log.info("POST /categorias - Creando categoria: {}", dto.getNombre_categoria());
        Categoria saved = categoriaService.save(dto.toModel());//dto a entidad para guardar en base de datos
        return new ResponseEntity<>(CategoriaDTO.fromModel(saved), HttpStatus.CREATED);
        //Convierte la entidad a dto y retorna 201                 codigo http 201
    }

    //Actualiza una categoria 
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaDTO> update(@PathVariable Integer id, @Valid @RequestBody CategoriaDTO dto) {
        log.info("PUT /categorias/{} - Actualizando categoria", id);
        Categoria existing = categoriaService.findById(id); //Verifica si existe por la id
        existing.setNombre_categoria(dto.getNombre_categoria());//Actualiza el campo
        return ResponseEntity.ok(CategoriaDTO.fromModel(categoriaService.save(existing)));
        //Guarda los cambios convierte a dto y retorna 200
    }

    //Borrar categoria por su id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) { //void pq no retorna nada
        log.info("DELETE /categorias/{} - Eliminando categoria", id);
        categoriaService.delete(id);//Le pide a service que la borre
        return ResponseEntity.noContent().build();
        //Retorna 204, eliminado sin contenido
    }

    //Activar categoria 
    @PatchMapping("/{id}/activar")
    public ResponseEntity<CategoriaDTO> activar(@PathVariable Integer id) {
        log.info("PATCH /categorias/{}/activar - Activando categoria", id);
        Categoria existing = categoriaService.findById(id);//Verifica si la categoria existe
        existing.setActivo(true);//Cambia el campo activo a true
        return ResponseEntity.ok(CategoriaDTO.fromModel(categoriaService.save(existing)));
        //Guarda el cambio, cambia a dto y retorna 200
    }

    //Desactivar categoria 
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<CategoriaDTO> desactivar(@PathVariable Integer id) {
        log.info("PATCH /categorias/{}/desactivar - Desactivando categoria", id);
        return ResponseEntity.ok(CategoriaDTO.fromModel(categoriaService.desactivar(id)));//service maneja toda la logica 
        //categoria desactivada y retorna 200

    }
}   

