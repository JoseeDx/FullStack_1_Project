package com.example.ms_pedido.service;

import com.example.ms_pedido.model.Pedido;
import com.example.ms_pedido.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    // 1. Obtener todos los pedidos
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    // 2. Obtener un pedido por su ID
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    // 3. Guardar o actualizar un pedido
    public Pedido guardar(Pedido pedido) {
        // Lógica de negocio: Si el pedido viene sin fecha, le asignamos la fecha y hora actual exacta.
        if (pedido.getFechaPedido() == null) {
            pedido.setFechaPedido(LocalDateTime.now());
        }
        
        return pedidoRepository.save(pedido);
    }

    // 4. Eliminar un pedido
    public void eliminar(Long id) {
        // Aquí podrías agregar lógica, por ejemplo: verificar si el pedido existe antes de borrarlo
        pedidoRepository.deleteById(id);
    }
}