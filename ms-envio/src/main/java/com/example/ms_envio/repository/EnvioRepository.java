package com.example.ms_envio.repository;

import com.example.ms_envio.model.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Integer> {
    // Regla: Encontrar el envío correspondiente a un pedido específico
    List<Envio> findByIdPedido(Integer idPedido);
}