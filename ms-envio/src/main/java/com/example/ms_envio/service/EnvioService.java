package com.example.ms_envio.service;

import com.example.ms_envio.dto.EnvioDTO;
import com.example.ms_envio.model.Envio;
import com.example.ms_envio.repository.EnvioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EnvioService {

    @Autowired
    private EnvioRepository repository;

    public EnvioDTO crear(EnvioDTO dto) {
        log.info("Creando nuevo envío para el pedido ID: {}", dto.getIdPedido());
        Envio entidad = new Envio(null, dto.getIdPedido(), dto.getDireccion(), dto.getCiudad(), "PREPARANDO", null);
        Envio guardado = repository.save(entidad);
        dto.setIdEnvio(guardado.getIdEnvio());
        dto.setEstado(guardado.getEstado());
        return dto;
    }

    public EnvioDTO obtenerPorId(Integer id) {
        log.info("Buscando envío con ID: {}", id);
        Envio entidad = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envío no encontrado"));
        return new EnvioDTO(entidad.getIdEnvio(), entidad.getIdPedido(), entidad.getDireccion(), entidad.getCiudad(), entidad.getEstado(), entidad.getFechaDespacho());
    }

    public List<EnvioDTO> obtenerTodos() {
        log.info("Obteniendo todos los envíos registrados");
        return repository.findAll().stream()
                .map(e -> new EnvioDTO(e.getIdEnvio(), e.getIdPedido(), e.getDireccion(), e.getCiudad(), e.getEstado(), e.getFechaDespacho()))
                .collect(Collectors.toList());
    }

    // Regla de Negocio: Avanzar el estado de la logística
    public EnvioDTO actualizarEstado(Integer id, String nuevoEstado) {
        log.info("Actualizando el estado del envío ID: {} a {}", id, nuevoEstado);
        Envio entidad = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envío no encontrado"));
        
        entidad.setEstado(nuevoEstado);
        if (nuevoEstado.equalsIgnoreCase("EN_RUTA") && entidad.getFechaDespacho() == null) {
            entidad.setFechaDespacho(LocalDateTime.now());
        }
        
        repository.save(entidad);
        return new EnvioDTO(entidad.getIdEnvio(), entidad.getIdPedido(), entidad.getDireccion(), entidad.getCiudad(), entidad.getEstado(), entidad.getFechaDespacho());
    }
}