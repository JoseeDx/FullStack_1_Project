package com.tienda.ms_carrito.controller;

import com.tienda.ms_carrito.dto.CarritoItemDTO;
import com.tienda.ms_carrito.model.CarritoItem;
import com.tienda.ms_carrito.service.CarritoItemService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController //esta clase es un controlador rest, todas las respuestas se convierten en json
@RequestMapping("/api/v1/carrito")
public class CarritoItemController {

    //registrar eventos
    private static Logger log = LoggerFactory.getLogger(CarritoItemController.class);

    private final CarritoItemService carritoItemService;

    public CarritoItemController(CarritoItemService carritoItemService) {
        this.carritoItemService = carritoItemService;
    }

    @GetMapping //trae todos los items del carrito
    public ResponseEntity<List<CarritoItemDTO>> findAll() {
        log.info("GET /carrito - Listando todos los items del carrito"); //se llama al endpoint
        List<CarritoItemDTO> items = carritoItemService.findAll() //le pide al service todos los items
                .stream() //lista en flujo
                .map(CarritoItemDTO::fromModel) //entidad de carrito a dto
                .toList(); //convierte el resultado a lista
        if (items.isEmpty()) {
            return ResponseEntity.noContent().build(); //si no hay items retorna 204 
        }
        return ResponseEntity.ok(items); //si hay items retorna la lista 200
    }

    @GetMapping("/{id}") //busca por una id en especifico
    public ResponseEntity<CarritoItemDTO> findById(@PathVariable Integer id) { //extrae id de la url 
        log.info("GET /carrito/{} - Buscando item del carrito", id);
        CarritoItem item = carritoItemService.findById(id); //busca el item en la base de datos
        return ResponseEntity.ok(CarritoItemDTO.fromModel(item));//la convierte a dto y la devuelve
    }

    @GetMapping("/cliente/{id_cliente}") //busca los items de un cliente
    public ResponseEntity<List<CarritoItemDTO>> findByIdCliente(@PathVariable Integer id_cliente) {
        log.info("GET /carrito/cliente/{} - Buscando items del cliente", id_cliente);
        List<CarritoItemDTO> items = carritoItemService.findByIdCliente(id_cliente)
                .stream()//flujo
                .map(CarritoItemDTO::fromModel)//convierte a dto
                .toList();//pasa a lista
        if (items.isEmpty()) {
            return ResponseEntity.noContent().build();//si no tiene items 204
        }
        return ResponseEntity.ok(items);//si tiene items 200
    }

    @GetMapping("/total/{id_cliente}")//calcula el total a pagar
    public ResponseEntity<Double> getTotalByIdCliente(@PathVariable Integer id_cliente) {
        log.info("GET /carrito/total/{} - Calculando total del carrito", id_cliente);
        return ResponseEntity.ok(carritoItemService.getTotalByIdCliente(id_cliente));//llama al metodo en service y retorna el total
    }

    @PostMapping
    public ResponseEntity<CarritoItemDTO> save(@Valid @RequestBody CarritoItemDTO dto) {//validaciones dto
        log.info("POST /carrito - Agregando item al carrito");
        CarritoItem saved = carritoItemService.save(dto.toModel());//convierte el dto a entidad y lo guarda
        return new ResponseEntity<>(CarritoItemDTO.fromModel(saved), HttpStatus.CREATED);//cuerpo y codigo http
        //la entidad guardada de vuelta a dto y devuelve 201
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarritoItemDTO> update(@PathVariable Integer id, @Valid @RequestBody CarritoItemDTO dto) {
        //recibe la id por la url y los datos nuevos por el body
        log.info("PUT /carrito/{} - Actualizando item del carrito", id);
        return ResponseEntity.ok(CarritoItemDTO.fromModel(carritoItemService.actualizar(id, dto.toModel())));
        //el service se encarga de buscar, mezclar los campos y guardar
    }

    @DeleteMapping("/{id}") //borra xd
    public ResponseEntity<Void> delete(@PathVariable Integer id) {//es un void porque no retorna nada
        log.info("DELETE /carrito/{} - Eliminando item del carrito", id);
        carritoItemService.delete(id);//llama al service
        return ResponseEntity.noContent().build();//204 eliminado sin contenido
    }

}
