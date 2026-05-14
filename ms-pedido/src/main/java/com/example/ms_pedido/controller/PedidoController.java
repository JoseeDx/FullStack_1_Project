package com.example.ms_pedido.controller;

import com.example.ms_pedido.dto.PedidoDTO;
import com.example.ms_pedido.model.Pedido;
import com.example.ms_pedido.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pedidos") // Esta es la ruta que configuraste en tu API Gateway
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // 1. GET: Obtener todos los pedidos
    @GetMapping
    public ResponseEntity<List<PedidoDTO>> listarPedidos() {
        List<Pedido> pedidos = pedidoService.listarTodos();
        
        // Convertimos la lista de Entidades a lista de DTOs
        List<PedidoDTO> pedidosDTO = pedidos.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
                
        return new ResponseEntity<>(pedidosDTO, HttpStatus.OK);
    }

    // 2. GET: Obtener un pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> obtenerPedido(@PathVariable Long id) {
        Optional<Pedido> pedidoOpt = pedidoService.buscarPorId(id);
        
        if (pedidoOpt.isPresent()) {
            PedidoDTO dto = convertirADto(pedidoOpt.get());
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Devuelve error 404
        }
    }

    // 3. POST: Crear un nuevo pedido
    @PostMapping
    public ResponseEntity<PedidoDTO> crearPedido(@RequestBody PedidoDTO pedidoDTO) {
        // Convertimos el DTO que llega del frontend a Entidad para guardarlo
        Pedido pedido = convertirAEntidad(pedidoDTO);
        
        Pedido pedidoGuardado = pedidoService.guardar(pedido);
        
        // Convertimos la entidad guardada de vuelta a DTO para responder
        PedidoDTO respuestaDTO = convertirADto(pedidoGuardado);
        return new ResponseEntity<>(respuestaDTO, HttpStatus.CREATED); // Devuelve 201 Created
    }

    // 4. DELETE: Eliminar un pedido por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Devuelve 204 No Content
    }

    // --- MÉTODOS AUXILIARES PARA CONVERTIR ENTRE ENTIDAD Y DTO ---
    
    private PedidoDTO convertirADto(Pedido pedido) {
        PedidoDTO dto = new PedidoDTO();
        dto.setIdPedido(pedido.getIdPedido());
        dto.setFechaPedido(pedido.getFechaPedido());
        return dto;
    }

    private Pedido convertirAEntidad(PedidoDTO dto) {
        Pedido pedido = new Pedido();
        pedido.setIdPedido(dto.getIdPedido());
        pedido.setFechaPedido(dto.getFechaPedido());
        return pedido;
    }
}