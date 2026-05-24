package com.tienda.ms_carrito.repository;

import com.tienda.ms_carrito.model.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Integer> {

        @Query("SELECT c FROM CarritoItem c WHERE c.id_cliente = :id_cliente")
        List<CarritoItem> findByIdCliente(Integer id_cliente); //Lo necesito para el endpoint
}
