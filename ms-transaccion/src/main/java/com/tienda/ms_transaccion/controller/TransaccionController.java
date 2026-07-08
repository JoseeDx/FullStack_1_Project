package com.tienda.ms_transaccion.controller;

import com.tienda.ms_transaccion.dto.TransaccionDTO;
import com.tienda.ms_transaccion.model.Transaccion;
import com.tienda.ms_transaccion.service.TransaccionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;

import java.util.List;

@RestController //controlador REST - todas las respuestas se convierten en JSON
@RequestMapping("/api/v1/transacciones") //ruta
public class TransaccionController {

    // Registrar eventos importantes en cada endpoint
    private static Logger log = LoggerFactory.getLogger(TransaccionController.class);

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @GetMapping //sin parametros, se usa el "/api/v1/transacciones", retorna la lista completa
    public ResponseEntity<List<TransaccionDTO>> findAll() {
        log.info("GET /transacciones - Listando todas las transacciones");
        List<TransaccionDTO> transacciones = transaccionService.findAll()
                .stream() //lista a flujo de datos para procesarla 
                .map(TransaccionDTO::fromModel) //convierte cada entidad a DTO
                .toList(); // convierte el resultado a lista
        if (transacciones.isEmpty()) {
            return ResponseEntity.noContent().build();
            //si no hay transacciones, devuelve 204 no content
        }
        return ResponseEntity.ok(transacciones); // 200 ok
    }

    @GetMapping("/{id}") //se le agrega la id
    public ResponseEntity<TransaccionDTO> findById(@PathVariable Integer id) { //pathvariable extrae la id de la url
        log.info("GET /transacciones/{} - Buscando transaccion", id);
        Transaccion transaccion = transaccionService.findById(id); //busca la entidad
        return ResponseEntity.ok(TransaccionDTO.fromModel(transaccion)); //la encuentra y la transforma a dto y retorna 200 ok
    }

    @PostMapping
    public ResponseEntity<TransaccionDTO> save(@Valid @RequestBody TransaccionDTO dto) { //valid activa las validaciones del dto, request body
        log.info("POST /transacciones - Creando transaccion");
        Transaccion saved = transaccionService.save(dto.toModel()); //convierte dto a entidad
        return new ResponseEntity<>(TransaccionDTO.fromModel(saved), HttpStatus.CREATED); //lo guarda y devuelve el resultado como dto con 201 created (recurso nuevo)
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransaccionDTO> update(@PathVariable Integer id, @Valid @RequestBody TransaccionDTO dto) { // recibe la id por el url y los datos nuevos por el body
        log.info("PUT /transacciones/{} - Actualizando transaccion", id);
        return ResponseEntity.ok(TransaccionDTO.fromModel(transaccionService.actualizar(id, dto.toModel())));
        //el service se encarga de buscar, mezclar los campos y guardar
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) { //void pq no retorna nada en el postman!
        log.info("DELETE /transacciones/{} - Eliminando transaccion", id);
        transaccionService.delete(id);
        return ResponseEntity.noContent().build(); //elimina y devuelve 204 no content
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<TransaccionDTO> updateEstado(@PathVariable Integer id, @RequestParam String estado) { //solo cambia el estado (actualizacion parcial), request param recibe el estado como parametro
        log.info("PATCH /transacciones/{}/estado - Cambiando estado a {}", id, estado);
        return ResponseEntity.ok(TransaccionDTO.fromModel(transaccionService.updateEstado(id, estado))); //llama al service que valida el estado y lo actualiza, retorna 200 ok con la transaccion actualizada
    }

    // El controller solo recibe la peticion, llama al service y devuelve la respuesta
    //Toda la logica esta en el service
}
