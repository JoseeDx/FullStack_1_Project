package com.example.ms_descuento.repository; // Ajusta tu paquete

import com.example.ms_descuento.model.Descuento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DescuentoRepository extends JpaRepository<Descuento, Integer> {
    
    // Método clave para que el carrito busque si un cupón existe por su texto
    Optional<Descuento> findByCodigoCupon(String codigoCupon);
}