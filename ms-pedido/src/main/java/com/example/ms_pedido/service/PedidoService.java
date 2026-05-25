package com.example.ms_pedido.service;

import com.example.ms_pedido.model.Pedido;
import com.example.ms_pedido.repository.PedidoRepository;
import com.example.ms_pedido.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);
    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> listarTodos() {
        log.info("Consultando todos los pedidos");
        try {
            return pedidoRepository.findAll();
        } catch (Exception e) {
            log.error("Error al consultar pedidos: {}", e.getMessage());
            throw new RuntimeException("Error al obtener los pedidos");
        }
    }

    public Pedido buscarPorId(Long id) {
        log.info("Obteniendo pedido por ID: {}", id);
        return pedidoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Pedido con ID: {} no encontrado", id);
                    return new ResourceNotFoundException("Pedido no encontrado con ID: " + id);
                });
    }

    public Pedido guardar(Pedido pedido) {
        log.info("Guardando nuevo pedido");
        try {
            // Se mantiene tu lógica original por si el DTO no frena el null en algún caso interno
            if (pedido.getFechaPedido() == null) {
                pedido.setFechaPedido(LocalDateTime.now());
            }
            return pedidoRepository.save(pedido);
        } catch (Exception e) {
            log.error("Error al guardar pedido: {}", e.getMessage());
            throw new RuntimeException("Error al guardar el pedido");
        }
    }

    public Pedido actualizar(Long id, Pedido datosActualizados) {
        log.info("Actualizando pedido con ID: {}", id);
        try {
            Pedido existente = buscarPorId(id);
            existente.setFechaPedido(datosActualizados.getFechaPedido());
            return pedidoRepository.save(existente);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al actualizar pedido ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al actualizar el pedido");
        }
    }

    public void eliminar(Long id) {
        log.info("Borrando pedido con ID: {}", id);
        try {
            if (!pedidoRepository.existsById(id)) {
                throw new ResourceNotFoundException("Pedido no encontrado con ID: " + id);
            }
            pedidoRepository.deleteById(id);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al eliminar pedido con ID: {}", id);
            throw new RuntimeException("Error al eliminar el pedido");
        }
    }
}