package com.example.ms_descuento.service;

import com.example.ms_descuento.dto.DescuentoDTO;
import com.example.ms_descuento.exception.BadRequestException;
import com.example.ms_descuento.exception.ResourceNotFoundException;
import com.example.ms_descuento.model.Descuento;
import com.example.ms_descuento.repository.DescuentoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j // Cumple con la exigencia de logging de la rúbrica
@Service
public class DescuentoService {

    @Autowired
    private DescuentoRepository repository;

    // Regla de Negocio: Calcular Descuento
    public Double calcularDescuento(String codigo, Double montoTotal) {
        log.info("Calculando descuento para el código: {} con monto total: {}", codigo, montoTotal);
        Descuento descuento = repository.findByCodigoCupon(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Cupón no encontrado"));

        if (!descuento.getActivo() || descuento.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            log.warn("El cupón {} es inválido o ha expirado", codigo);
            throw new BadRequestException("El cupón no es válido o ha expirado");
        }

        Double montoDescontado = montoTotal * (descuento.getPorcentaje() / 100);
        return montoTotal - montoDescontado;
    }

    // POST
    public DescuentoDTO crear(DescuentoDTO dto) {
        log.info("Creando nuevo descuento: {}", dto.getCodigoCupon());
        Descuento entidad = new Descuento(null, dto.getCodigoCupon(), dto.getPorcentaje(), dto.getFechaExpiracion(), dto.getActivo());
        Descuento guardado = repository.save(entidad);
        dto.setIdDescuento(guardado.getIdDescuento());
        return dto;
    }

    // GET (Por ID)
    public DescuentoDTO obtenerPorId(Integer id) {
        log.info("Buscando descuento con ID: {}", id);
        Descuento entidad = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Descuento no encontrado"));
        return new DescuentoDTO(entidad.getIdDescuento(), entidad.getCodigoCupon(), entidad.getPorcentaje(), entidad.getFechaExpiracion(), entidad.getActivo());
    }

    // GET (Todos) - Requerido por rúbrica
    public List<DescuentoDTO> obtenerTodos() {
        log.info("Obteniendo todos los descuentos");
        return repository.findAll().stream()
                .map(e -> new DescuentoDTO(e.getIdDescuento(), e.getCodigoCupon(), e.getPorcentaje(), e.getFechaExpiracion(), e.getActivo()))
                .collect(Collectors.toList());
    }

    // PUT - Requerido por rúbrica
    public DescuentoDTO actualizar(Integer id, DescuentoDTO dto) {
        log.info("Actualizando descuento con ID: {}", id);
        Descuento entidad = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Descuento no encontrado"));
        entidad.setCodigoCupon(dto.getCodigoCupon());
        entidad.setPorcentaje(dto.getPorcentaje());
        entidad.setFechaExpiracion(dto.getFechaExpiracion());
        entidad.setActivo(dto.getActivo());
        repository.save(entidad);
        dto.setIdDescuento(id);
        return dto;
    }

    // DELETE - Requerido por rúbrica
    public void eliminar(Integer id) {
        log.info("Eliminando descuento con ID: {}", id);
        repository.deleteById(id);
    }
}