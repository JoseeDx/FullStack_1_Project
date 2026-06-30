package com.example.ms_resena.repository;

import com.example.ms_resena.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    // Método clave para traer todas las reseñas de un solo producto
    List<Resena> findByIdProducto(Long idProducto);
}