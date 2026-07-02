package com.example.ms_resena.service;

import com.example.ms_resena.dto.ResenaDTO;
import com.example.ms_resena.exception.BadRequestException;
import com.example.ms_resena.exception.ResourceNotFoundException;
import com.example.ms_resena.model.Resena;
import com.example.ms_resena.repository.ResenaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResenaService {

    private final ResenaRepository resenaRepository;

    public ResenaDTO crearResena(ResenaDTO resenaDTO) {
        // Regla de negocio: la calificación debe ser válida
        if (resenaDTO.getCalificacion() == null || resenaDTO.getCalificacion() < 1 || resenaDTO.getCalificacion() > 5) {
            throw new BadRequestException("La calificación debe ser un valor entre 1 y 5 estrellas.");
        }

        Resena nuevaResena = resenaDTO.toModel();
        Resena resenaGuardada = resenaRepository.save(nuevaResena);
        return ResenaDTO.fromModel(resenaGuardada);
    }

    public List<ResenaDTO> obtenerResenasPorProducto(Long idProducto) {
        List<Resena> resenas = resenaRepository.findByIdProducto(idProducto);

        return resenas.stream()
                .map(ResenaDTO::fromModel)
                .collect(Collectors.toList());
    }

    public List<ResenaDTO> listar() {
        return resenaRepository.findAll().stream()
                .map(ResenaDTO::fromModel)
                .collect(Collectors.toList());
    }

    public ResenaDTO obtenerPorId(Long id) {
        Resena resena = resenaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la reseña con ID: " + id));

        return ResenaDTO.fromModel(resena);
    }

    public void eliminarResena(Long id) {
        Resena resena = resenaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la reseña con ID: " + id));
        
        resenaRepository.delete(resena);
    }
}